package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpotifyAuthenticatorHandler implements HttpHandler {
    private static final String redirectUrl = "http://localhost:8080";

    private final User user;
    private final String spotifyAccessServerUrl;

    public SpotifyAuthenticatorHandler(User user, String spotifyAccessServerUrl) {
        this.user = user;
        this.spotifyAccessServerUrl = spotifyAccessServerUrl;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String code = getCode(exchange.getRequestURI().getQuery());

        writeResponse(exchange, code.isEmpty()
                ? "Authorization code not found. Try again."
                : "Got the code. Return back to your program.");

        if (!code.isEmpty()) {
            System.out.println("making http request for access_token..." + " on Thread " + Thread.currentThread().getId());

            HttpRequest request = buildHttpRequest(code);

            try {
                HttpClient client = HttpClient.newBuilder().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("response:");
                System.out.println(response.body());
            } catch (Exception exception) {
            }

            System.out.println("---SUCCESS---");

            user.isAuthenticated = true;
        }
    }

    private HttpRequest buildHttpRequest(String code) {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(spotifyAccessServerUrl + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code" +
                                "&code=" + code +
                                "&redirect_uri=" + redirectUrl +
                                "&client_id=" + user.CLIENT_ID +
                                "&client_secret=" + user.CLIENT_SECRET))
                .build();
    }

    private static void writeResponse(HttpExchange exchange, String responseMessage) {
        try {
            exchange.sendResponseHeaders(200, responseMessage.length());
            exchange.getResponseBody().write(responseMessage.getBytes());
            exchange.getResponseBody().close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static String getCode(String query) {
        String codeParam = "code=";
        if (query == null || !query.contains(codeParam)) {
            return "";
        }

        System.out.println("QUERY IS:--|" + query + "|--");

        int indexAfterCodeParam = query.indexOf(codeParam) + codeParam.length();

        return query.substring(indexAfterCodeParam);
    }
}
