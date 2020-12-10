package advisor;

import advisor.auth.SpotifyConfiguration;
import advisor.auth.User;
import advisor.controller.CommandCenter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var in = new Scanner(System.in);

        offerAdvice(in, new ServerPoints(args));
    }

    private static void offerAdvice(Scanner in, ServerPoints serverPoints) {
        var user = new User();

        boolean keepAsking = true;
        while(keepAsking) {
            user.input = in.nextLine();

            keepAsking = CommandCenter.dispatch(user, serverPoints);
        }
    }
}
