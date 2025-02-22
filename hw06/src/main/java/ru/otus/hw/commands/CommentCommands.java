package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with %s not found".formatted(id));
    }

    @ShellMethod(value = "Find all comments by book id", key = "cbbid")
    public String findCommentsByBookId(long bookId) {
        return commentService.findByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String title, long bookId) {
        var savedComment = commentService.insert(title, bookId);

        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, String title, long bookId) {
        var savedComment = commentService.update(id, title, bookId);

        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Delete comment", key = "cdel")
    void deleteComment(long id) {
        commentService.delete(id);
    }
}
