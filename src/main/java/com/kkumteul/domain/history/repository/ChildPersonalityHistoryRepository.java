package com.kkumteul.domain.history.repository;

import com.kkumteul.domain.history.entity.ChildPersonalityHistory;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.kkumteul.domain.history.entity.HistoryCreatedType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChildPersonalityHistoryRepository extends JpaRepository<ChildPersonalityHistory, Long> {
    ChildPersonalityHistory findTopByChildProfileIdOrderByCreatedAtDesc(Long childProfileId);

    @Query("SELECT h FROM ChildPersonalityHistory h WHERE h.childProfile.id = :childProfileId AND h.historyCreatedType = :historyCreatedType")
    Optional<ChildPersonalityHistory> findHistoryByChildProfileIdAndHistoryCreatedType(Long childProfileId,
                                                                                       HistoryCreatedType historyCreatedType);
  
    @Query("SELECT h FROM ChildPersonalityHistory  h JOIN FETCH h.mbtiScore ms JOIN FETCH ms.mbti m WHERE h.childProfile.id = :childProfileId AND h.isDeleted = false")
    List<ChildPersonalityHistory> findHistoryWithMBTIByChildProfileId(@Param("childProfileId") Long childProfileId);

    @Query("SELECT h FROM ChildPersonalityHistory h WHERE h.isDeleted = true AND h.deletedAt < :dateTime")
    List<ChildPersonalityHistory> findAllByRealDelete(@Param("dateTime") LocalDateTime dateTime);
}
