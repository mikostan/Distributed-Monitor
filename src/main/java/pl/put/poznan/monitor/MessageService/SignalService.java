package pl.put.poznan.monitor.MessageService;

import lombok.Getter;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.MessageType;

import java.util.Objects;

import static pl.put.poznan.rabbitServerAPI.MessageType.REGISTER;
import static pl.put.poznan.rabbitServerAPI.MessageType.SIGNAL;

/**
 * Created by Miko on 17.05.2017.
 */
public class SignalService implements IMessageService{

    @Getter
    private MessageType myType = SIGNAL;

    @Override
    public void service(Monitor monitor, Message message) {
        //wykonaj odlozona funkcje
        monitor.getFunc().apply(monitor.getReg().get(monitor.getLabel()));
        //usun odlozona funkcje i predykat
        monitor.setFunc(null);
        monitor.setPred(null);
        monitor.setLabel(null);
        monitor.setLock(null);
        monitor.setConditionalVariable(null);
        monitor.setConditionalVariable2(null);
    }

}
