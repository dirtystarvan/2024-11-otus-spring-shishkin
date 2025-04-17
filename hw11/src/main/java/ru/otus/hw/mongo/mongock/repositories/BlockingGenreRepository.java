package ru.otus.hw.mongo.mongock.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Genre;

public interface BlockingGenreRepository extends MongoRepository<Genre, String> {

}
