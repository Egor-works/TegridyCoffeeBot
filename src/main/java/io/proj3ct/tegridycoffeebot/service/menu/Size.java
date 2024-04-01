package io.proj3ct.tegridycoffeebot.service.menu;

public enum Size {
    //TWOHUNDRED ("200 ml"),
    THREEHUNDRED ("300 ml");

    private String value;

    Size(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
