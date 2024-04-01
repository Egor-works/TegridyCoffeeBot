package io.proj3ct.tegridycoffeebot.service;

import com.vdurmont.emoji.EmojiParser;
import io.proj3ct.tegridycoffeebot.config.BotConfig;
import io.proj3ct.tegridycoffeebot.model.Order;
import io.proj3ct.tegridycoffeebot.model.OrderRepository;
import io.proj3ct.tegridycoffeebot.model.User;
import io.proj3ct.tegridycoffeebot.model.UserRepository;
import io.proj3ct.tegridycoffeebot.service.menu.Additives;
import io.proj3ct.tegridycoffeebot.service.menu.Coffee;
import io.proj3ct.tegridycoffeebot.service.menu.Milk;
import io.proj3ct.tegridycoffeebot.service.menu.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    final BotConfig config;
    private Order order = new Order();

    private boolean blockFlag = false;
    static final String HELP_TEXT = "This bot is created to keep records of orders. \n\n" +
            "You can execute commands from the main  menu on the left or by typing a command:\n\n" +
            "Type /start to see a welcome message\n" +
            "Type Menu to view the menu\n" +
            "Type Order to open the order panel\n" +
            "Type About us to view cafe info";
    static final String MENU = "Coffee / price:\n" +
            "\n" +
            "Cappuccino / 100 rubles\n" +
            "Flat White / 100 rubles\n" +
            "Latte / 100 rubles\n" +
            "American / 100 rubles\n" +
            "Hot chocolate (coming soon)\n" +
            "\n" +
            "Additives / price:\n" +
            "\n" +
            "Vanilla / 20 rubles\n" +
            "Caramel / 20 rubles\n" +
            "Almonds / 20 rubles\n" +
            "Marshmallow (coming soon)\n" +
            "\n" +
            "Alternative milk / price:\n" +
            "(In Cappuccino and Flat White)\n" +
            "\n" +
            "Almond / 40 rubles\n" +
            "Banana / 40 rubles\n" +
            "Coconut / 40 rubles";
    static final String ABOUT_US = "My name is Dima. And I love making coffee. So, I decided to cook it for you.\n" +
            "\n" +
            "The most common questions:\n" +
            "\n" +
            "- When can I order coffee ?\n" +
            "MON, TUE, THU from 8:00 to 10:00 and from 20:00 to 22:00\n" +
            "WED, THU from 8:00 to 10:00\n" +
            "SUN from 10:00 to 12:00 and from 14:00 to 22:00\n" +
            "\n" +
            "- Do you deliver coffee?\n" +
            "Yes, the delivery is free. You can specify the address at the end of the order.\n" +
            "\n" +
            "- How much time do you spend on ordering?\n" +
            "With delivery, it takes 10 minutes.\n" +
            "\n" +
            "- Where can I pick up my order ?\n" +
            "I issue orders from the window of the 111 block kitchen (next to the laundress, there is a sticker on the window \"coffee with you\").\n" +
            "\n" +
            "- What goes with a cup of coffee?\n" +
            "A wooden stick and two bags of sugar.\n" +
            "\n" +
            "- What syrups do you add ?\n" +
            "Syrups of the company \"Spoom\".\n" +
            "\n" +
            "- What kind of milk do you make coffee with ?\n" +
            "Ekoniva milk with a fat content of 3.2% and various brands of vegetable milk.\n" +
            "\n" +
            "- What kind of coffee do you use in cooking?\n" +
            "90% Arabica and 10% Robusta.";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("Menu", "viewing the menu"));
        listOfCommands.add(new BotCommand("Order", "make an order"));
        listOfCommands.add(new BotCommand("Feedback", "write a review"));
        listOfCommands.add(new BotCommand("AboutUs", "viewing information about the cafe"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error("Error setting bot's command list:\t" + e.getMessage());
        }

    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String massageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (massageText) {
                case "/start":
                    startCommandReceived(update.getMessage());
                    break;
                case "/help":
                case "Help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "Order":
                    if(chatId == 866915193 || chatId == 872544203) {
                        firstMakeOrder(update.getMessage(), new ArrayList<>(List.of(List.of("Make an order"), List.of("View orders"), List.of("View all orders"), List.of("Cancel the order"), List.of("Close the order"))));
                    }
                    else
                        firstMakeOrder(update.getMessage(), new ArrayList<>(List.of(List.of("Make an order"), List.of("View orders"), List.of("Cancel the order"))));
                    break;
                case "Menu":
                    sendMessage(chatId, MENU);
                    break;
                case "About Us":
                    sendMessage(chatId, ABOUT_US);
                    break;
                default:
                    if (blockFlag && massageText.matches("\\d{3}")) {
                        order.setBlockNumber(Short.parseShort(massageText));
                        sendMessage(872544203, "You have new delivery order from @" + order.getUserName());
                        orderRepository.save(order);
                        blockFlag = false;
                        StringBuilder viewO = new StringBuilder();
                        viewBuilder(viewO, order);
                        sendMessage(chatId, "Your order is accepted, wait\nNumber of order: " + viewO);
                    }
                    else sendMessage(chatId,"Excuse me, I only understand push-button input");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();


            switch (callbackData) {
                case "MAKE_AN_ORDER" :
                    order.setChatId(chatId);
                    order.setUserName(userRepository.findById(chatId).get().getUserName());
                    sendEditMessage(chatId, messageId, "What kind of coffee would you like?", new ArrayList<>(List.of(List.of(Coffee.AMERICANO.getName()), List.of(Coffee.LATTE.getName()), List.of(Coffee.FLATWHITE.getName()), List.of(Coffee.CAPPUCCINO.getName()))));
                    break;

                case "VIEW_ORDERS" :
                   viewOrderStatus(chatId, messageId);
                   break;

                case "VIEW_ALL_ORDERS" :
                    viewAllOrderStatus(chatId, messageId);
                    break;

                case "CANCEL_THE_ORDER" :
                    cancelTheOrder(chatId, messageId);
                    break;

                case "CLOSE_THE_ORDER" :
                    closeTheOrder(chatId, messageId);
                    break;

                case "AMERICANO" :
                    order.setCoffeeName(Coffee.AMERICANO.getName());
                    order.setCoffeeMilk("");
                    secondMakeOrder(chatId, messageId, "Choose the size", new ArrayList<>(List.of(List.of(Size.THREEHUNDRED.getValue()))));
                    break;

                case "LATTE" :
                    order.setCoffeeName(Coffee.LATTE.getName());
                    order.setCoffeeMilk("");
                    secondMakeOrder(chatId, messageId, "Choose the size", new ArrayList<>(List.of(List.of(Size.THREEHUNDRED.getValue()))));
                    break;

                case "FLAT_WHITE" :
                    order.setCoffeeName(Coffee.FLATWHITE.getName());
                    secondMakeOrder(chatId, messageId, "Choose the milk", new ArrayList<>(List.of(List.of(Milk.COMMON.getName()), List.of(Milk.BANANA.getName()), List.of("Almond milk"), List.of(Milk.COCONUT.getName()))));
                    break;

                case "CAPPUCCINO" :
                    order.setCoffeeName(Coffee.CAPPUCCINO.getName());
                    secondMakeOrder(chatId, messageId, "Choose the milk", new ArrayList<>(List.of(List.of(Milk.COMMON.getName()), List.of(Milk.BANANA.getName()), List.of("Almond milk"), List.of(Milk.COCONUT.getName()))));
                    break;

                case "COMMON" :
                    order.setCoffeeMilk(Milk.COMMON.getName());
                    secondMakeOrder(chatId, messageId, "Choose the size", new ArrayList<>(List.of(List.of(Size.THREEHUNDRED.getValue()))));
                    break;

                case "BANANA" :
                    order.setCoffeeMilk(Milk.BANANA.getName());
                    secondMakeOrder(chatId, messageId, "Choose the size", new ArrayList<>(List.of(List.of(Size.THREEHUNDRED.getValue()))));
                    break;

                case "ALMOND_MILK" :
                    order.setCoffeeMilk(Milk.ALMOND.getName());
                    secondMakeOrder(chatId, messageId, "Choose the size", new ArrayList<>(List.of(List.of(Size.THREEHUNDRED.getValue()))));
                    break;

                case "COCONUT" :
                    order.setCoffeeMilk(Milk.COCONUT.getName());
                    secondMakeOrder(chatId, messageId, "Choose the size", new ArrayList<>(List.of(List.of(Size.THREEHUNDRED.getValue()))));
                    break;

                case "300_ML" :
                    order.setCoffeeSize(Size.THREEHUNDRED.getValue());
                    secondMakeOrder(chatId, messageId, "Coffee additive of your choice (it's not free)", new ArrayList<>(List.of(List.of(Additives.CARAMEL.getName()), List.of(Additives.ALMOND.getName()), List.of(Additives.VANILLA.getName()), List.of(Additives.NOTHING.getName()))));
                    break;

                case "CARAMEL" :
                    order.setCoffeeAdditives(Additives.CARAMEL.getName());
                    secondMakeOrder(chatId, messageId, "How will you pick up your order?", new ArrayList<>(List.of(List.of("Pickup"), List.of("Delivery"))));
                    break;

                case "ALMOND" :
                    order.setCoffeeAdditives(Additives.ALMOND.getName());
                    secondMakeOrder(chatId, messageId, "How will you pick up your order?", new ArrayList<>(List.of(List.of("Pickup"), List.of("Delivery"))));
                    break;

                case "VANILLA" :
                    order.setCoffeeAdditives(Additives.VANILLA.getName());
                    secondMakeOrder(chatId, messageId, "How will you pick up your order?", new ArrayList<>(List.of(List.of("Pickup"), List.of("Delivery"))));
                    break;

                case "NOTHING" :
                    order.setCoffeeAdditives(Additives.NOTHING.getName());
                    secondMakeOrder(chatId, messageId, "How will you pick up your order?", new ArrayList<>(List.of(List.of("Pickup"), List.of("Delivery"))));
                    break;

                case "PICKUP" :
                    order.setOrderType("Pickup");
                    order.setBlockNumber(new Short("0"));
                    sendMessage(872544203, "You have new pickup order from @" + order.getUserName());
                    StringBuilder viewO = new StringBuilder();
                    viewBuilder(viewO, order);
                    finalPickupMakeOrder(chatId, messageId,  "Your order is accepted, wait\norder: " + viewO);

                    break;

                case "DELIVERY":
                    blockFlag = true;
                    order.setOrderType("Delivery");
                    sendEditMessage(chatId,messageId, "Please enter the number of the residential block where you need to deliver the order:");
                    break;
                default:
                    if (callbackData.matches("\\d{4}")) {

                        orderRepository.deleteById(Long.parseLong(callbackData));
                        sendEditMessage(chatId, messageId, String.format("Order %s is cancelled", callbackData));

                        break;
                    }
                    break;
            }
        }
    }

    private void startCommandReceived(Message msg){

        if(userRepository.findById(msg.getChatId()).isEmpty()){
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setUserName(chat.getUserName());
            user.setBanFlag(false);

            userRepository.save(user);

        }
        else if (userRepository.findById(msg.getChatId()).get().getBanFlag()){
            sendMessage(msg.getChatId(),"Go fuck yourself, " + msg.getFrom().getFirstName());
            return;
        }

        String startAnswer = EmojiParser.parseToUnicode("Hello and welcome to the Tegridy Coffee family, " +
                "my name is Dmitrii, but you can call me Dim." + " :open_hands:");

        sendMessage(msg.getChatId(), startAnswer);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(new ArrayList<>(List.of(List.of("Menu", "Order"), List.of("Feedback", "About Us"))));
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e){
            log.error("Send message error:\t" + e.getMessage());
        }
    }
    private void sendMessage(long chatId, String textToSend, List<List<String>> param){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(new ArrayList<>(List.of(List.of("Menu", "Order"), List.of("Feedback", "About Us"))));
        message.setReplyMarkup(replyKeyboardMarkup);
        message.setReplyMarkup(createInlineKeyboardMarkup(param));

        try {
            execute(message);
        } catch (TelegramApiException e){
            log.error("Send message error:\t" + e.getMessage());
        }
    }

    private void sendEditMessage(long chatId, long messageId, String text) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatId));
        messageText.setText(text);
        messageText.setMessageId((int) messageId);

        try {
            execute(messageText);
        } catch (TelegramApiException e){
            log.error("Send message error:\t" + e.getMessage());
        }
    }
    private void sendEditMessage(long chatId, long messageId, String text, List<List<String>> param) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatId));
        messageText.setText(text);
        messageText.setMessageId((int) messageId);
        messageText.setReplyMarkup(createInlineKeyboardMarkup(param));


        try {
            execute(messageText);
        } catch (TelegramApiException e){
            log.error("Send message error:\t" + e.getMessage());
        }
    }

    private void firstMakeOrder(Message msg, List<List<String>> param){

        order = new Order();
        var id = new Random().nextLong() % 10000;
        if (id < 0) id*=-1;
        while (id < 1000) id*=10;
        order.setId(id);

        sendMessage(msg.getChatId(),"Note that the coffee machine cools down for exactly 5 minutes.\n" +
                String.format("There are %d orders in the queue\n\n", orderRepository.count()) +
                "What do you want?", param);
    }

    private void secondMakeOrder(long chatId, long messageId, String text, List<List<String>> param){
        sendEditMessage(chatId, messageId, text, param);

    }

    private void finalPickupMakeOrder(long chatId, long messageId, String text){
        orderRepository.save(order);
        sendEditMessage(chatId, messageId, text);
    }

    private void viewOrderStatus(long chatId, long messageId){
        String text = "Your orders:\n\n";
        StringBuilder view =  new StringBuilder();
        for (Order ord : orderRepository.findAll()){
            if(ord.getChatId() == chatId){
                viewBuilder(view, ord);
            }
        }
        sendEditMessage(chatId, messageId, text + view);
    }
    private void viewAllOrderStatus(long chatId, long messageId){
        String text = "All orders:\n\n";
        StringBuilder view =  new StringBuilder();
        for (Order ord : orderRepository.findAll()){
            view.append(ord).append("\n\n");
        }
        sendEditMessage(chatId, messageId, text + view);
    }

    private void viewBuilder(StringBuilder view, Order order) {
        view.append(order.getId()).append("  /  ").append(order.getUserName()).append("  /  ").append(order.getCoffeeName()).append("  /  ").append(order.getCoffeeSize()).append("  /  ");
        if (!order.getCoffeeMilk().isEmpty())
            view.append(order.getCoffeeMilk()).append("  /  ");
        view.append(order.getCoffeeAdditives()).append("  /  ");
        if (order.getOrderType().equals("Delivery"))
            view.append(order.getBlockNumber()).append("  /  ");
        view.append("\n\n");
    }
    private void cancelTheOrder(long chatId, long messageId){
        String text = "Select the order number you want to cancel";
        List<List<String>> param = new ArrayList<>();
        List<String> orders = new ArrayList<>();
        for (Order ord : orderRepository.findAll()){
            if(ord.getChatId() == chatId){
                orders.add(String.valueOf(ord.getId()));
                param.add(orders);
                orders = new ArrayList<>();
            }
        }
        sendEditMessage(chatId, messageId, text, param);
    }

    private void closeTheOrder(long chatId, long messageId){
        String text = "Select the order number you want to close";
        List<List<String>> param = new ArrayList<>();
        List<String> orders = new ArrayList<>();
        for (Order ord : orderRepository.findAll()){
            orders.add(String.valueOf(ord.getId()));
            param.add(orders);
            orders = new ArrayList<>();
        }
        sendEditMessage(chatId, messageId, text, param);
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(List<List<String>> values){
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

    private InlineKeyboardMarkup createInlineKeyboardMarkup(List<List<String>> values){
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
