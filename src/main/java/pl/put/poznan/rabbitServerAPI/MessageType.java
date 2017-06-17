package pl.put.poznan.rabbitServerAPI;

/**
 * Created by Miko on 13.05.2017.
 */
public enum MessageType {
    REGISTER, //rozglos swoj rejestr po modyfikacjach
    ACCESS, //popros o dostep do zasobu
    SIGNAL, //poinformuj, ze obiekt spelnia warunek zmiennej warunkowej
    SPREAD, //oglos, ze istniejesz (rozglos swoj monitorID)
    PERMISSION, //odpowiedź na ACCESS - zgadza sie, zeby inny obiekt dostal rejestr
    RELEASE; //przy wychodzeniu z sekcji krytycznej wysylane do wszystkich
}
