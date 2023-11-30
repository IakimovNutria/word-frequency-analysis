package com.example.wordfrequencyanalysis.service;

import com.example.wordfrequencyanalysis.config.BotConfig;
import com.example.wordfrequencyanalysis.models.AppUser;
import com.example.wordfrequencyanalysis.models.AppGroup;
import com.example.wordfrequencyanalysis.models.Word;
import com.example.wordfrequencyanalysis.repositories.GroupRepository;
import com.example.wordfrequencyanalysis.repositories.UserRepository;
import com.example.wordfrequencyanalysis.repositories.WordRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
                    sendStatistic(true, chatId);
                } else {
                    sendMessage(chatId, "Я реагирую только на команду /getStatistic");
                }
            } else {
                if (messageText.equals("/getStatistic")) {
                    sendStatistic(false, chatId);
                } else {
                    addWords(userId, chatId, messageText.split(" "));
                }
            }
        }
    }

    private AppUser findUserById(long id) {
        AppUser appUser = userRepository.findUserById(id);
        if (appUser == null) {
            AppUser newAppUser = new AppUser();
            newAppUser.setId(id);
            newAppUser.setWords(new ArrayList<>());
            return userRepository.save(newAppUser);
        }
        return appUser;
    }

    private AppGroup findGroupById(long id) {
        AppGroup appGroup = groupRepository.findGroupById(id);
        if (appGroup == null) {
            AppGroup newAppGroup = new AppGroup();
            newAppGroup.setId(id);
            newAppGroup.setWords(new ArrayList<>());
            return groupRepository.save(newAppGroup);
        }
        return appGroup;
    }

    private Word findWordById(long id) {
        return wordRepository.findWordById(id);
    }

    private void addWords(long userId, long groupId, String[] words) {
        sendMessage(groupId, "слушаю");
        AppUser appUser = findUserById(userId);
        AppGroup appGroup = findGroupById(groupId);
        for (String word : words) {
            word = formatWord(word);
            boolean isWordFound = false;
            for (Word userWord : appUser.getWords()) {
                if (userWord.getWord().equals(word)) {
                    Word wordModel = findWordById(userWord.getId());
                    wordModel.setCount(wordModel.getCount() + 1);
                    wordRepository.save(wordModel);
                    isWordFound = true;
                }
            }

            if (!isWordFound) {
                Word wordToAdd = new Word();
                wordToAdd.setWord(word);
                wordToAdd.setCount(1);
                wordRepository.save(wordToAdd);
                List<Word> newUserWords = appUser.getWords();
                newUserWords.add(wordToAdd);
                appUser.setWords(newUserWords);
                userRepository.save(appUser);
            }

            isWordFound = false;

            for (Word groupWord : appGroup.getWords()) {
                if (groupWord.getWord().equals(word)) {
                    Word wordModel = findWordById(groupWord.getId());
                    wordModel.setCount(wordModel.getCount() + 1);
                    wordRepository.save(wordModel);
                    isWordFound = true;
                }
            }

            if (!isWordFound) {
                Word wordToAdd = new Word();
                wordToAdd.setWord(word);
                wordToAdd.setCount(1);
                wordRepository.save(wordToAdd);
                List<Word> newGroupWords = appGroup.getWords();
                newGroupWords.add(wordToAdd);
                appGroup.setWords(newGroupWords);
                groupRepository.save(appGroup);
            }
        }
    }

    private void sendStatistic(boolean isUser, long chatId) {
        List<Word> words = isUser ? userRepository.findUserById(chatId).getWords() : groupRepository.findGroupById(chatId).getWords();
        String message = words
                .stream()
                .sorted((word1, word2) -> word2.getCount() - word1.getCount())
                .map(word -> word.getWord() + ' ' + word.getCount())
                .toList()
                .toString()
                .replaceAll(", ", "\n");
        sendMessage(chatId, message.substring(1, message.length() - 1));

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

    private static String removeExtraHyphens(String input) {
        Pattern pattern = Pattern.compile("(\\s|^)-|-(?=\\s|$)");
        Matcher matcher = pattern.matcher(input);
        StringBuilder buffer = new StringBuilder(input.length());
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "");
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }


    private String formatWord(String word) {
        return removeExtraHyphens(word
                .toLowerCase()
                .replaceAll("\\d", "")
                .replaceAll("[^a-zA-Zа-яА-Я\\s-]", "")
        );
    }
}