package pl.put.poznan.rabbitServerAPI;

import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.put.poznan.monitor.Monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

/**
 * Created by mikolaj on 06.05.17.
 */

@AllArgsConstructor
@Slf4j
public class RabbitReceiver extends Observable implements Runnable {

    private UUID processID;
    private Connection connection;
    private Channel channel;
    private List<String> exchangeList = new ArrayList<>();
//    private final String EXCHANGE_NAME = "broadcast";

    public RabbitReceiver(UUID processID) throws IOException, TimeoutException {
        this.processID = processID;
        connection = createConnection();
        channel = connection.createChannel();
        declareExchange("broadcast", BuiltinExchangeType.FANOUT);
        bindQueueToExchange("broadcast");
    }

    public Connection createConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        return connection;
    }

    public void declareExchange(String exchangeName, BuiltinExchangeType exchangeType) throws IOException, TimeoutException {
        if (!exchangeList.contains(exchangeName)) {
            channel.exchangeDeclare(exchangeName, exchangeType);
            exchangeList.add(exchangeName);
            System.out.println("Zadeklarowano exchange po stronie receivera: " + exchangeName);
        }
    }

    public void bindQueueToExchange(String exchangeName) throws IOException {
        String queueName = channel.queueDeclare(String.valueOf(processID), false, true, false, null).getQueue();
        System.out.println("Nazwa kolejki receivera: " + queueName);
        channel.queueBind(queueName, exchangeName, "");
    }

    public void receive() throws IOException, TimeoutException {

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                setChanged();
                notifyObservers(message);
            }
        };

        channel.basicConsume(String.valueOf(processID), true, consumer);

    }

    @Override
    public void run() {
        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
