package advisor;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static advisor.MusicRepository.*;

public class Main {
    public static void main(String[] args) {
        var in = new Scanner(System.in);
        var user = new User();

        while(true) {
            Command command = getCommand(in.nextLine());

            if (command.musicRequest != MusicRequest.Auth &&
                command.musicRequest != MusicRequest.Exit &&
                !user.isAuthenticated) {
                System.out.println("Please, provide access for application.");
                continue;
            }

            switch (command.musicRequest) {
                case Exit:
                    System.out.println("---GOODBYE!---");
                    return;
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
                    AuthenticationService.authenticate(user, args.length > 0 ? args[1] : null);
                    break;
            }
        }
    }

    static Command getCommand(String userInput) {
        List<String> words = Arrays.stream(userInput.split("\\s+"))
                                    .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
                                    .collect(Collectors.toList());

        String userRequest = words.get(0);
        MusicRequest musicRequest = MusicRequest.valueOf(userRequest);

        if (musicRequest.equals(MusicRequest.Playlists)) {
            String category = words.get(1);

            return new Command(musicRequest, Category.valueOf(category));
        }

        return new Command(musicRequest);
    }

    private static void print(Object[] array) {
        for (Object item : array) {
            System.out.println(item);
        }
    }
}
