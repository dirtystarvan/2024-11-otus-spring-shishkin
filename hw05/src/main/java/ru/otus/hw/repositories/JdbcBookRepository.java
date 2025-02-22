package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcOperations jdbc;

    private final BookRowMapper rowMapper = new BookRowMapper();

    @Override
    public Optional<Book> findById(long id) {
        Optional<Book> book = Optional.empty();
        var queryParams = Map.of("bookId", id);
        var sql = """
                select
                    books.id, books.title, authors.id, authors.full_name, genres.id, genres.name
                from
                    books left join authors on books.author_id = authors.id
                          left join genres on books.genre_id = genres.id
                where books.id = :bookId
                """;

        try {
            book = Optional.ofNullable(jdbc.queryForObject(sql, queryParams, rowMapper));
        } catch (DataAccessException e) {
            log.error("Error while getting book by id: {}", e.getMessage());
        }

        return book;
    }

    @Override
    public List<Book> findAll() {
        var sql = """
                select
                    books.id, books.title, authors.id, authors.full_name, genres.id, genres.name
                from
                    books left join authors on books.author_id = authors.id
                          left join genres on books.genre_id = genres.id
                """;

        return jdbc.query(sql, rowMapper);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var queryParams = Map.of("bookId", id);
        var sql = """
                delete from books where id = :bookId
                """;

        jdbc.update(sql, queryParams);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var queryParams = Map.of(
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId(),
                "genreId", book.getGenre().getId()
        );
        var paramSource = new MapSqlParameterSource(queryParams);
        var sql = """
                insert into books (title, author_id, genre_id) values (:title, :authorId, :genreId)
                """;

        jdbc.update(sql, paramSource, keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var queryParams = Map.of(
                "bookId", book.getId(),
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId(),
                "genreId", book.getGenre().getId()
        );
        var sql = """
                update books
                set
                    title = :title, author_id = :authorId, genre_id = :genreId
                where id = :bookId
                """;
        var affected = jdbc.update(sql, queryParams);

        if (affected == 0) {
            throw new EntityNotFoundException("Can't find Book entity with id " + book.getId());
        }

        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var bookId = rs.getLong("books.id");
            var bookTitle = rs.getString("books.title");

            var authorId = rs.getLong("authors.id");
            var authorName = rs.getString("authors.full_name");
            var author = new Author(authorId, authorName);

            var genreId = rs.getLong("genres.id");
            var genreName = rs.getString("genres.name");
            var genre = new Genre(genreId, genreName);

            return new Book(bookId, bookTitle, author, genre);
        }
    }
}
