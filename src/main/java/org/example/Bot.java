package org.example;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Bot extends TelegramLongPollingBot {

    Properties conf = PropertiesLoader.loadProperties();

    private Map<Long, Integer> userData;

    public Bot() throws IOException {
        userData = new HashMap<>();
    }

    @Override
    public String getBotUsername() {
        return conf.getProperty("BotUsername");
    }

    @Override
    public String getBotToken() {
        return conf.getProperty("BotToken");
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        String text = message.getText();
        log.info(text);
        long userId = message.getFrom().getId();

        if (text.equals("/start")) {
            sendText(userId, "Привет! Это тест навыков Java, начинаем :");
            userData.put(userId, 1);
            String question = getQuestion(1);
            sendText(userId, question);
        } else {
            boolean result = checkAnswer(userData.get(userId), text);
            if (result) {
                sendText(userId, "Верно!");
                userData.put(userId, userData.getOrDefault(userId, 0)+1);
                sendText(userId, getQuestion(userData.get(userId)));
            } else {
                sendText(userId, "Неверно :(");
            }
        }

    }

    public String getQuestion(int number) {

        switch (number) {
            case 1:
                return "Вопрос 1. Сколько в языке Java есть примитивов?";
            case 2:
                return "Вопрос 2. Сколько в реляционных базах данных существует типов связей между таблицами?";
            case 3:
                return "Вопрос 3. С помощью какой команды в системе контроля версий Git можно просмотреть " +
                        "авторов различных строк в одном файле?";
            case 4:
                return "Вопрос 4. Какие методы HTTP-запросов вы занете?";
            default:
                return "Вы верно ответили на все вопросы!";
//                throw new IllegalStateException("Unexpected value: " + number);
        }

    }

    public boolean checkAnswer(int number, String answer) {
        answer = answer.toLowerCase();

        switch (number) {
            case 1:
                return answer.equals("8");
            case 2:
                return answer.equals("3");
            case 3:
                return answer.contains("blame");
            case 4:
                return answer.contains("get") && answer.contains("post") && answer.contains("put")
                        && answer.contains("patch") && answer.contains("delete");
            default:
                throw new IllegalStateException("Unexpected value: " + number);
        }
    }
}
