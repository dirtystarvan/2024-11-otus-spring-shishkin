package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Slf4j
@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        Optional<Book> book = Optional.empty();
        var graph = em.getEntityGraph("book-authors-genres");
        var query = em.createQuery("select b from Book b where b.id = :id", Book.class);

        query.setParameter("id", id);
        query.setHint(FETCH.getKey(), graph);

        try {
            book = Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            log.error("Error while getting book by id: {}", e.getMessage());
        }

        return book;
    }

    @Override
    public List<Book> findAll() {
        var graph = em.getEntityGraph("book-authors-genres");
        var query = em.createQuery("select b from Book b", Book.class);

        query.setHint(FETCH.getKey(), graph);

        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }

        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(book -> em.remove(book));
    }

    @Override
    public boolean exists(long id) {
        var query = em.createQuery("select exists(select 1 from Book b where b.id = :id)", Boolean.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }
}
