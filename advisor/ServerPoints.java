package advisor;

import advisor.auth.SpotifyConfiguration;

public class ServerPoints {
    public final String access;
    public final String resource;

    ServerPoints(String[] args) {
        String access = tryGetParam(args, "access");
        this.access = access != null
                ? access
                : SpotifyConfiguration.SPOTIFY_ACCESS_SERVER_URL;

        String resource = tryGetParam(args, "resource");
        this.resource = resource != null
                ? resource
                : SpotifyConfiguration.SPOTIFY_RESOURCES_SERVER_URL;
    }

    private static String tryGetParam(String[] args, String paramName) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equalsIgnoreCase(paramName)) {
                return args[i + 1];
            }
        }

        return null;
    }
}
