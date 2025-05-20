package ru.otus.hw.mongo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("ru.otus.hw.repositories")
@Configuration
public class MongoConfiguration {
}
