package pl.put.poznan.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import lombok.Builder;
import lombok.Data;
import pl.put.poznan.monitor.MessageService.IMessageService;
import pl.put.poznan.monitor.MessageService.RegisterService;
import pl.put.poznan.monitor.MessageService.RequestService;
import pl.put.poznan.monitor.MessageService.SpreadService;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.RabbitReceiver;
import pl.put.poznan.rabbitServerAPI.RabbitSender;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static pl.put.poznan.rabbitServerAPI.MessageType.*;


/**
 * Created by mikolaj on 06.05.17.
 */

@Data
//monitor jest obserwatorem
public class Monitor implements Observer {

    int timeStamp=0;
    private UUID monitorID;
    private Map<String, Boolean> allMonitors = new HashMap<>();
    private Register reg;
//    private Map<String, Consumer<Object>> functions;
//    private Map<String, Consumer<Object>> predicats;
    private RabbitSender sender;
    private RabbitReceiver receiver;
    private Map<String, Integer> requestLockQueue;
    private boolean permissionFromAll=true;

    String label;
    Function<Object, Object> func;
    Predicate<Object> pred;
    String lock;
    String conditionalVariable;
    String conditionalVariable2;


    public Monitor() throws IOException, TimeoutException {
        monitorID = generateID();
        reg = new Register();
//        reg.setObjects(downloadObjects());
        allMonitors.put(String.valueOf(monitorID), true);
        sender = new RabbitSender(monitorID);
        receiver = new RabbitReceiver(monitorID);
        new Thread(receiver).start();
        receiver.addObserver(this);

        Message message = Message.builder()
            .messageType(SPREAD)
            .monitorID(monitorID)
            .build();
        String messageString = javaToJson(message);
        broadcast(messageString);

    }

    public static UUID generateID() {
        return UUID.randomUUID();
    }


//prawdopodobnie niepotrzbne, bo i tak rozglaszamy rejestr po zmianach
//    public static Map<String, Object> downloadObjects() {
////        if (nie jestem pierwszy) {
////            return pobranyRejestr;
////        }
//        return new HashMap<>();
//    }

    //dodaje obiekt do rejestru i go rozgłasza
    public void addObjectToRegister(String key, Object obj) throws IOException, TimeoutException {
        String generalLock = "generalLock";
        getLock(generalLock);
        reg.put(key, obj);

        Message message = Message.builder()
            .register(reg)
            .messageType(REGISTER)
            .monitorID(monitorID)
            .build();
        String messageString = javaToJson(message);

        broadcast(messageString);
        releaseLock(generalLock);

    }

    public void execute(String label, Function<Object, Object> func, Predicate<Object> pred, String lock, String conditionalVariable, String conditionalVariable2) throws IOException, TimeoutException {

        getLock(lock);
        this.label=label;
        this.func=func;
        this.pred=pred;
        this.lock=lock;
        this.conditionalVariable=conditionalVariable;
        this.conditionalVariable2=conditionalVariable2;

    }

    public void incrementTimestamp() {
        timeStamp=timeStamp+1;
    }

    public void getLock(String zamek) throws IOException, TimeoutException {

        //zinkrementuj timeStamp przed send
        incrementTimestamp();
        requestLockQueue.put(monitorID.toString(), timeStamp);

        //send message for access
        Message message = Message.builder()
            .messageType(ACCESS)
            .monitorID(monitorID)
            .timeStamp(timeStamp)
            .build();
        String messageString = javaToJson(message);

        broadcast(messageString);

    }

    public void onPermissionGranted() throws IOException, TimeoutException {
        if (permissionFromAll==true && checkIfMyTimeStampIsTheBiggest()) {
            //TODO wyczysc permissions

            if (pred.test(reg.get(label))) {
                reg.put(label, func.apply(reg.get(label)));

                Message message = Message.builder()
                    .register(reg)
                    .messageType(REGISTER)
                    .monitorID(monitorID)
                    .build();
                String messageString = javaToJson(message);

                broadcast(messageString);

                //notify producer
                signal(conditionalVariable2);

            } else {
                wait(lock, conditionalVariable);
            }
            releaseLock(lock);
        }
    }

   public Boolean checkIfMyTimeStampIsTheBiggest() {
        Boolean MyTimeStampTheBiggest = true;

       for (Map.Entry<String, Integer> entry : requestLockQueue.entrySet())
       {
           if(entry.getValue()>timeStamp) {MyTimeStampTheBiggest=false;}
       }

        return MyTimeStampTheBiggest;
   }

    public void releaseLock(String lock1) throws IOException, TimeoutException {
        //zinkrementuj timeStamp przed send
        incrementTimestamp();

        requestLockQueue.remove(lock1);

        Message message = Message.builder()
            .register(reg)
            .messageType(RELEASE)
            .monitorID(monitorID)
            .timeStamp(timeStamp)
            .build();
        String messageString = javaToJson(message);

        broadcast(messageString);
    }


    public void wait(String lock, String conditionalVariable) throws IOException, TimeoutException {
        receiver.bindQueueToExchange(conditionalVariable);
        releaseLock(lock);
    }

    public void signal(String conditionalVariable) throws IOException, TimeoutException {
        //poinformowanie watkow czekajacych na zamek/spelnienie warunku w exchange'u o nazwie takiej jak zamek/zmienna warunkowa
        Message message = Message.builder()
            .messageType(SIGNAL)
            .build();
        String messageString = javaToJson(message);

        sender.sendToExchange(conditionalVariable, BuiltinExchangeType.FANOUT, messageString);
    }

    public void broadcast(String messageString) throws IOException, TimeoutException {

        sender.sendToExchange("broadcast", BuiltinExchangeType.FANOUT, messageString);
    }

    @Override
    public void update(Observable o, Object arg) {

        String messageString = (String) arg;

        Message message = null;
        try {
            message = jsonToJava(messageString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, IMessageService> messageService = new HashMap<>();
        messageService.put("access", new RequestService());
        messageService.put("register", new RegisterService());
        messageService.put("spread", new SpreadService());

        for (Map.Entry<String, IMessageService> entry : messageService.entrySet()) {
            if (entry.getValue().accept(message.getMessageType())) try {
                entry.getValue().service(this, message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

//    public Register mergeRegisters(Register reg1, Register reg2) {
//
//    }

    public String javaToJson(Message message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(message);
    }

    public Message jsonToJava(String messageString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(messageString, Message.class);
    }


}
