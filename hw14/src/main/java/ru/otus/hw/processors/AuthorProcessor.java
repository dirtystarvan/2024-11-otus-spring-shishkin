package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.mongo.MongoAuthor;

@Service
public class AuthorProcessor implements ItemProcessor<JpaAuthor, MongoAuthor> {

    @Override
    public MongoAuthor process(JpaAuthor jpaAuthor) {
        var authorFullName = jpaAuthor.getFullName();

        return new MongoAuthor(authorFullName);
    }

}
