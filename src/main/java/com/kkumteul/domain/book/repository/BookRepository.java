package com.kkumteul.domain.book.repository;

import com.kkumteul.domain.book.dto.AdminBookFilterResponseDto;
import com.kkumteul.domain.book.dto.AdminGetBookListResponseDto;
import com.kkumteul.domain.book.entity.Book;
import java.util.Optional;

import com.kkumteul.domain.mbti.entity.MBTIName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // 민아님 코드와 동일

    @Query(value = """
                SELECT b
                  FROM Book b
                 ORDER BY b.id ASC
            """)
    Page<Book> findAllBookInfo(final Pageable pageable);

    @EntityGraph(attributePaths = {"genre", "bookTopics.topic"})
    @Query("SELECT b FROM Book b")
    List<Book> findAllBooksWithTopicsAndGenre();

    @Query("""
        SELECT DISTINCT b
        FROM Book b
        WHERE CAST(SUBSTRING(b.ageGroup, 1, LOCATE('세', b.ageGroup)-1) AS integer) < :age
        ORDER BY :age - CAST(SUBSTRING(b.ageGroup, 1, LOCATE('세', b.ageGroup)-1) AS integer) ASC
    """)
    List<Book> findBookListByAgeGroup(@Param("age") int age, Pageable pageable);

    @Query(value = """
            SELECT b
              FROM Book b
             WHERE b.title LIKE %:keyword%
                OR b.author LIKE %:keyword%
             ORDER BY CASE WHEN b.title LIKE %:keyword% THEN 1 ELSE 2 END
            """)
    Page<Book> searchByTitleOrAuthor(@Param("keyword") String keyword, final Pageable pageable);
