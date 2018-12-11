package Config;

import java.nio.charset.Charset;

public class ConfigureExample {
    // DB configure
    public static String URL = "localhost";
    public static int PORT = 3306;
    public static String USERNAME = "root";
    public static String PASSWORD = "root";
    public static String DB_NAME = "mud";
    public static byte[] SALT = "helloworldandmud".getBytes(Charset.forName("UTF-8"));
}
