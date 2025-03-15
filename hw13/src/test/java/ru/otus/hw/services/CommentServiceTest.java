package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void findByIdTest(Long id) {
        var comment = commentService.findById(id);

        assertThat(comment).isPresent();
        assertThat(comment.get().getBook()).isNotNull();
    }

    @Test
    void findByBookIdTest() {
        var comments = commentService.findByBookId(2L);

        assertThat(comments).containsExactlyElementsOf(getDbComments());
    }

    @Test
    void findNoCommentsByBookIdTest() {
        var comments = commentService.findByBookId(3L);

        assertThat(comments).isEmpty();
    }

    @Test
    void insertNewCommentTest() {
        var commentText = "Comment_100";
        var actualComment = commentService.insert(commentText, 3L);

        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getId()).isEqualTo(4L);
        assertThat(actualComment.getText()).isEqualTo(commentText);
    }

    @Test
    void updateCommentTest() {
        var text = "Comment300";
        var actualComment = commentService.update(3L, text, 3L);

        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getId()).isEqualTo(3L);
        assertThat(actualComment.getText()).isEqualTo(text);
        assertThat(actualComment.getBook().getId()).isEqualTo(3L);
    }

    @Test
    void deleteCommentTest() {
        var actualComment = commentService.findById(3L);
        assertThat(actualComment).isPresent();

        commentService.delete(3L);

        actualComment = commentService.findById(3L);
        assertThat(actualComment).isEmpty();
    }

    private List<Comment> getDbComments() {
        return List.of(new Comment(2L, "Comment_2", new Book(2L)),
                new Comment(3L, "Comment_3", new Book(2L)));
    }

}
