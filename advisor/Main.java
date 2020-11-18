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
import java.util.Scanner;
import java.util.stream.Collectors;

import static advisor.MusicRepository.*;

public class Main {
    private static boolean isAuthenticated;

    private static final String clientId = "ffe57dd7c06d4b2dac46c0ba8c7dd63d";
    private static final String redirectUrl = "http://localhost:8080";
    private static String spotifyAccessServerUrl;

    private static String code = "";

    Main() {
        isAuthenticated = false;
    }

    static String authorizationCode = "";

    public static void main(String[] args) {
        second_main(args);

        /*if (args.length > 0) {
            spotifyAccessServerUrl = args[1];
            if (spotifyAccessServerUrl == null) {
                spotifyAccessServerUrl = "https://accounts.spotify.com";
            }
        }

        var user = new User();
        var commandCenter = new CommandCenter(user, System.out::println, spotifyAccessServerUrl);

        var in = new Scanner(System.in);

        while(true) {
            Command command = getCommand(in.nextLine());

            if (command.request == Request.Exit) {
                System.out.println("---GOODBYE!---");
                return;
            }

            if (!user.isAuthenticated) {
                System.out.println("Please, provide access for application.");
                continue;
            }

            commandCenter.dispatch(command, authorizationCode);
        }*/
    }

    private static void second_main(String[] args) {
        if (args.length > 0) {
            spotifyAccessServerUrl = args[1];
            if (spotifyAccessServerUrl == null) {
                spotifyAccessServerUrl = "https://accounts.spotify.com";
            }
        }

        var in = new Scanner(System.in);

        while(true) {
            Command command = getCommand(in.nextLine());

            if (!isAuthenticated(command)) {
                System.out.println("Please, provide access for application.");
                continue;
            }

            executeCommand(command);

            if (command.request == Request.Exit) {
                System.out.println("---GOODBYE!---");
                return;
            }
        }
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

    public static class Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            code = getCode(exchange.getRequestURI().getQuery());

            System.out.printf("Code received is:--|%s|--\n", code);

            writeResponse(exchange, code.isEmpty()
                    ? "Authorization code not found. Try again." + " on Thread " + Thread.currentThread().getId()
                    : "Got the code. Return back to your program." + " on Thread " + Thread.currentThread().getId());

            if (!code.isEmpty()) {
                //server.stop(1);

                System.out.println("making http request for access_token..." + " on Thread " + Thread.currentThread().getId());

                HttpRequest request = buildHttpRequest();

                try {
                    HttpClient client = HttpClient.newBuilder().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    System.out.println("response:");
                    System.out.println(response.body());
                } catch (Exception exception) {
                    System.out.println("ERROR: " + exception.getMessage());
                }

                System.out.println("---SUCCESS---");

                isAuthenticated = true;
            }
        }

        private HttpRequest buildHttpRequest() {
            return HttpRequest.newBuilder()
                              .header("Content-Type", "application/x-www-form-urlencoded")
                              .uri(URI.create(spotifyAccessServerUrl + "/api/token"))
                              .POST(HttpRequest.BodyPublishers.ofString(
                                        "grant_type=authorization_code" +
                                              "&code=" + code +
                                              "&redirect_uri=" + redirectUrl +
                                              "&client_id=" + clientId +
                                              "&client_secret=" + "1883bd45d15543429c0dd3ac37857f13"))
                              .build();
        }
    }

    private static void authenticate() {
        try {
            HttpServer server = HttpServer.create();

            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/", new Handler());

            server.start();

            System.out.println("waiting for code... on thread-" + Thread.currentThread().getId());

            while (!isAuthenticated) {
            //while (code.isEmpty()) {
                Thread.sleep(100);
            }

            server.stop(5);

        } catch (Exception exception) {
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
        return String.format(spotifyAccessServerUrl + "/authorize" +
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
