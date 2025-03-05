package ru.otus.hw.mongo.mongock.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface BlockingCommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBookId(String id);

    void deleteByBookId(String bookId);
}
