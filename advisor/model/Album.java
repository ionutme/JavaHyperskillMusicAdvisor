package advisor.model;

public class Album {
    final String name;
    final String[] artists;

    public Album(String name, String... artists) {
        this.name = name;
        this.artists = artists;
    }

    @Override
    public String toString() {
        return name + " [" + String.join(", ", artists) + "]";
    }
}
