package io.proj3ct.tegridycoffeebot.service.menu;

public enum Additives {
    CARAMEL ("Caramel"),
    ALMOND ("Almond"),
    VANILLA ("Vanilla"),
    NOTHING("Nothing");

    private String name;
    Additives(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
