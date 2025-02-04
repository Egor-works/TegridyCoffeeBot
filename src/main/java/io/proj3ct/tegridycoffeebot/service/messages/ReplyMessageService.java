package io.proj3ct.tegridycoffeebot.service.messages;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
public class ReplyMessageService {
    private LocaleMessageService localeMessageService;

    public ReplyMessageService(LocaleMessageService messageService) {
        this.localeMessageService = messageService;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(String.valueOf(chatId), localeMessageService.getMessage(replyMessage));
    }
    public EditMessageText getReplyEditMessage(long chatId,long messageId, String replyMessage) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId((int) messageId);
        editMessageText.setText(localeMessageService.getMessage(replyMessage));
        return editMessageText;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage, Object... args) {
        return new SendMessage(String.valueOf(chatId), localeMessageService.getMessage(replyMessage, args));
    }

    public String getReplyText(String replyText) {
        return localeMessageService.getMessage(replyText);
    }

}
