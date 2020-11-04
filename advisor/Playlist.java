package advisor;

public class Playlist {
    final String name;

    Playlist(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return name;
    }
}
