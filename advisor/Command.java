package advisor;

public class Command {
    Category category;
    final MusicRequest musicRequest;

    Command(MusicRequest musicRequest) {
        this.musicRequest = musicRequest;
    }

    Command(MusicRequest musicRequest, Category category) {
        this.musicRequest = musicRequest;
        this.category = category;
    }
}
