package advisor;

public class AuthenticationService {
    private static final String REDIRECT_URL;
    public static final String DEFAULT_ACCESS_SERVER_URL;

    static {
        REDIRECT_URL = LocalHttpServerConfiguration.LOCAL_HTTP_SERVER_URL;
        DEFAULT_ACCESS_SERVER_URL = SpotifyConfiguration.SPOTIFY_ACCESS_SERVER_URL;
    }

    static void authenticate(User user, String userChosenAccessServerUrl) {
        String accessServerUrl = getAccessServerUrl(userChosenAccessServerUrl);

        System.out.println("use this link to request the access code:");
        System.out.println(getAuthLink(user.CLIENT_ID, accessServerUrl));

        var handler = new SpotifyAuthenticatorHandler(user, accessServerUrl);
        try (var server = new LocalHttpServer(handler)) {
            waitAuthentication(user);
        }
    }

    private static void waitAuthentication(User user) {
        System.out.println("waiting for code...");

        while (!user.isAuthenticated) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static String getAccessServerUrl(String userChosenAccessServerUrl) {
        return userChosenAccessServerUrl == null
               ? DEFAULT_ACCESS_SERVER_URL
               : userChosenAccessServerUrl;
    }

    private static String getAuthLink(String clientId, String accessServerUrl) {
        return  accessServerUrl + "/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + REDIRECT_URL +
                "&response_type=code";
    }
}
