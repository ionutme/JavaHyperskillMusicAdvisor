package advisor;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static advisor.MusicRepository.*;

public class Main {
    private static boolean isAuthenticated;

    private static final String clientId = "ffe57dd7c06d4b2dac46c0ba8c7dd63d";
    private static final String redirectUrl = "http://localhost:8080";

    Main() {
        isAuthenticated = false;
    }

    public static void main(String[] args) {
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
                System.out.println(getAuthLink());
                System.out.println("---SUCCESS---");

                isAuthenticated = true;
                System.out.println();

                break;
        }
    }

    private static boolean isAuthenticated(Command command) {
        return isAuthenticated ||
               command.request == Request.Auth ||
               command.request == Request.Exit;
    }

    private static String getAuthLink() {
        return String.format("https://accounts.spotify.com/authorize" +
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
