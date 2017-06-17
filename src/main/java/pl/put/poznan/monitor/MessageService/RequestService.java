package pl.put.poznan.monitor.MessageService;

import lombok.Getter;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.MessageType;

import java.util.Objects;

import static pl.put.poznan.rabbitServerAPI.MessageType.ACCESS;
import static pl.put.poznan.rabbitServerAPI.MessageType.REGISTER;

/**
 * Created by Miko on 13.05.2017.
 */
public class RequestService implements IMessageService {

    @Getter
    private MessageType myType = ACCESS;

    @Override
    public void service(Monitor monitor, Message message) {
    }
}
