package advisor.controller;

import advisor.data.MusicRepository;
import advisor.model.Album;
import advisor.model.Category;
import advisor.model.MusicRequest;
import advisor.model.Playlist;

import java.util.function.Consumer;

public class MusicAdvisor {

    private final Consumer<String> writeFunc;

    MusicAdvisor(Consumer<String> writeFunc) {
        this.writeFunc = writeFunc;
    }

    public void requestAdvice(MusicRequest musicRequest) {
        switch (musicRequest) {
            case New:
                writeHeader("NEW RELEASES");
                write(getNewReleases());
                break;
            case Featured:
                writeHeader("FEATURED");
                write(getFeatured());
                break;
            case Categories:
                writeHeader("CATEGORIES");
                write(getCategories());
                break;
            case Playlists:
                Category category = musicRequest.getCategory();
                writeHeader("PLAYLISTS", category);
                write(getPlaylists(category));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + musicRequest);
        }
    }

    //region MusicRepository

    public static Album[] getNewReleases() {
        return MusicRepository.getNewReleases();
    }

    public static Playlist[] getFeatured() {
        return MusicRepository.getFeatured();
    }

    public static Category[] getCategories() {
        return MusicRepository.getCategories();
    }

    public static Playlist[] getPlaylists(Category category) {
        return MusicRepository.getPlaylists(category).playlists;
    }

    //endregion

    //region WRITE

    private void writeHeader(String header) {
        writeFunc.accept("---" + header + "---");
    }

    private void writeHeader(String header, Category category) {
        String categoryHeader = category.toString().toUpperCase();
        writeHeader(categoryHeader + " " + header);
    }

    private void write(Object[] array) {
        for (Object item : array) {
            writeFunc.accept(item.toString());
        }
    }

    //endregion
}
