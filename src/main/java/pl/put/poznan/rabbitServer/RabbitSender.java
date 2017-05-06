package pl.put.poznan.rabbitServer;

/**
 * Created by mikolaj on 06.05.17.
 */
public class RabbitSender implements Sender {

    @Override
    public void send(Object object, Exchange exchange) {
        channel.basicPublish(exchange.getName(), QUEUE_NAME, null, message.getBytes("UTF-8"));

    }
}
