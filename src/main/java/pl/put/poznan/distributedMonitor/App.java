package pl.put.poznan.distributedMonitor;

import lombok.extern.slf4j.Slf4j;
import pl.put.poznan.distributedMonitor.ProducerConsumer.Consumer;
import pl.put.poznan.distributedMonitor.ProducerConsumer.Producer;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.monitor.Register;
import pl.put.poznan.rabbitServerAPI.RabbitReceiver;
import pl.put.poznan.rabbitServerAPI.RabbitSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

/**
 * Created by mikolaj on 06.05.17.
 */
@Slf4j
public class App {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
//        List<String> ob1 = new ArrayList<>();
//        RabbitSender sender = new RabbitSender();
//        RabbitReceiver receiver = new RabbitReceiver();
////        Monitor monitor = new Monitor();
//        Monitor monitor = Monitor.builder()
//                .monitorID(Monitor.generateID())
//                .reg(Register.builder()
//                    .reg(Monitor.downloadRegister())
//                    .build())
//                .sender(sender)
//                .receiver(receiver)
//                .build();
//
//
//        new Thread(receiver).start();
//        monitor.put("ob1_key", ob1);
//        Producer producer = new Producer();
        Monitor monitor = new Monitor();
//        List<String> ob1 = new ArrayList<>();
//        monitor.addObjectToRegister("kot", ob1);
//        String ob2 = "kotek";
//        monitor.addObjectToRegister("ob1_key", ob1);
//        monitor.put("ob2_key", ob2);
//        System.out.println("Jestem procesem o UUID: " + monitor.getMonitorID());
//        System.out.println("Lista znanych mi procesow: ");
//        Iterator it = monitor.getAllMonitors().entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
//            it.remove(); // avoids a ConcurrentModificationException
//        }

//        //PRODUCENT KONSUMENT
//        Producer producer = Producer.builder()
//            .build();
//        Consumer consumer = Consumer.builder()
//            .build();
//        new Thread(producer).start();
//        new Thread(consumer).start();


    }
}
