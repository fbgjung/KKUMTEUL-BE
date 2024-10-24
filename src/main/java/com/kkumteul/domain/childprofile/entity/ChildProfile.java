package com.kkumteul.domain.childprofile.entity;


import com.kkumteul.domain.book.entity.BookLike;
import com.kkumteul.domain.history.entity.ChildPersonalityHistory;
import com.kkumteul.domain.recommendation.entity.Recommendation;
import com.kkumteul.domain.user.entity.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.kkumteul.domain.history.entity.ChildPersonalityHistory;
import com.kkumteul.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildProfile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date birthDate;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] profileImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 자녀 추천 책 리스트
    @OneToMany(mappedBy = "childProfile", cascade = CascadeType.ALL)
    List<Recommendation> recommendationList = new ArrayList<>();

    // 자녀 선호 장르 리스트
    @OneToMany(mappedBy = "childProfile", cascade = CascadeType.ALL)
    List<GenreScore> genreScoreList = new ArrayList<>();

    // 자녀 선호 주제어 리스트
    @OneToMany(mappedBy = "childProfile", cascade = CascadeType.ALL)
    List<TopicScore> topicScoreList = new ArrayList<>();

    // 자녀 성향 히스토리 리스트
    @OneToMany(mappedBy = "childProfile", cascade = CascadeType.ALL)
    List<ChildPersonalityHistory> childPersonalityHistoryList = new ArrayList<>();

    // 자녀 도서 좋아요 리스트
    @OneToMany(mappedBy = "childProfile", cascade = CascadeType.ALL)
    List<BookLike> bookLikeList = new ArrayList<>();
  
    @OneToMany(mappedBy = "childProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenreScore> genreScores = new ArrayList<>();

    @OneToMany(mappedBy = "childProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicScore> topicScores = new ArrayList<>();

    @OneToOne(mappedBy = "childProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private CumulativeMBTIScore cumulativeMBTIScore;

    @OneToMany(mappedBy = "childProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildPersonalityHistory> histories = new ArrayList<>();

    @Builder
    public ChildProfile(String name, Gender gender, Date birthDate, byte[] profileImage, User user) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.profileImage = profileImage;
        this.user = user;
    }

    public void addGenreScore(GenreScore genreScore) {
        genreScores.add(genreScore);
        genreScore.setChildProfile(this);
    }

    public void addTopicScore(TopicScore topicScore) {
        topicScores.add(topicScore);
        topicScore.setChildProfile(this);
    }

    public void setCumulativeMBTIScore(CumulativeMBTIScore cumulativeMBTIScore) {
        this.cumulativeMBTIScore = cumulativeMBTIScore;
        cumulativeMBTIScore.setChildProfile(this);
    }

    public void addHistory(ChildPersonalityHistory history) {
        histories.add(history);
        history.setChildProfile(this);
    }

    public void removeHistory(ChildPersonalityHistory history) {
        histories.remove(history);
        history.setChildProfile(null);
    }

    public List<GenreScore> getGenreScores() {
        return Collections.unmodifiableList(genreScores);
    }

    public List<TopicScore> getTopicScores() {
        return Collections.unmodifiableList(topicScores);
    }

    public List<ChildPersonalityHistory> getHistories() {
        return Collections.unmodifiableList(histories);
    }
}
