package advisor;

public class User {
    final String CLIENT_ID;
    final String CLIENT_SECRET;

    boolean isAuthenticated;

    {
        CLIENT_ID = SpotifyConfiguration.SPOTIFY_CLIENT_ID;
        CLIENT_SECRET = SpotifyConfiguration.SPOTIFY_CLIENT_SECRET;

        isAuthenticated = false;
    }
}
