package com.example.wordfrequencyanalysis.service;

import com.example.wordfrequencyanalysis.config.BotConfig;
import com.example.wordfrequencyanalysis.models.Group;
import com.example.wordfrequencyanalysis.models.User;
import com.example.wordfrequencyanalysis.models.Word;
import com.example.wordfrequencyanalysis.repositories.GroupRepository;
import com.example.wordfrequencyanalysis.repositories.UserRepository;
import com.example.wordfrequencyanalysis.repositories.WordRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    static final String ERROR_TEXT = "Error occurred: ";

    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final GroupRepository groupRepository;

    public TelegramBot(BotConfig config, UserRepository userRepository, GroupRepository groupRepository, WordRepository wordRepository) {
        this.config = config;
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
        this.groupRepository = groupRepository;
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
                    
                } else {
                    sendMessage(chatId, "Я реагирую только на команду /getStatistic");
                }
            } else {
                if (messageText.equals("/getStatistic")) {
                    startCommandReceived(chatId, update.getMessage().getFrom().getFirstName());
                } else {
                    for (String word : messageText.split(" ")) {

                    }
                }
            }
        }
    }

    private User findUserById(long id) {
        return userRepository.findUserById(id);
    }

    private Group findGroupById(long id) {
        return groupRepository.findGroupById(id);
    }

    private Word findWordById(long id) {
        return wordRepository.findWordById(id);
    }

    private void addWords(long userId, long groupId, List<String> words) {
        User user = findUserById(userId);
        Group group = findGroupById(groupId);
        List<Word> userWords = user.getWords();
        List<Word> groupWords = group.getWords();
        for (String word : words) {

        }
    }

    private void startCommandReceived(long chatId, String firstName) {
        String answer = "Hi, " + firstName + ", nice to meet you!";
        sendMessage(chatId, answer);
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