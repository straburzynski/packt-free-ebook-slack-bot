package pl.straburzynski.packt.ebook.model.slack;

public enum Type {

    BUTTON("button"),
    SELECT("select");

    private String name;

    Type(String name) {
        this.name = name;
    }

    public static String getEnumByString(String name) {
        for (Type type : Type.values()) {
            if (name.equals(type.name)) return name;
        }
        return null;
    }

    public String toString() {
        return name;
    }

}