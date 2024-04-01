package io.proj3ct.tegridycoffeebot.service.menu;

public enum Coffee {
    AMERICANO("Americano"),
    LATTE ("Latte"),
    FLATWHITE ("Flat White"),
    CAPPUCCINO ("Cappuccino");

    private String name;

    Coffee(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return name;
    }

}
