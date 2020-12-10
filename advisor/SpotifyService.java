package advisor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpotifyService {
    public static String send(HttpRequest request) {
        HttpClient client = HttpClient.newBuilder().build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException("Cannot send request! " +
                    "Error message: " + exception.getMessage());
        }

        return response.body();
    }
}
