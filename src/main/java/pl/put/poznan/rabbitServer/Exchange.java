package pl.put.poznan.rabbitServer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by mikolaj on 06.05.17.
 */
@AllArgsConstructor
public enum Exchange {

    FIRST("first"),
    SECOND("second");

    @Getter
    private String name;
}
