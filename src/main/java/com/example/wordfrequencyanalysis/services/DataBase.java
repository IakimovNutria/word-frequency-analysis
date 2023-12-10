package com.example.wordfrequencyanalysis.services;

import com.example.wordfrequencyanalysis.models.AppGroup;
import com.example.wordfrequencyanalysis.models.AppUser;
import com.example.wordfrequencyanalysis.models.Word;
import com.example.wordfrequencyanalysis.repositories.GroupRepository;
import com.example.wordfrequencyanalysis.repositories.UserRepository;
import com.example.wordfrequencyanalysis.repositories.WordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.wordfrequencyanalysis.services.Words.formatWord;

@Service
public class DataBase {
    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final GroupRepository groupRepository;

    public DataBase(UserRepository userRepository, GroupRepository groupRepository, WordRepository wordRepository) {
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
        this.groupRepository = groupRepository;
    }


    public AppUser findUserById(long id) {
        AppUser appUser = userRepository.findUserById(id);
        if (appUser == null) {
            AppUser newAppUser = new AppUser();
            newAppUser.setId(id);
            newAppUser.setWords(new ArrayList<>());
            return userRepository.save(newAppUser);
        }
        return appUser;
    }

    public AppGroup findGroupById(long id) {
        AppGroup appGroup = groupRepository.findGroupById(id);
        if (appGroup == null) {
            AppGroup newAppGroup = new AppGroup();
            newAppGroup.setId(id);
            newAppGroup.setWords(new ArrayList<>());
            return groupRepository.save(newAppGroup);
        }
        return appGroup;
    }

    public Word findWordById(long id) {
        return wordRepository.findWordById(id);
    }

    public void addWords(long userId, long groupId, String[] words) {
        for (String word : words) {
            AppUser appUser = findUserById(userId);
            AppGroup appGroup = findGroupById(groupId);
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
}
