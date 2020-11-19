package advisor.auth;

public class User {
    final String CLIENT_ID;
    final String CLIENT_SECRET;

    private boolean isAuthenticated;
    public String input;

    {
        CLIENT_ID = SpotifyConfiguration.SPOTIFY_CLIENT_ID;
        CLIENT_SECRET = SpotifyConfiguration.SPOTIFY_CLIENT_SECRET;

        isAuthenticated = false;
    }

    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    public void markAsAuthenticated() {
        this.isAuthenticated = true;
    }
}
