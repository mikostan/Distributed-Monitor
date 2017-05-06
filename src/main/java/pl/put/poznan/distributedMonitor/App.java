package pl.put.poznan.distributedMonitor;

import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.monitor.Register;
import pl.put.poznan.rabbitServer.RabbitSender;
import pl.put.poznan.rabbitServer.Sender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikolaj on 06.05.17.
 */
public class App {

    public  static void main(String[] args) {
        List<String> wareHouse = new ArrayList<>();
        Sender sender = new RabbitSender();
        Monitor monitor = Monitor.builder()
                .reg(Register.builder()
                    .reg(Monitor.downloadRegister())
                    .build())
                .sender(sender)
                .build();
        //dodanie obiektu do rejestru
        monitor.put("lista", wareHouse);

    }
}
