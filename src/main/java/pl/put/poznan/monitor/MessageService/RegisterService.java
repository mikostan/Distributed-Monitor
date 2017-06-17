package pl.put.poznan.monitor.MessageService;

import com.fasterxml.jackson.databind.util.TypeKey;
import lombok.Getter;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.MessageType;

import java.util.Map;
import java.util.Objects;

import static pl.put.poznan.rabbitServerAPI.MessageType.REGISTER;

/**
 * Created by Miko on 13.05.2017.
 */
public class RegisterService implements IMessageService {

    @Getter
    private MessageType myType = REGISTER;

    @Override
    public void service(Monitor monitor, Message message) {
        //Zmergowanie rejestrow

    monitor.setReg(message.getRegister());

    }
}
