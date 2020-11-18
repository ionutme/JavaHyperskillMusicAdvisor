package advisor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpotifyAuthenticatorService {
    private final SpotifyConfiguration configuration;

    private static final HttpClient client;

    static {
        client = HttpClient.newBuilder().build();
    }

    public SpotifyAuthenticatorService() {
        configuration = new SpotifyConfiguration();
    }

    public boolean authenticate(User user) {
        System.out.println("making http request for access_token...");

        System.out.println("response:");

        HttpRequest request = getAuthorizationRequest()
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


    }

    private HttpRequest getAuthorizationRequest(String authorizationCode, String redirectUrl) {
        return HttpRequest.newBuilder()
                          .header("Content-Type", "application/x-www-form-urlencoded")
                          .uri(getUrlForApiToken())
                          .POST(getBodyParams(authorizationCode, redirectUrl))
                          .build();
    }

    private HttpRequest.BodyPublisher getBodyParams(String authorizationCode, String redirectUrl) {
        return HttpRequest.BodyPublishers.ofString(
                "grant_type=authorization_code" +
                     "&code=" + authorizationCode +
                     "&redirect_uri=" + redirectUrl +
                     "&client_id=" + configuration.getClientId() +
                     "&client_secret=" + configuration.getClientSecret());
    }

    private URI getUrlForApiToken() {
        return URI.create(configuration.getAccessServerUrl() + "/api/token");
    }
}
