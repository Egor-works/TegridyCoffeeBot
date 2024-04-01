package io.proj3ct.tegridycoffeebot.botapi.handlers.mainmenu;


import io.proj3ct.tegridycoffeebot.botapi.BotState;
import io.proj3ct.tegridycoffeebot.botapi.InputMessageHandler;
import io.proj3ct.tegridycoffeebot.service.messages.MainMenuService;
import io.proj3ct.tegridycoffeebot.service.messages.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpMessageHandler implements InputMessageHandler {
    private MainMenuService mainMenuService;
    private ReplyMessageService messagesService;

    public HelpMessageHandler(MainMenuService mainMenuService, ReplyMessageService messagesService) {
        this.mainMenuService = mainMenuService;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(),
                messagesService.getReplyText("reply.showHelpMessage"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MESSAGE;
    }
}
