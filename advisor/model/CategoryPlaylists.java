package advisor.model;

public class CategoryPlaylists {
    final Category category;
    public final Playlist[] playlists;

    public CategoryPlaylists(Category category, Playlist[] playlists) {
        this.category = category;
        this.playlists = playlists;
    }
}
