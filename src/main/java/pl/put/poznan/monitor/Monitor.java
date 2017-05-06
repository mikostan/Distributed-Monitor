package pl.put.poznan.monitor;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import pl.put.poznan.rabbitServer.Exchange;
import pl.put.poznan.rabbitServer.Sender;
import sun.plugin2.message.Message;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by mikolaj on 06.05.17.
 */
@Builder
@RequiredArgsConstructor
public class Monitor implements Observer {

    private final IRegister reg;
    private final Sender sender;
    private final Map<String, Consumer<Object>> orders;

    public void execute(String key, Consumer<Object> func) {
       wsyylam zapytanie do wsyzstkich watkow
                rejestruje sie u receviera
                receiver.addObserver(this)
                orders.put(key, func);

    }

    public void action(Message message) {
        jezeli wiadomosc to zgoda to dodaj to listy zgod

                jesli mam zgode od wszyskich
        //akceptacja od pozostalych watkow
        orders.get(message.getKey()).accept(reg.get(key));
        czyszcze liste zgod
    }


    public static Map<String, Object> downloadRegister() {
        if (nie jestem pierwszy) {
            return pobranyRejestr;
        }
        return new HashMap<>();
    }

    public void put(String key, Object obj) {
    //dodaje obiekt do rejestru i go rozgłasza
    }

    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message) arg;
        if messae is mine? {
            dodaje zgode
        }
        if full of zgoda {
            func.accept(orders.get(message.getKey()))
        }
    }
}
