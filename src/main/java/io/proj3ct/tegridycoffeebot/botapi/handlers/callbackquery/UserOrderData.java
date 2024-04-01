package io.proj3ct.tegridycoffeebot.botapi.handlers.callbackquery;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserOrderData {

    Long chatId;
    String userName;
    String coffeeName;
    String coffeeSize;
    String coffeeMilk;
    String coffeeAdditives;
    String orderType;
    Short blockNumber;

}
