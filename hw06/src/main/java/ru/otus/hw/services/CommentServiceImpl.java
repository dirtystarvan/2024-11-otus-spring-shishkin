package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;


    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment insert(String title, long bookId) {
        return save(0, title, bookId);
    }

    @Transactional
    @Override
    public Comment update(long id, String title, long bookId) {
        return save(id, title, bookId);
    }

    @Transactional
    @Override
    public void delete(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String title, long bookId) {
        var isBookExists = bookRepository.exists(bookId);

        if (!TRUE.equals(isBookExists)) {
            throw new IllegalArgumentException("Cannot save comment for book with id %d. Book not found"
                    .formatted(bookId));
        }

        return commentRepository.save(new Comment(id, title, new Book(bookId)));
    }
}
