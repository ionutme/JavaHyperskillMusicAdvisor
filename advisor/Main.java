package advisor;

import advisor.auth.User;
import advisor.controller.CommandCenter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var in = new Scanner(System.in);

        var user = new User();

        boolean keepAsking = true;
        while(keepAsking) {
            user.input = in.nextLine();

            keepAsking = CommandCenter.dispatch(user, tryGetAccessParam(args));
        }
    }

    private static String tryGetAccessParam(String[] args) {
        return args.length > 0 ? args[1] : null;
    }
}
