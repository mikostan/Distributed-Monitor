package pl.put.poznan.monitor.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.Channel;
import lombok.Getter;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.MessageType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static java.lang.Math.max;
import static pl.put.poznan.rabbitServerAPI.MessageType.ACCESS;
import static pl.put.poznan.rabbitServerAPI.MessageType.PERMISSION;

/**
 * Created by Miko on 02.06.2017.
 */
public class AccessService implements IMessageService {

    @Getter
    private MessageType myType = ACCESS;


    @Override
    public void service(Monitor monitor, Message message) throws IOException, TimeoutException {
        //ustal monitor.timeStamp po odebraniu
        monitor.setTimeStamp(max(monitor.getTimeStamp(),message.getTimeStamp())+1);

        //dodaj monitor zglaszajacy ACCESS do swojej kolejki monitorow
        monitor.getRequestLockQueue().put(message.getMonitorID().toString(), message.getTimeStamp());

        boolean permission = false;
        //TODO sprawdz, czy dajesz pozwolenia -> permission = YES/NO

        //zinkrementuj monitor.timeStamp przed send
        monitor.incrementTimestamp();

        Message messageOut = Message.builder()
            .messageType(PERMISSION)
            .permission(permission)
            .monitorID(monitor.getMonitorID())
            .timeStamp(monitor.getTimeStamp())
            .build();
        String messageStringOut = monitor.javaToJson(messageOut);

        Channel channel = monitor.getSender().getChannel();
        channel.basicPublish("", message.getMonitorID().toString(), null, messageStringOut.getBytes());

    }

}
