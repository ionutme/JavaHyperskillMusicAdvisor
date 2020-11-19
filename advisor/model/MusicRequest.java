package advisor.model;

public enum MusicRequest {
    Categories,
    Featured,
    New,
    Playlists;

    private Category category;

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean equals(String string) {
        return this.toString().equals(string);
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
