package io.proj3ct.tegridycoffeebot.botapi.handlers.callbackquery;

import io.proj3ct.tegridycoffeebot.cache.OrderDataCache;
import io.proj3ct.tegridycoffeebot.model.Order;
import io.proj3ct.tegridycoffeebot.model.OrderRepository;
import io.proj3ct.tegridycoffeebot.service.TelegramBot;
import io.proj3ct.tegridycoffeebot.service.messages.ReplyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class MakeAnOrderQueryHandler implements CallbackQueryHandler{
    private static final CallbackQueryType HANDLER_QUERY_TYPE = CallbackQueryType.MAKE_AN_ORDER;
    private OrderRepository orderRepository;
    private ReplyMessageService replyMessageService;
    private String callbackData;
    private TelegramBot telegramBot;
    private OrderDataCache orderDataCache;


    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        return null;
    }

    @Override
    public CallbackQueryType getHandlerQueryType() {
        return null;
    }
}
