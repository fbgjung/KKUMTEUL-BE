package com.kkumteul.domain.childprofile.service;

import com.kkumteul.domain.childprofile.entity.ChildProfile;
import com.kkumteul.domain.childprofile.entity.CumulativeMBTIScore;
import com.kkumteul.domain.childprofile.entity.GenreScore;
import com.kkumteul.domain.childprofile.entity.TopicScore;
import com.kkumteul.domain.childprofile.repository.CumulativeMBTIScoreRepository;
import com.kkumteul.domain.childprofile.repository.GenreScoreRepository;
import com.kkumteul.domain.childprofile.repository.TopicScoreRepository;
import com.kkumteul.domain.history.entity.MBTIScore;
import com.kkumteul.exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalityScoreService {

    private final GenreScoreRepository genreScoreRepository;
    private final TopicScoreRepository topicScoreRepository;
    private final CumulativeMBTIScoreRepository cumulativeMBTIScoreRepository;

    @Transactional
    public void resetFavoriteScores(Long childProfileId) {
        log.info("reset Genre/Topic Score ChildProfile ID: {}", childProfileId);
        List<TopicScore> topicScores = topicScoreRepository.findByChildProfileId(childProfileId);
        List<GenreScore> genreScores = genreScoreRepository.findByChildProfileId(childProfileId);

        for (TopicScore topicScore : topicScores) {
            topicScore.resetScore();
        }

        for (GenreScore genreScore : genreScores) {
            genreScore.resetScore();
        }
    }

    @Transactional
    public void updateFavoriteGenresScore(ChildProfile childProfile, List<Long> favoriteGenreIds) {
        log.info("Updating GenreScores ChildProfile ID: {}", childProfile.getId());

        // 선택한 장르에 5점 부여
        for (Long genreId : favoriteGenreIds) {
            GenreScore genreScore = childProfile.getGenreScores().stream()
                    .filter(gs -> gs.getGenre().getId().equals(genreId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(genreId));
            genreScore.updateScore(5.0);
        }

        log.info("GenreScores updated ChildProfile ID: {}", childProfile.getId());
    }

    @Transactional
    public void updateFavoriteTopicsScore(ChildProfile childProfile, List<Long> favoriteTopicIds) {
        log.info("Updating TopicScores ChildProfile ID: {}", childProfile.getId());

        // 선택한 주제어에 5점 부여
        for (Long topicId : favoriteTopicIds) {
            TopicScore topicScore = childProfile.getTopicScores().stream()
                    .filter(ts -> ts.getTopic().getId().equals(topicId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(topicId));
            topicScore.updateScore(5.0);
        }

        log.info("TopicScores updated ChildProfile ID: {}", childProfile.getId());
    }

    @Transactional
    public void resetCumulativeMBTIScore(Long childProfileId) {
        log.info("reset CumulativeMBTIScore ChildProfile ID: {}", childProfileId);
        CumulativeMBTIScore cumulativeScore = cumulativeMBTIScoreRepository.findByChildProfileId(childProfileId)
                .orElseThrow(() -> new IllegalArgumentException("점수가 존재하지 않습니다."));

        cumulativeScore.resetScores();
    }

    @Transactional
    public CumulativeMBTIScore updateCumulativeMBTIScore(Long childProfileId, MBTIScore mbtiScore) {
        log.info("Update CumulativeMBTIScore ChildProfile ID: {}", childProfileId);
        CumulativeMBTIScore cumulativeScore = cumulativeMBTIScoreRepository.findByChildProfileId(childProfileId)
                .orElseThrow(() -> new IllegalArgumentException("점수가 존재하지 않습니다."));

        return cumulativeScore.updateScores(mbtiScore);
    }
}
