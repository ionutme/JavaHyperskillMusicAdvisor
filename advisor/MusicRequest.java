package advisor;

public enum MusicRequest {
    Categories,
    Featured,
    New,
    Playlists,
    Auth,
    Exit;

    public boolean equals(String string) {
        return this.toString().equals(string);
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
