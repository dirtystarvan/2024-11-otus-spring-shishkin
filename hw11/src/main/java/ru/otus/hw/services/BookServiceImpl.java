package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Mono<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Mono<Book> insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Override
    @Transactional
    public Mono<Book> update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id);
    }

    private Mono<Book> save(String id, String title, String authorId, String genreId) {
        return authorRepository.findById(authorId)
                .flatMap(author ->
                        genreRepository.findById(genreId)
                                .flatMap(genre ->
                                        bookRepository.save(new Book(id, title, author, genre))
                                )
                );
    }
}
