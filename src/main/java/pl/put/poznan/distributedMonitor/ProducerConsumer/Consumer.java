package pl.put.poznan.distributedMonitor.ProducerConsumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Consumer implements Runnable {

    private Monitor monitor;
    private Map<String, Object> objects;

    public Consumer() throws IOException, TimeoutException {
        monitor = new Monitor();
    }

    public void consume() throws InterruptedException, IOException, TimeoutException {
        HashMap<String, String> bufor = new HashMap<>();

        while (true) {

            monitor.getLock("lock1");

            while (monitor.getReg().getObjects().isEmpty()) {
                monitor.wait("lock1", "isFull");
            }

            bufor = (HashMap<String, String>) monitor.getReg().getObjects().get("bufor");
            bufor.remove("Miko");
            this.objects=null;
            this.objects.put("bufor", bufor);
            monitor.getReg().setObjects(this.objects);

            monitor.signal("isFull");
            monitor.releaseLock("lock1");

        }
    }

    @Override
    public void run() {
        try {
            consume();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
