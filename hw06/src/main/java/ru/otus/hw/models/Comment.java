package ru.otus.hw.models;


import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@EqualsAndHashCode(of = {"id", "text"})
@ToString(of = {"id", "text"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
