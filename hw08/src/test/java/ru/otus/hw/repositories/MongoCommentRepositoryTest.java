package ru.otus.hw.repositories;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@EnableMongock
@DataMongoTest
class MongoCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void testFindCommentById() {
        var expectedComments = mongoTemplate.findAll(Comment.class);

        for (var comment : expectedComments) {
            var actualComment = commentRepository.findById(comment.getId());
            assertThat(actualComment).isPresent();
            assertThat(actualComment.get()).isEqualTo(comment);
        }
    }

    @Test
    void testFindCommentByBookId() {
        var expectedComment = persistTestComment();
        var actualComments = commentRepository.findByBookId(expectedComment.getBook().getId());

        assertThat(actualComments).isNotEmpty();
        assertThat(actualComments).containsExactly(expectedComment);
    }

    @Test
    void testInsertComment() {
        var persistedBook = persistTestBook();
        var expectedComment = new Comment("testComment", persistedBook);
        var actualComment = commentRepository.insert(expectedComment);

        assertThat(actualComment).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(mongoTemplate.findById(actualComment.getId(), Comment.class)).isEqualTo(actualComment);
    }

    @Test
    void testUpdateComment() {
        var persistedBook = persistTestBook();

        var expectedComment = new Comment("testComment", persistedBook);
        var actualComment = commentRepository.insert(expectedComment);

        var testUpdTitle = "updated";
        actualComment.setText(testUpdTitle);
        commentRepository.save(actualComment);

        assertThat(mongoTemplate.findById(actualComment.getId(), Comment.class)).isNotNull()
                .matches(comment -> testUpdTitle.equals(comment.getText()));
    }

    @Test
    void testDeleteCommentById() {
        var testComment = persistTestComment();

        assertThat(mongoTemplate.findById(testComment.getId(), Comment.class)).isNotNull();
        commentRepository.deleteById(testComment.getId());
        assertThat(mongoTemplate.findById(testComment.getId(), Comment.class)).isNull();
    }

    private Book persistTestBook() {
        var testGenre = new Genre("testGenre");
        var persistedGenre = mongoTemplate.save(testGenre);

        var testAuthor = new Author("testAuthor");
        var persistedAuthor = mongoTemplate.save(testAuthor);

        var testBook = new Book("testBook", persistedAuthor, persistedGenre);

        return mongoTemplate.save(testBook);
    }

    private Comment persistTestComment() {
        var persistedBook = persistTestBook();
        var testComment = new Comment("testComment", persistedBook);

        return mongoTemplate.save(testComment);
    }

}
