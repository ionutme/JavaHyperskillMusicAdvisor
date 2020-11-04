package advisor;

public class Command {
    Category category;
    final Request request;

    Command(Request request) {
        this.request = request;
    }

    Command(Request request, Category category) {
        this.request = request;
        this.category = category;
    }
}
