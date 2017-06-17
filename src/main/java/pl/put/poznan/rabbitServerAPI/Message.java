package pl.put.poznan.rabbitServerAPI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.put.poznan.monitor.Register;

import java.util.UUID;

/**
 * Created by Miko on 12.05.2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private MessageType messageType;
    private Integer counter;
    private UUID monitorID;
    private Register register;
    private Integer timeStamp;
    private boolean permission;
}
