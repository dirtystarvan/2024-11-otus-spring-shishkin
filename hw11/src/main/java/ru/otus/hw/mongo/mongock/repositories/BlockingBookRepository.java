package ru.otus.hw.mongo.mongock.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Book;

public interface BlockingBookRepository extends MongoRepository<Book, String> {
    boolean existsById(String id);
}
