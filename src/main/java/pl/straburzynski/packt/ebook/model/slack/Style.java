package pl.straburzynski.packt.ebook.model.slack;

public enum Style {

    PRIMARY("primary"),
    DEFAULT("default");

    private String name;

    Style(String name) {
        this.name = name;
    }

    public static String getEnumByString(String name) {
        for (Style style : Style.values()) {
            if (name.equals(style.name)) return name;
        }
        return null;
    }

    public String toString() {
        return name;
    }

}