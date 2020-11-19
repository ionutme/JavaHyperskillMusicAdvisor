package advisor.auth;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthenticatorHandler implements HttpHandler {
    private final User user;
    private final String accessServerUrl;

    public AuthenticatorHandler(User user, String accessServerUrl) {
        this.user = user;
        this.accessServerUrl = accessServerUrl;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String code = getCode(exchange.getRequestURI().getQuery());

        writeResponse(exchange, code.isEmpty()
                ? "Authorization code not found. Try again."
                : "Got the code. Return back to your program.");

        if (!code.isEmpty()) {
            System.out.println("making http request for access_token...");

            String response = send(buildRequest(code));

            System.out.println("response:");
            System.out.println(response);

            System.out.println("---SUCCESS---");

            this.user.markAsAuthenticated();
        }
    }

    private static String send(HttpRequest request) {
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

    private HttpRequest buildRequest(String code) {
        var params = new HttpRequestBodyParams(this.user, code, this.accessServerUrl);

        return buildHttpRequest(params);
    }

    private static HttpRequest buildHttpRequest(HttpRequestBodyParams params) {
        return HttpRequest.newBuilder()
                          .header("Content-Type", "application/x-www-form-urlencoded")
                          .uri(URI.create(params.accessServerUrl + "/api/token"))
                          .POST(HttpRequest.BodyPublishers.ofString(
                                  "grant_type=authorization_code" +
                                        "&code=" + params.code +
                                        "&redirect_uri=" + params.redirectUrl +
                                        "&client_id=" + params.clientId +
                                        "&client_secret=" + params.clientSecret))
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

    static class HttpRequestBodyParams {
        final String code;
        final String clientId;
        final String clientSecret;
        final String accessServerUrl;
        final String redirectUrl;

        HttpRequestBodyParams(User user, String code, String accessServerUrl) {
            this.code = code;
            this.clientId = user.CLIENT_ID;
            this.clientSecret = user.CLIENT_SECRET;
            this.accessServerUrl = accessServerUrl;
            this.redirectUrl = LocalHttpServerConfiguration.LOCAL_HTTP_SERVER_URL;
        }
    }
}
