package com.example.wordfrequencyanalysis.controllers;

import com.example.wordfrequencyanalysis.config.BotConfig;
import com.example.wordfrequencyanalysis.models.Word;
import com.example.wordfrequencyanalysis.services.DataBase;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.example.wordfrequencyanalysis.services.Words.getStatisticMessage;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    static final String ERROR_TEXT = "Error occurred: ";
    private final DataBase dataBase;



    public TelegramBot(BotConfig config, DataBase dataBase) {
        this.config = config;
        this.dataBase = dataBase;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            long userId = update.getMessage().getFrom().getId();
            if (userId == chatId) {
                if (messageText.equals("/getStatistic")) {
                    sendStatistic(true, chatId);
                } else {
                    sendMessage(chatId, "Я реагирую только на команду /getStatistic");
                }
            } else {
                if (messageText.equals("/getStatistic")) {
                    sendStatistic(false, chatId);
                } else {
                    dataBase.addWords(userId, chatId, messageText.split(" "));
                }
            }
        }
    }

    private void sendStatistic(boolean isUser, long chatId) {
        List<Word> words = isUser ? dataBase.findUserById(chatId).getWords() : dataBase.findGroupById(chatId).getWords();

        sendMessage(chatId, getStatisticMessage(words));
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}