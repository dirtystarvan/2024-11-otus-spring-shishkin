package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final Map<Long, List<Comment>> dbComments = Map.of(
            1L, List.of(new Comment(1L, "Comment_1", null)),
            2L, List.of(new Comment(2L, "Comment_2", null), new Comment(3L, "Comment_3", null))
    );

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void testFindCommentById(Long id) {
        var actualComment = commentRepository.findById(id).get();
        var expectedComment = testEntityManager.find(Comment.class, id);

        assertThat(expectedComment).isEqualTo(actualComment);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void testFindCommentByBookId(Long id) {
        var actualComments = commentRepository.findByBookId(id);

        assertThat(actualComments).containsExactlyElementsOf(dbComments.get(id));
    }

    @Test
    void testInsertComment() {
        var expectedComment = new Comment(0, "Comment_11", new Book(1L, "BookTitle_1", null, null));

        var actualComment = commentRepository.save(expectedComment);

        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(testEntityManager.find(Comment.class, actualComment.getId())).isEqualTo(actualComment);
    }

    @Test
    void testUpdateComment() {
        var expectedComment = new Comment(1L, "Comment_11", new Book(1L, "BookTitle_1", null, null));

        assertThat(testEntityManager.find(Comment.class, expectedComment.getId())).isNotEqualTo(expectedComment);

        var actualComment = commentRepository.save(expectedComment);

        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(testEntityManager.find(Comment.class, actualComment.getId())).isEqualTo(actualComment);
    }

    @Test
    void testDeleteCommentById() {
        assertThat(testEntityManager.find(Comment.class, 1L)).isNotNull();
        commentRepository.deleteById(1L);
        assertThat(testEntityManager.find(Comment.class, 1L)).isNull();
    }

}