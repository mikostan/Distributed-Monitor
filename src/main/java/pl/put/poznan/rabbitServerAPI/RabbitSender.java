package pl.put.poznan.rabbitServerAPI;

import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by mikolaj on 06.05.17.
 */
@Data
public class RabbitSender {

    //    connection - 1 na watek
//    channel    - 1 na watek
    private UUID monitorID;
    private Connection connection;
    private Channel channel;
    private List<String> exchangeList = new ArrayList<>();

    public RabbitSender(UUID monitorID) throws IOException, TimeoutException {
        this.monitorID = monitorID;
        connection = createConnection();
        channel = connection.createChannel();
        declareExchange("broadcast", BuiltinExchangeType.FANOUT);
    }


    public Connection createConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        return connection;
    }


//    private static final String EXCHANGE_NAME = "broadcast";

    public void declareExchange(String exchangeName, BuiltinExchangeType exchangeType) throws IOException, TimeoutException {
        if (!exchangeList.contains(exchangeName)) {
            channel.exchangeDeclare(exchangeName, exchangeType);
            exchangeList.add(exchangeName);
            System.out.println("Zadeklarowano exchange po stronie sendera: " + exchangeName);
        }
    }

    public void sendToExchange(String exchangeName, BuiltinExchangeType exchangeType, String message) throws IOException, TimeoutException {
        declareExchange(exchangeName, exchangeType);
        channel.basicPublish(exchangeName, "", null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");
    }

//    public void broadcast(String message) throws IOException, TimeoutException {
//        connectToExchange("broadcast");
//
//        //czy queueName = broadcast?
//        String queueName = channel.queueDeclare("broadcast", true, false, false, null).getQueue();
//        channel.queueBind("broadcast", EXCHANGE_NAME, "");
//
//        channel.basicPublish(EXCHANGE_NAME, "",  null, message.getBytes("UTF-8"));
//        System.out.println(" [x] Sent '" + message + "'");
//
//        channel.close();
//        connection.close();

//    }


}
