package advisor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    public static final String CONFIG_FILENAME = "config.properties";

    protected Properties properties;

    Configuration() {
        loadPropertiesFile();
    }

    private void loadPropertiesFile() {
        properties = new Properties();
        InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILENAME);

        try {
            properties.load(inputStream);
        } catch (IOException ignored) {
        }
    }
}

class SpotifyConfiguration extends Configuration {

    public static final String SPOTIFY_ACCESS_SERVER_URL = "spotifyAccessServerUrl";
    public static final String SPOTIFY_CLIENT_ID = "spotifyClientId";

    public String getAccessServerUrl() {
        return properties.getProperty(SPOTIFY_ACCESS_SERVER_URL);
    }

    public String getSpotifyClientId() {
        return properties.getProperty(SPOTIFY_CLIENT_ID);
    }
}

public class LocalHttpServerConfiguration extends Configuration {

    public static final String LOCAL_HTTP_SERVER_URL = "localHttpServerUrl";
    public static final String LOCAL_HTTP_SERVER_PORT = "localHttpServerPort";

    public String getUrl() {
        return properties.getProperty(LOCAL_HTTP_SERVER_URL);
    }

    public String getPort() {
        return properties.getProperty(LOCAL_HTTP_SERVER_PORT);
    }
}
