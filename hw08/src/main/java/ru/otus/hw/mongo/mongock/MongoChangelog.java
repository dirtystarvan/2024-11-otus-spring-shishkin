package ru.otus.hw.mongo.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class MongoChangelog {

    private List<Author> allAuthors;

    private List<Genre> allGenres;

    private List<Book> allBooks;

    @ChangeSet(order = "001", id = "dropDb", author = "ishishkin", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "createAuthors", author = "ishishkin", runAlways = true)
    public void insertAuthors(AuthorRepository authorRepository) {
        allAuthors = authorRepository.saveAll(List.of(
                new Author("Author_1"),
                new Author("Author_2"),
                new Author("Author_3")
        ));
    }

    @ChangeSet(order = "003", id = "createGenres", author = "ishishkin", runAlways = true)
    public void insertGenres(GenreRepository genreRepository) {
        allGenres = genreRepository.saveAll(List.of(
                new Genre("Genre_1"),
                new Genre("Genre_2"),
                new Genre("Genre_3")
        ));
    }

    @ChangeSet(order = "004", id = "createBooks", author = "ishishkin", runAlways = true)
    public void insertBooks(BookRepository bookRepository) {
        allBooks = bookRepository.saveAll(List.of(
                new Book("BookTitle_1", allAuthors.get(0), allGenres.get(0)),
                new Book("BookTitle_2", allAuthors.get(1), allGenres.get(1)),
                new Book("BookTitle_3", allAuthors.get(2), allGenres.get(2))
        ));
    }

    @ChangeSet(order = "005", id = "createComments", author = "ishishkin", runAlways = true)
    public void insertComments(CommentRepository commentRepository) {
        commentRepository.saveAll(List.of(
                new Comment("Comment_1", allBooks.get(0)),
                new Comment("Comment_2", allBooks.get(1)),
                new Comment("Comment_3", allBooks.get(1))
        ));
    }

}
