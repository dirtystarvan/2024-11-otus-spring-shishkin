package ru.otus.hw.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.models.mongo.MongoGenre;

@Service
public class GenreProcessor implements ItemProcessor<JpaGenre, MongoGenre> {

    @Override
    public MongoGenre process(JpaGenre jpaGenre) {
        var genreName = jpaGenre.getName();

        return new MongoGenre(genreName);
    }

}
