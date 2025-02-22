package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreRowMapper rowMapper = new GenreRowMapper();

    @Override
    public List<Genre> findAll() {
        var sql = "select id, name from genres";
        return jdbc.query(sql, rowMapper);
    }

    @Override
    public Optional<Genre> findById(long id) {
        Optional<Genre> genre = Optional.empty();
        var queryParams = Map.of("genreId", id);
        var sql = """
                select id, name
                from genres
                where id = :genreId
                """;

        try {
            genre = Optional.ofNullable(jdbc.queryForObject(sql, queryParams, rowMapper));
        } catch (DataAccessException e) {
            log.error("Error while getting genre by id: {}", e.getMessage());
        }

        return genre;
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            var id = rs.getLong("id");
            var name = rs.getString("name");

            return new Genre(id, name);
        }
    }
}
