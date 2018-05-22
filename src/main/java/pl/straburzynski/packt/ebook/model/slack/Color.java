package pl.straburzynski.packt.ebook.model.slack;

public enum Color {

    BLUE("#007bff"),
    ORANGE("#f36e28");

    private String name;

    Color(String name) {
        this.name = name;
    }

    public static String getEnumByString(String name) {
        for (Color color : Color.values()) {
            if (name.equals(color.name)) return name;
        }
        return null;
    }

    public String toString() {
        return name;
    }

}