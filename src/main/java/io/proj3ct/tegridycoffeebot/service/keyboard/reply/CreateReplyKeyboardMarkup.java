package io.proj3ct.tegridycoffeebot.service.keyboard.reply;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class CreateReplyKeyboardMarkup extends ReplyKeyboardMarkup{
    public ReplyKeyboardMarkup createReplyKeyboardMarkup(List<List<String>> values){
        ReplyKeyboardMarkup result = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (List<String> val : values){
            for (String str : val) {
                row.add(str);
            }
            keyboardRows.add(row);
            row = new KeyboardRow();
        }
        result.setKeyboard(keyboardRows);
        return result;
    }
}
