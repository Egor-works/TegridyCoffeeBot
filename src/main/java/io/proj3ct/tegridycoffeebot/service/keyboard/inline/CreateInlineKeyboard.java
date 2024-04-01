package io.proj3ct.tegridycoffeebot.service.keyboard.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class CreateInlineKeyboard extends InlineKeyboardMarkup{
    public InlineKeyboardMarkup createInlineKeyboardMarkup(List<List<String>> values){
        InlineKeyboardMarkup result = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();
        List<InlineKeyboardButton> row;
        var button = new InlineKeyboardButton();
        for (List<String> val : values){
            row = new ArrayList<>();
            for (String str : val) {
                if(str.equals("Almond milk")) button.setText("Almond");
                else button.setText(str);
                button.setCallbackData(str.toUpperCase().replaceAll(" ", "_"));
                row.add(button);
                button = new InlineKeyboardButton();
            }
            inlineRows.add(row);
        }
        result.setKeyboard(inlineRows);
        return result;
    }
}
