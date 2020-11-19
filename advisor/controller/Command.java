package advisor.controller;

import advisor.model.Category;
import advisor.model.MusicRequest;

public class Command {
    Category category;
    final MusicRequest musicRequest;

    Command(MusicRequest musicRequest) {
        this.musicRequest = musicRequest;
    }

    Command(MusicRequest musicRequest, Category category) {
        this(musicRequest);
        this.category = category;
    }
}
