package com.kkumteul.domain.childprofile.controller;

import com.kkumteul.domain.childprofile.dto.ChildProfileResponseDto;
import com.kkumteul.domain.childprofile.service.ChildProfileService;
import com.kkumteul.util.ApiUtil;
import com.kkumteul.util.ApiUtil.ApiSuccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.kkumteul.domain.childprofile.dto.ChildProfileDto;
import com.kkumteul.domain.childprofile.service.ChildProfileService;
import com.kkumteul.util.ApiUtil;
import com.kkumteul.util.ApiUtil.ApiSuccess;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/childProfiles")
public class ChildProfileController {
    private final ChildProfileService childProfileService;

    // 1. 자녀 프로필 상세 조회
    @GetMapping("/{childProfileId}")
    public ApiSuccess<?> getChildProfile(@PathVariable(name = "childProfileId") Long childProfileId) {
        ChildProfileResponseDto childProfile = childProfileService.getChildProfileDetail(childProfileId);
        return ApiUtil.success(childProfile);
    }

    @GetMapping
    public ApiSuccess<?> getChildProfiles() {
        // 인증 인가 구현 시 @AuthenticationPrincipal 사용해서 UserID 가져오기.
        Long userId = 1L;

        List<ChildProfileDto> childProfiles = childProfileService.getChildProfileList(userId);

        return ApiUtil.success(childProfiles);
    }

    //JWT 구현되면 Session방식에서 토큰 활용하는 방식으로 수정 예정
    @PostMapping("/switch")
    public ApiSuccess<?> switchChildProfile(@RequestParam(name = "childProfileId") Long childProfileId, HttpSession session) {
        childProfileService.validateChildProfile(childProfileId);

        session.setAttribute("currentChildProfileId", childProfileId);

        return ApiUtil.success("프로필이 성공적으로 변경되었습니다.");
    }
}
