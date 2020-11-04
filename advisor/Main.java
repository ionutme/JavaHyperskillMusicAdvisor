package advisor;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static advisor.MusicRepository.*;

public class Main {
    public static void main(String[] args) {
        var in = new Scanner(System.in);

        while(true) {
            Command command = getCommand(in.nextLine());

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
                case Exit:
                    System.out.println("---GOODBYE!---");
                    return;
                case Playlists:
                    String categoryHeader = command.category.toString().toUpperCase();
                    System.out.printf("---%s PLAYLISTS---\n", categoryHeader);

                    print(getPlaylists(command.category).playlists);
                    break;
            }
        }
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
