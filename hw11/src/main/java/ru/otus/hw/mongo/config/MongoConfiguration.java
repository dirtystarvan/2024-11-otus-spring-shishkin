package ru.otus.hw.mongo.config;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableMongoRepositories("ru.otus.hw.mongo.mongock.repositories")
@EnableReactiveMongoRepositories("ru.otus.hw.repositories")
@EnableMongock
@Configuration
public class MongoConfiguration {
}
