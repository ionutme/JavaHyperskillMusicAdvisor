package advisor;

public class CategoryPlaylists {
    final Category category;
    final Playlist[] playlists;

    CategoryPlaylists(Category category, Playlist[] playlists) {
        this.category = category;
        this.playlists = playlists;
    }
}
