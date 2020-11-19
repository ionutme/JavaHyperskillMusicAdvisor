package advisor;

import advisor.controller.CommandCenter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var in = new Scanner(System.in);

        boolean keepAsking = true;
        while(keepAsking) {
            String userInput = in.nextLine();

            keepAsking = CommandCenter.execute(userInput, tryGetAccessParam(args));
        }
    }

    private static String tryGetAccessParam(String[] args) {
        return args.length > 0 ? args[1] : null;
    }
}
