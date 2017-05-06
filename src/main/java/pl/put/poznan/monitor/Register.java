package pl.put.poznan.monitor;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import pl.put.poznan.monitor.IRegister;

import java.util.Map;

/**
 * Created by mikolaj on 06.05.17.
 */
@Builder
@RequiredArgsConstructor
public class Register implements IRegister {

    private final Map<String, Object> reg;

    @Override
    public void put(String key, Object value) {
        reg.put(key, value);
    }

    @Override
    public Object get(String key) {
        return reg.get(key);
    }
}
