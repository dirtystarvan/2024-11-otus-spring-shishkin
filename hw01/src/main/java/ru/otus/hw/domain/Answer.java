package ru.otus.hw.domain;

public record Answer(String text, boolean isCorrect) {
    public String toTestString() {
        return text + "\n";
    }
}
