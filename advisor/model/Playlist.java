package advisor.model;

public class Playlist {
    final String name;

    public Playlist(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return name;
    }
}
