package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {
    private final NamedParameterJdbcOperations jdbc;

    private final AuthorRowMapper rowMapper = new AuthorRowMapper();

    @Override
    public List<Author> findAll() {
        var sql = "select id, full_name from authors";
        return jdbc.query(sql, rowMapper);
    }

    @Override
    public Optional<Author> findById(long id) {
        Optional<Author> author = Optional.empty();
        var queryParams = Map.of("authorId", id);
        var sql = """
                select id, full_name
                from authors
                where id = :authorId
                """;

        try {
            author = Optional.ofNullable(jdbc.queryForObject(sql, queryParams, rowMapper));
        } catch (DataAccessException e) {
            log.error("Error while getting author by id: {}", e.getMessage());
        }

        return author;
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {

            var id = rs.getLong("id");
            var fullName = rs.getString("full_name");

            return new Author(id, fullName);
        }
    }
}
