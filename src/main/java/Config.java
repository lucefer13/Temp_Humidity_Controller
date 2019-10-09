import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final File PROPERTIES_FILE = new File("config/controller.properties");
    private static final Config INSTANCE = new Config();
    private String portNumber;

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            properties.load(is);
            portNumber = properties.getProperty("port.number");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPERTIES_FILE.getAbsolutePath());
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public String getPortNumber() {
        return portNumber;
    }
}
