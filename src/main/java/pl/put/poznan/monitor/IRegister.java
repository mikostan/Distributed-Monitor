package pl.put.poznan.monitor;

/**
 * Created by mikolaj on 06.05.17.
 */
public interface IRegister {

    void put(String key, Object object);

    Object get(String key);
}
