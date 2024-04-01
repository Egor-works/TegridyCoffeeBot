package io.proj3ct.tegridycoffeebot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "ordersDataTable")
public class Order {

    @Id
    @Column(name = "id", nullable = false)
    private long id;

    private Long chatId;

    private String userName;

    private String coffeeName;

    private String coffeeSize;

    private String coffeeMilk;

    private String coffeeAdditives;

    private String orderType;

    private Short blockNumber;

    @Override
    public String toString() {
        return "Order №" +
                 id +
                " : " + chatId +
                " / " + userName +
                " / " + coffeeName +
                " / " + coffeeSize +
                " / Milk <" + coffeeMilk +
                "> / Add <" + coffeeAdditives +
                "> / " + orderType +
                " / " + blockNumber;
    }
}
