package pl.put.poznan.rabbitServer;

import java.util.Observable;

/**
 * Created by mikolaj on 06.05.17.
 */
public class RabbitReceiver extends Observable implements Receiver {

    @Override
    public Object receive(Message message) {
        return null;

        notifyObservers((Object) message);

    }
}
