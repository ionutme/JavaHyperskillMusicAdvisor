package advisor.controller;

import advisor.ServerPoints;
import advisor.auth.AuthenticationService;
import advisor.auth.User;
import advisor.model.Category;
import advisor.model.MusicRequest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandCenter {
    private static final MusicAdvisor musicAdvisor = new MusicAdvisor(System.out::println);

    public static boolean dispatch(User user, ServerPoints serverPoints) {

        switch (getUserCommand(user.input)) {
            case "AUTH":
                AuthenticationService.authenticate(user, serverPoints.access);
                break;
            case "EXIT":
                System.out.println("---GOODBYE!---");
                return false;
            default:
                if (!user.isAuthenticated()) {
                    System.out.println("Please, provide access for application.");
                    break;
                }

                musicAdvisor.requestAdvice(getMusicRequest(user.input), user.accessToken, serverPoints.resource);
                break;
        }

        return true;
    }

    private static String getUserCommand(String userInput) {
        return userInput.split(" ", 2)[0].toUpperCase();
    }

    private static MusicRequest getMusicRequest(String userInput) {
        List<String> args = getMusicRequestArgs(userInput);

        String userRequest = args.get(0);
        MusicRequest musicRequest = MusicRequest.valueOf(userRequest);

        if (musicRequest.equals(MusicRequest.Playlists)) {
            String category = args.get(1);
            musicRequest.setCategory(Category.valueOf(category));
        }

        return musicRequest;
    }

    private static List<String> getMusicRequestArgs(String userInput) {
        return Arrays.stream(userInput.split("\\s+"))
                .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
                .collect(Collectors.toList());
    }
}
