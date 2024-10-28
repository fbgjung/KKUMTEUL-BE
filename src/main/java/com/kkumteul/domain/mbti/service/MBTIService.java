package com.kkumteul.domain.mbti.service;

import com.kkumteul.domain.mbti.entity.MBTI;
import com.kkumteul.domain.mbti.entity.MBTIName;
import com.kkumteul.domain.mbti.repository.MBTIRepository;
import org.springframework.transaction.annotation.Transactional;//
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MBTIService {
    private final MBTIRepository MBTIRepository;

    // 1. MBTI Name으로 MBTI 객체 가져오기
    public MBTI getMBTI(String mbti) {

        MBTIName mbtiName = MBTIName.valueOf(mbti);
        return MBTIRepository.findByMbti(mbtiName)
                .orElseThrow(() -> new IllegalArgumentException("mbti not found"));
    }
}
