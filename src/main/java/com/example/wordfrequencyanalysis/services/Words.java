package com.example.wordfrequencyanalysis.services;

import com.example.wordfrequencyanalysis.models.Word;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Words {
    public static String removeExtraHyphens(String input) {
        Pattern pattern = Pattern.compile("(\\s|^)-|-(?=\\s|$)");
        Matcher matcher = pattern.matcher(input);
        StringBuilder buffer = new StringBuilder(input.length());
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "");
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }


    public static String formatWord(String word) {
        return removeExtraHyphens(word
                .toLowerCase()
                .replaceAll("\\d", "")
                .replaceAll("[^a-zA-Zа-яА-Я\\s-]", "")
        );
    }

    public static String getStatisticMessage(List<Word> words) {
        String message = words
                .stream()
                .sorted((word1, word2) -> word2.getCount() - word1.getCount())
                .map(word -> word.getWord() + ' ' + word.getCount())
                .toList()
                .toString()
                .replaceAll(", ", "\n");

        return message.substring(1, message.length() - 1);
    }
}
