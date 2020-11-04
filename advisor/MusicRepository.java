package advisor;

class MusicRepository {

    static Album[] getNewReleases() {
        return new Album[] {
                new Album("Mountains", "Sia", "Diplo", "Labrinth"),
                new Album("Runaway", "Lil Peep"),
                new Album("The Greatest Show", "Panic! At The Disco"),
                new Album("All Out Life", "Slipknot")
        };
    }

    static Playlist[] getFeatured() {
        return new Playlist[] {
                new Playlist("Mellow Morning"),
                new Playlist("Wake Up and Smell the Coffee"),
                new Playlist("Monday Motivation"),
                new Playlist("Songs to Sing in the Shower"),
        };
    }

    static CategoryPlaylists getPlaylists(Category category) {
        switch (category) {
            case Mood:
                return new CategoryPlaylists(category, new Playlist[] {
                        new Playlist("Walk Like A Badass"),
                        new Playlist("Rage Beats"),
                        new Playlist("Arab Mood Booster"),
                        new Playlist("Sunday Stroll"),
                });
            default:
                return new CategoryPlaylists(category, new Playlist[0]);
        }
    }

    static Category[] getCategories() {
        return Category.values();
    }
}