// ----------------------------------------------------------------------------------
// 필터링 메소드 정의: genre는 genre 테이블에서 ID를 찾아 book 테이블의 genre_id와 비교, topics는 book_topic 테이블에서, mbti는 bookmbti 테이블에서 필터링합니다.
@Query(value = """
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
        b.id, b.bookImage, b.title, b.author, b.publisher, g.name, b.ageGroup,
        t.name, m.mbti
     )
        FROM Book b
        LEFT JOIN Genre g ON b.genre.id = g.id
        LEFT JOIN BookTopic bt ON bt.book.id = b.id
        LEFT JOIN Topic t ON bt.topic.id = t.id
        LEFT JOIN BookMBTI bm ON bm.book.id = b.id
        LEFT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:genre IS NULL OR :genre = '' OR g.name = :genre)
        AND (:topic IS NULL OR :topic = '' OR t.name = :topic)
        AND (:mbti IS NULL OR :mbti = '' OR m.mbti = :mbti)
        
        UNION 
        
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
            b.id, b.bookImage, b.title, b.author, b.publisher,
            g.name, b.ageGroup, t.name, m.mbti)
        FROM Book b
        RIGHT JOIN Genre g ON b.genre.id = g.id
        RIGHT JOIN BookTopic bt ON bt.book.id = b.id
        RIGHT JOIN Topic t ON bt.topic.id = t.id
        RIGHT JOIN BookMBTI bm ON bm.book.id = b.id
        RIGHT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:genre IS NULL OR :genre = '' OR g.name = :genre)
        AND (:topic IS NULL OR :topic = '' OR t.name = :topic)
        AND (:mbti IS NULL OR :mbti = '' OR m.mbti = :mbti)
        """)
    Page<AdminBookFilterResponseDto> filterBooksGenreTopicMBTI(@Param("genre") String genre, @Param("topic") String topic, @Param("mbti") MBTIName mbti, Pageable pageable);

    // 장르만
    @Query(value = """
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
        b.id, b.bookImage, b.title, b.author, b.publisher, g.name, b.ageGroup,
        t.name, m.mbti
     )
        FROM Book b
        LEFT JOIN Genre g ON b.genre.id = g.id
        LEFT JOIN BookTopic bt ON bt.book.id = b.id
        LEFT JOIN Topic t ON bt.topic.id = t.id
        LEFT JOIN BookMBTI bm ON bm.book.id = b.id
        LEFT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:genre IS NULL OR :genre = '' OR g.name = :genre)

        UNION
        
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
            b.id, b.bookImage, b.title, b.author, b.publisher,
            g.name, b.ageGroup, t.name, m.mbti)
        FROM Book b
        RIGHT JOIN Genre g ON b.genre.id = g.id
        RIGHT JOIN BookTopic bt ON bt.book.id = b.id
        RIGHT JOIN Topic t ON bt.topic.id = t.id
        RIGHT JOIN BookMBTI bm ON bm.book.id = b.id
        RIGHT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:genre IS NULL OR :genre = '' OR g.name = :genre)
        """)
    Page<AdminBookFilterResponseDto> filterBooksGenre(@Param("genre") String genre, Pageable pageable);

    // topic만
    @Query(value = """
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
        b.id, b.bookImage, b.title, b.author, b.publisher, g.name, b.ageGroup,
        t.name, m.mbti
     )
        FROM Book b
        LEFT JOIN Genre g ON b.genre.id = g.id
        LEFT JOIN BookTopic bt ON bt.book.id = b.id
        LEFT JOIN Topic t ON bt.topic.id = t.id
        LEFT JOIN BookMBTI bm ON bm.book.id = b.id
        LEFT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:topic IS NULL OR :topic = '' OR t.name = :topic)

        UNION
        
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
            b.id, b.bookImage, b.title, b.author, b.publisher,
            g.name, b.ageGroup, t.name, m.mbti)
        FROM Book b
        RIGHT JOIN Genre g ON b.genre.id = g.id
        RIGHT JOIN BookTopic bt ON bt.book.id = b.id
        RIGHT JOIN Topic t ON bt.topic.id = t.id
        RIGHT JOIN BookMBTI bm ON bm.book.id = b.id
        RIGHT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:topic IS NULL OR :topic = '' OR t.name = :topic)
        """)
    Page<AdminBookFilterResponseDto> filterBooksTopic(@Param("topic") String topic, Pageable pageable);

    // MBTI만
    @Query(value = """
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
        b.id, b.bookImage, b.title, b.author, b.publisher, g.name, b.ageGroup,
        t.name, m.mbti
     )
        FROM Book b
        LEFT JOIN Genre g ON b.genre.id = g.id
        LEFT JOIN BookTopic bt ON bt.book.id = b.id
        LEFT JOIN Topic t ON bt.topic.id = t.id
        LEFT JOIN BookMBTI bm ON bm.book.id = b.id
        LEFT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:mbti IS NULL OR :mbti = '' OR m.mbti = :mbti)
        
        UNION
        
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
            b.id, b.bookImage, b.title, b.author, b.publisher,
            g.name, b.ageGroup, t.name, m.mbti)
        FROM Book b
        RIGHT JOIN Genre g ON b.genre.id = g.id
        RIGHT JOIN BookTopic bt ON bt.book.id = b.id
        RIGHT JOIN Topic t ON bt.topic.id = t.id
        RIGHT JOIN BookMBTI bm ON bm.book.id = b.id
        RIGHT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:mbti IS NULL OR :mbti = '' OR m.mbti = :mbti)
        """)
    Page<AdminBookFilterResponseDto> filterBooksMBTI(@Param("mbti") MBTIName mbti, Pageable pageable);

    // 장르 + 주제어
    @Query(value = """
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
        b.id, b.bookImage, b.title, b.author, b.publisher, g.name, b.ageGroup,
        t.name, m.mbti
     )
        FROM Book b
        LEFT JOIN Genre g ON b.genre.id = g.id
        LEFT JOIN BookTopic bt ON bt.book.id = b.id
        LEFT JOIN Topic t ON bt.topic.id = t.id
        LEFT JOIN BookMBTI bm ON bm.book.id = b.id
        LEFT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:genre IS NULL OR :genre = '' OR g.name = :genre)
        AND (:topic IS NULL OR :topic = '' OR t.name = :topic)
        
        UNION
        
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
            b.id, b.bookImage, b.title, b.author, b.publisher,
            g.name, b.ageGroup, t.name, m.mbti)
        FROM Book b
        RIGHT JOIN Genre g ON b.genre.id = g.id
        RIGHT JOIN BookTopic bt ON bt.book.id = b.id
        RIGHT JOIN Topic t ON bt.topic.id = t.id
        RIGHT JOIN BookMBTI bm ON bm.book.id = b.id
        RIGHT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:genre IS NULL OR :genre = '' OR g.name = :genre)
        AND (:topic IS NULL OR :topic = '' OR t.name = :topic)
        """)
    Page<AdminBookFilterResponseDto> filterBooksGenreTopic(@Param("genre") String genre, @Param("topic") String topic, Pageable pageable);

    // 장르 + MBTI
    @Query(value = """
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
        b.id, b.bookImage, b.title, b.author, b.publisher, g.name, b.ageGroup,
        t.name, m.mbti
     )
        FROM Book b
        LEFT JOIN Genre g ON b.genre.id = g.id
        LEFT JOIN BookTopic bt ON bt.book.id = b.id
        LEFT JOIN Topic t ON bt.topic.id = t.id
        LEFT JOIN BookMBTI bm ON bm.book.id = b.id
        LEFT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:genre IS NULL OR :genre = '' OR g.name = :genre)
        AND (:mbti IS NULL OR :mbti = '' OR m.mbti = :mbti)
        
        UNION
        
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
            b.id, b.bookImage, b.title, b.author, b.publisher,
            g.name, b.ageGroup, t.name, m.mbti)
        FROM Book b
        RIGHT JOIN Genre g ON b.genre.id = g.id
        RIGHT JOIN BookTopic bt ON bt.book.id = b.id
        RIGHT JOIN Topic t ON bt.topic.id = t.id
        RIGHT JOIN BookMBTI bm ON bm.book.id = b.id
        RIGHT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:genre IS NULL OR :genre = '' OR g.name = :genre)
        AND (:mbti IS NULL OR :mbti = '' OR m.mbti = :mbti)
        """)
    Page<AdminBookFilterResponseDto> filterBooksGenreMBTI(@Param("genre") String genre, @Param("mbti") MBTIName mbti, Pageable pageable);

    // 주제어 + MBTI
    @Query(value = """
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
        b.id, b.bookImage, b.title, b.author, b.publisher, g.name, b.ageGroup,
        t.name, m.mbti
     )
        FROM Book b
        LEFT JOIN Genre g ON b.genre.id = g.id
        LEFT JOIN BookTopic bt ON bt.book.id = b.id
        LEFT JOIN Topic t ON bt.topic.id = t.id
        LEFT JOIN BookMBTI bm ON bm.book.id = b.id
        LEFT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:topic IS NULL OR :topic = '' OR t.name = :topic)
        AND (:mbti IS NULL OR :mbti = '' OR m.mbti = :mbti)
        
        UNION
        
        SELECT DISTINCT new com.kkumteul.domain.book.dto.AdminBookFilterResponseDto(
            b.id, b.bookImage, b.title, b.author, b.publisher,
            g.name, b.ageGroup, t.name, m.mbti)
        FROM Book b
        RIGHT JOIN Genre g ON b.genre.id = g.id
        RIGHT JOIN BookTopic bt ON bt.book.id = b.id
        RIGHT JOIN Topic t ON bt.topic.id = t.id
        RIGHT JOIN BookMBTI bm ON bm.book.id = b.id
        RIGHT JOIN MBTI m ON bm.mbti.id = m.id
        WHERE (:topic IS NULL OR :topic = '' OR t.name = :topic)
        AND (:mbti IS NULL OR :mbti = '' OR m.mbti = :mbti)
        """)
    Page<AdminBookFilterResponseDto> filterBooksTopicMBTI(@Param("topic") String topic, @Param("mbti") MBTIName mbti, Pageable pageable);

    // -------------------------------------------------------------------------------
    @Query("SELECT b FROM Book b JOIN FETCH b.genre JOIN FETCH b.bookTopics WHERE b.id = :bookId")
    Optional<Book> findBookByIdWithGenreAndTopic(@Param("bookId") Long bookId);

    @Query(value = """
        SELECT b FROM Book b
        JOIN b.genre bg
        JOIN b.bookTopics bt
        JOIN bt.topic t
        WHERE b.title LIKE CONCAT('%', :keyword, '%')
        OR b.author LIKE CONCAT('%', :keyword, '%')
        OR t.name LIKE CONCAT('%', :keyword, '%')
        OR bg.name LIKE CONCAT('%', :keyword, '%')
        ORDER BY CASE
        WHEN b.title LIKE CONCAT('%', :keyword, '%') THEN 0
        WHEN b.author LIKE CONCAT('%', :keyword, '%') THEN 1
        WHEN t.name LIKE CONCAT('%', :keyword, '%') THEN 2
        WHEN bg.name LIKE CONCAT('%', :keyword, '%') THEN 3
        ELSE 4 END, b.id ASC
        """)
    Page<Book> findBookListByKeyword(
            @Param("keyword") final String keyword,
            final Pageable pageable);

}
