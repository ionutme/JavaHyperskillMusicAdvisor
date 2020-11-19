package advisor;

public class MusicAdvisor {

    public Album[] getNewReleases() {
        return MusicRepository.getNewReleases();
    }

    public Playlist[] getFeatured() {
        return MusicRepository.getFeatured();
    }

    public Category[] getCategories() {
        return MusicRepository.getCategories();
    }

    public Playlist[] getPlaylists(Category category) {
        return MusicRepository.getPlaylists(category).playlists;
    }
}
