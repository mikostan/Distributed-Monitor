package pl.put.poznan.monitor.MessageService;

import lombok.Getter;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.MessageType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static java.lang.Math.max;
import static pl.put.poznan.rabbitServerAPI.MessageType.*;

/**
 * Created by Miko on 16.06.2017.
 */
public class ReleaseService implements IMessageService {

    @Getter
    private MessageType myType = RELEASE;

    @Override
    public void service(Monitor monitor, Message message) throws IOException, TimeoutException {
        //ustal monitor.timeStamp po odebraniu
        monitor.setTimeStamp(max(monitor.getTimeStamp(),message.getTimeStamp())+1);

        monitor.getRequestLockQueue().remove(message.getMonitorID());
        monitor.onPermissionGranted();
    }
}

