package pl.put.poznan.rabbitServer;

/**
 * Created by mikolaj on 06.05.17.
 */
public interface Sender {

    void send(Object object, Exchange exchange);
}
