package advisor;

public enum Request {
    Categories,
    Exit,
    Featured,
    New,
    Playlists;

    public boolean equals(String string) {
        return this.toString().equals(string);
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
