package advisor.model;

public enum Category {
    TopLists,
    Pop,
    Mood,
    Latin;

    @Override
    public String toString() {
        return this.name().replaceAll("(.)([A-Z])", "$1 $2");
    }
}
