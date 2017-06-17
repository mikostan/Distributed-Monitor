package pl.put.poznan.monitor;

import lombok.*;
import pl.put.poznan.monitor.IRegister;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mikolaj on 06.05.17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Register implements IRegister {

    private Map<String, Object> objects;
    private Set<String> locks;

    @Override
    public void put(String key, Object value) {
        objects.put(key, value);
    }

    @Override
    public Object get(String key) {
        return objects.get(key);
    }
}
