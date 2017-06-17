package pl.put.poznan.distributedMonitor.ProducerConsumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.put.poznan.monitor.Monitor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Miko on 16.05.2017.
 */
@Data
@Builder
@AllArgsConstructor
public class Producer implements Runnable {

    private static final int BUFF_SIZE = 10;
    private Monitor monitor;
    private Map<String, Object> objects;

    public Producer() throws IOException, TimeoutException {
        monitor = new Monitor();
        this.objects = monitor.getReg().getObjects();
    }

    @SuppressWarnings("unchecked")
    private Object produce(Object obj) {
        if (obj instanceof Map) {
            ((Map) obj).put("Piotr", "newObject");
        }
        return obj;
    }

    private Boolean check(Object obj) {
        return obj instanceof Map && ((Map) obj).entrySet().size() <= BUFF_SIZE;
    }


    private void produce() throws InterruptedException, IOException, TimeoutException {

        HashMap<String, String> bufor = new HashMap<>();
        monitor.addObjectToRegister("bufor_key", bufor);

//        while (true) {
          monitor.execute("bufor_key", this::produce, this::check, "lock1", "isFull", "isEmpty");
            //rejestracja u monitora
          //        }
    }

    //update {execute}
    //Producent obserwuje monitor

    @Override
    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
