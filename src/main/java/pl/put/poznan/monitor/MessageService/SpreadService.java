package pl.put.poznan.monitor.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.MessageType;
import pl.put.poznan.rabbitServerAPI.RabbitSender;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static pl.put.poznan.rabbitServerAPI.MessageType.REGISTER;
import static pl.put.poznan.rabbitServerAPI.MessageType.SPREAD;

/**
 * Created by Miko on 19.05.2017.
 */
public class SpreadService implements IMessageService{

    @Getter
    private MessageType myType = SPREAD;

    @Override
    public void service(Monitor monitor, Message message) throws IOException, TimeoutException {
        //Dodanie UUID procesu do listy znanych lokalnie procesow

        if(!monitor.getAllMonitors().containsKey(message.getMonitorID().toString())) {
            monitor.getAllMonitors().put(message.getMonitorID().toString(), false);
            System.out.println("ROZLGASZAM SWOJE UUID: " + monitor.getMonitorID());

            Message message2 = Message.builder()
                .messageType(SPREAD)
                .monitorID(monitor.getMonitorID())
                .build();
            String messageString2 = monitor.javaToJson(message2);
            monitor.broadcast(messageString2);

        }


        System.out.println("Jestem monitorem o UUID: " + monitor.getMonitorID());
        System.out.println("Znam monitory: ");

        for (String name: monitor.getAllMonitors().keySet()){

            String key =name.toString();
            String value = monitor.getAllMonitors().get(name).toString();
            System.out.println(key + " " + value);


        }

    }

}
