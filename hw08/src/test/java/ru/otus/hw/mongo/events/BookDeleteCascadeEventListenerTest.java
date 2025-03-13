package ru.otus.hw.mongo.events;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ComponentScan("ru.otus.hw.mongo.events")
class BookDeleteCascadeEventListenerTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void commentDeleteCascadeByBookDeleteTest() {
        var testComment = persistTestComment();
        assertThat(mongoTemplate.findById(testComment.getId(), Comment.class)).isNotNull();

        mongoTemplate.remove(testComment.getBook());
        assertThat(mongoTemplate.findById(testComment.getId(), Comment.class)).isNull();
    }

    private Comment persistTestComment() {
        var testGenre = new Genre("testGenre");
        var persistedGenre = mongoTemplate.save(testGenre);

        var testAuthor = new Author("testAuthor");
        var persistedAuthor = mongoTemplate.save(testAuthor);

        var testBook = new Book("testBook", persistedAuthor, persistedGenre);
        var persistedBook = mongoTemplate.save(testBook);

        var testComment = new Comment("testComment", persistedBook);

        return mongoTemplate.save(testComment);
    }

}