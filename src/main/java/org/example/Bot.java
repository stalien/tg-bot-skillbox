package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.questions.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Bot extends TelegramLongPollingBot {

    Properties conf = PropertiesLoader.loadProperties();

    private Map<Long, UserData> users;

    private ArrayList<AbstractQuestion> questions;

    public Bot() throws IOException {
        users = new HashMap<>();
        questions = new ArrayList<>();
        questions.add(new Question1());
        questions.add(new Question2());
        questions.add(new Question3());
        questions.add(new Question4());
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
        UserData userData = users.get(userId);

        if (text.equals("/start")) {
            sendText(userId, "Привет! Это тест навыков Java, начинаем :");
            users.put(userId, new UserData());
            String question = questions.get(0).getQuestion();
            sendText(userId, question);
        } else if (userData.getQuestionNumber() < questions.size()) {
            int questionNumber = userData.getQuestionNumber();
            int score = userData.getScore();
            boolean result = questions.get(questionNumber).checkAnswer(text);
            if (userData.getQuestionNumber() == questions.size()-1) {
                if (result) {
                    sendText(userId, "Верно!");
                    userData.setScore(++score);
                }
                sendText(userId, "Вы ответили на все вопросы!" + " У вас " + userData.getScore() + " из "
                        + questions.size() + " очков! ");
                sendText(userId, "Чтобы начать заново, используйте команду /start");
            } else if (result) {
                sendText(userId, "Верно!");
                userData.setScore(++score);
                userData.setQuestionNumber(++questionNumber);
                sendText(userId, questions.get(questionNumber).getQuestion());
            } else {
                sendText(userId, "Неверно :(");
                userData.setQuestionNumber(++questionNumber);
                sendText(userId, questions.get(questionNumber).getQuestion());
            }
        } else {
            sendText(userId, "Вы ответили на все вопросы!" + " У вас " + userData.getScore() + "/4 очков! ");
            sendText(userId, "Чтобы начать заново, используйте команду /start");
        }

    }

}
