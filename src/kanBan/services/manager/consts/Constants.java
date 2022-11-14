package kanBan.services.manager.consts;

import java.time.format.DateTimeFormatter;

public class Constants {

    private Constants() {}

    public static final int PORT = 8080;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public static String getAddressKVServices()  {
        return new String("http://localhost:8078/");
    }
}
