package ru.otus.hw.services;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    @Retry(name = "dbRetry")
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    @Retry(name = "dbRetry")
    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }
}
