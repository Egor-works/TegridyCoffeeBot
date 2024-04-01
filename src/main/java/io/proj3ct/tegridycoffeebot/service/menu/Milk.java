package io.proj3ct.tegridycoffeebot.service.menu;


public enum Milk {
    COMMON ("Common"),
    BANANA("Banana"),
    ALMOND("Almond"),
    COCONUT("Coconut");

    private String name;

    Milk(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
