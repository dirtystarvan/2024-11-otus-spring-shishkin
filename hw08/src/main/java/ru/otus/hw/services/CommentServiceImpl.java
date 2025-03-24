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
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment insert(String title, String bookId) {
        return save(null, title, bookId);
    }

    @Transactional
    @Override
    public Comment update(String id, String title, String bookId) {
        return save(id, title, bookId);
    }

    @Transactional
    @Override
    public void delete(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String title, String bookId) {
        var isBookExists = bookRepository.existsById(bookId);

        if (!TRUE.equals(isBookExists)) {
            throw new IllegalArgumentException("Cannot save comment for book with id %s. Book not found"
                    .formatted(bookId));
        }

        return commentRepository.save(new Comment(id, title, new Book(bookId)));
    }
}
