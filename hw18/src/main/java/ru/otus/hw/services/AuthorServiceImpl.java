package ru.otus.hw.services;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    @Retry(name = "dbRetry")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @Retry(name = "dbRetry")
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }
}
