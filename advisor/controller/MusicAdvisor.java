package advisor.controller;

import advisor.SpotifyService;
import advisor.data.MusicRepository;
import advisor.model.Album;
import advisor.model.Category;
import advisor.model.MusicRequest;
import advisor.model.Playlist;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class MusicAdvisor {

    private final Consumer<String> writeFunc;

    MusicAdvisor(Consumer<String> writeFunc) {
        this.writeFunc = writeFunc;
    }

    public void requestAdvice(
            MusicRequest musicRequest, String accessToken, String apiServerPath) {
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

                //write(getCategories());

                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .header("Authorization", "Bearer " + accessToken)
                        .uri(URI.create(apiServerPath + "/v1/browse/categories"))
                        .GET()
                        .build();

                System.out.println("HTTP REQUEST:");
                System.out.println(SpotifyService.send(httpRequest));

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
