package ru.otus.hw.mongo.mongock.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Author;

public interface BlockingAuthorRepository extends MongoRepository<Author, String> {

}
