package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.mongo.MongoBook;

@Service
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<JpaBook, MongoBook> {

    private final AuthorProcessor authorProcessor;

    private final GenreProcessor genreProcessor;

    @Override
    public MongoBook process(JpaBook jpaBook) {

        var bookTitle = jpaBook.getTitle();
        var jpaAuthor = jpaBook.getAuthor();
        var jpaGenre = jpaBook.getGenre();

        var mongoAuthor = authorProcessor.process(jpaAuthor);
        var mongoGenre = genreProcessor.process(jpaGenre);

        return new MongoBook(bookTitle, mongoAuthor, mongoGenre);
    }

}
