package io.proj3ct.tegridycoffeebot.cache;

import io.proj3ct.tegridycoffeebot.botapi.BotState;
import io.proj3ct.tegridycoffeebot.botapi.handlers.callbackquery.UserOrderData;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserOrderData getUserOrderData(int userId);

    void saveUserOrderData(int userId, UserOrderData userProfileData);
}
