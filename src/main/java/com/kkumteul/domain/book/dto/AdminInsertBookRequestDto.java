package com.kkumteul.domain.book.dto;

import com.kkumteul.domain.mbti.entity.MBTI;
import com.kkumteul.domain.personality.entity.Genre;
import com.kkumteul.domain.personality.entity.Topic;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class AdminInsertBookRequestDto {
    // book
    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Author is required")
    private String author;

    @NotNull(message = "Publisher is required")
    private String publisher;

    @NotNull(message = "Price is required")
    private String price;

    @NotNull(message = "Page is required")
    private String page;

    @NotNull(message = "Age group is required")
    private String ageGroup;

    @NotNull(message = "Summary is required")
    private String summary;

    @NotNull(message = "Book image is required")
    private byte[] bookImage;

    // bookMbti
    @NotNull(message = "Book MBTI is required")
    private MBTI bookMbti;

    // bookGenre
    @NotNull(message = "Book genre list is required")
    private List<Genre> bookGenreList;

    // bookTopic
    @NotNull(message = "Book topic list is required")
    private List<Topic> bookTopicList;
}
