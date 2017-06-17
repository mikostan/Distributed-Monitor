package pl.put.poznan.monitor.MessageService;

import lombok.Getter;
import pl.put.poznan.monitor.Monitor;
import pl.put.poznan.rabbitServerAPI.Message;
import pl.put.poznan.rabbitServerAPI.MessageType;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static java.lang.Math.max;
import static pl.put.poznan.rabbitServerAPI.MessageType.PERMISSION;

/**
 * Created by Miko on 02.06.2017.
 */
public class PermissionService implements IMessageService {


    @Getter
    private MessageType myType = PERMISSION;

    @Override
    public void service(Monitor monitor, Message message) throws IOException, TimeoutException {
        //ustal monitor.timeStamp po odebraniu
        monitor.setTimeStamp(max(monitor.getTimeStamp(),message.getTimeStamp())+1);

        //dodaj pozwoloenie do listy pozwolen
        monitor.getAllMonitors().put(message.getMonitorID().toString(),true);
        //sprawdz czy masz juz pozwolenia od wszystkich
        Iterator it = monitor.getAllMonitors().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (pair.getValue().equals(false)) monitor.setPermissionFromAll(false);
            it.remove(); // avoids a ConcurrentModificationException
        }

            monitor.onPermissionGranted();

    }

}
