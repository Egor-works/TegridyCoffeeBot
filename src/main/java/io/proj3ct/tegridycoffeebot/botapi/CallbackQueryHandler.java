package io.proj3ct.tegridycoffeebot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CallbackQueryHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}
