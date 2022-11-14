package kanBan.services.manager.consts;

public class Constants {

    private Constants() {}

    public static final int PORT = 8080;

    public static String getAddressKVServices()  {
        return new String("http://localhost:8078/");
    }
}
