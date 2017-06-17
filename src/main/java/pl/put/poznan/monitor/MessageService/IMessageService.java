package pl.put.poznan.monitor.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.MessageType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Miko on 13.05.2017.
 */
public interface IMessageService {

    MessageType getMyType();

    default boolean accept(MessageType type) {
        return type == getMyType();
    }

    void service(Monitor monitor, Message message) throws IOException, TimeoutException;
}
