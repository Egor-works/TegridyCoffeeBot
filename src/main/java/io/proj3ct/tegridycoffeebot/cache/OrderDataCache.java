package io.proj3ct.tegridycoffeebot.cache;

import io.proj3ct.tegridycoffeebot.botapi.BotState;
import io.proj3ct.tegridycoffeebot.botapi.handlers.callbackquery.UserOrderData;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

@Component
public class OrderDataCache implements DataCache{
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Integer, UserOrderData> usersOrderData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.START_MESSAGE;
        }

        return botState;
    }

    @Override
    public UserOrderData getUserOrderData(int userId) {
        UserOrderData userOrderData = usersOrderData.get(userId);
        if (userOrderData == null) {
            userOrderData = new UserOrderData();
        }
        return userOrderData;
    }

    @Override
    public void saveUserOrderData(int userId, UserOrderData userOrderData) {
        usersOrderData.put(userId, userOrderData);
    }
}
