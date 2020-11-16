package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static advisor.MusicRepository.*;
import static advisor.MusicRepository.getPlaylists;

public class MusicAdvisor {
    private static boolean isAuthenticated;

    private static final String clientId = "ffe57dd7c06d4b2dac46c0ba8c7dd63d";
    private static final String redirectUrl = "http://localhost:8080";
    private static HttpServer server;
    private static String accessServerUrl;

    private static String code = "";

    MusicAdvisor(String accessServerUrl) {
        this.accessServerUrl = accessServerUrl;
        isAuthenticated = false;
    }

    public boolean request(String input) {
        Command command = getCommand(input);

        if (!isAuthenticated(command)) {
            System.out.println("Please, provide access for application.");
            return false;
        }

        executeCommand(command);

        if (command.request == Request.Exit) {
            System.out.println("---GOODBYE!---");
            return true;
        }

        return false;
    }

    private static void executeCommand(Command command) {
        switch (command.request) {
            case New:
                System.out.println("---NEW RELEASES---");
                print(getNewReleases());
                break;
            case Featured:
                System.out.println("---FEATURED---");
                print(getFeatured());
                break;
            case Categories:
                System.out.println("---CATEGORIES---");
                print(getCategories());
                break;
            case Playlists:
                String categoryHeader = command.category.toString().toUpperCase();
                System.out.printf("---%s PLAYLISTS---\n", categoryHeader);

                print(getPlaylists(command.category).playlists);
                break;
            case Auth:
                System.out.println("use this link to request the access code:");
                System.out.println(getAuthLink());

                while (!isAuthenticated) {
                    authenticate();
                }

                break;
        }
    }

    private static void authenticate() {
        try {
            server = HttpServer.create();

            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/",
                    new HttpHandler() {
                        public void handle(HttpExchange exchange) {
                            code = getCode(exchange.getRequestURI().getQuery());

                            System.out.printf("Code received is:--|%s|--\n", code);

                            writeResponse(exchange, code.isEmpty()
                                    ? "Authorization code not found. Try again."
                                    : "Got the code. Return back to your program.");

                            if (!code.isEmpty()) {
                                System.out.println("code received on thread-" + Thread.currentThread().getId());
                                server.stop(1);

                                System.out.println("making http request for access_token...");

                                HttpRequest request = HttpRequest.newBuilder()
                                        .header("Content-Type", "application/x-www-form-urlencoded")
                                        .uri(URI.create(accessServerUrl + "/api/token"))
                                        .POST(HttpRequest.BodyPublishers.ofString(String.format(
                                                "grant_type=authorization_code&" +
                                                        "code=%s&" +
                                                        "redirect_uri=%s&" +
                                                        "client_id=%s&" +
                                                        "client_secret=%s",
                                                code, redirectUrl, clientId, "1883bd45d15543429c0dd3ac37857f13")))
                                        .build();

                                try {
                                    HttpClient client = HttpClient.newBuilder().build();
                                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                                    System.out.println("response:");
                                    System.out.println(response.body());
                                } catch (IOException | InterruptedException exception) {
                                    exception.printStackTrace();
                                }

                                /////////////////// move up ///////////////////
                                isAuthenticated = true;

                                System.out.println("---SUCCESS---");
                            }
                        }
                    }
            );

            server.start();

            System.out.println("waiting for code... on thread-" + Thread.currentThread().getId());

        } catch (IOException exception) {
            exception.printStackTrace();
        }
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

    private static boolean isAuthenticated(Command command) {
        return isAuthenticated ||
                command.request == Request.Auth ||
                command.request == Request.Exit;
    }

    private static String getAuthLink() {
        return String.format(accessServerUrl + "/authorize" +
                        "?client_id=%s&redirect_uri=%s&response_type=code",
                clientId,
                redirectUrl);
    }

    static Command getCommand(String userInput) {
        List<String> words = Arrays.stream(userInput.split("\\s+"))
                .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
                .collect(Collectors.toList());

        String userRequest = words.get(0);
        Request request = Request.valueOf(userRequest);

        if (request.equals(Request.Playlists)) {
            String category = words.get(1);

            return new Command(request, Category.valueOf(category));
        }

        return new Command(request);
    }

    private static void print(Object[] array) {
        for (Object item : array) {
            System.out.println(item);
        }
    }
}
