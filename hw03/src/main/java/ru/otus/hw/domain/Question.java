package ru.otus.hw.domain;

import java.util.List;
import java.util.stream.IntStream;

public record Question(String text, List<Answer> answers) {
    public String toTestString() {
        StringBuilder sb = new StringBuilder();

        sb.append(text);
        sb.append("\n-----\n");

        if (answers != null) {
            IntStream.range(0, answers.size())
                    .mapToObj(idx -> (idx + 1) + ". " + answers.get(idx).toTestString())
                    .forEach(sb::append);
        }

        return sb.toString();
    }
}
