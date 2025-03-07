package com.meossamos.smore.global.filter;

import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudyAccessCheckFilter extends OncePerRequestFilter {
    private final StudyMemberService studyMemberService;

    public StudyAccessCheckFilter(StudyMemberService studyMemberService) {
        this.studyMemberService = studyMemberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {

        String uri = request.getRequestURI();
        System.out.println("uri: " + uri);

        // URL이 /api/study/{studyId}/... 형태인지 확인 (필요에 따라 정규표현식을 조정)
        if (uri.matches(".*/api/*/study/\\d+.*")) {
            System.out.println("uri matches");
            try {
                Long studyId = extractStudyId(uri);
                System.out.println("studyId: " + studyId);

                // SecurityContext에서 인증된 사용자 정보 가져오기
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if (principal instanceof User userDetails) {
                    Long memberId = Long.parseLong(userDetails.getUsername());
                    System.out.println("memberId: " + memberId);

                    // 해당 사용자가 studyId에 속해있는지 확인
                    if (!studyMemberService.isUserMemberOfStudy(memberId, studyId)) {
                        System.out.println("해당 스터디에 소속되어 있지 않습니다.");
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "해당 스터디에 소속되어 있지 않습니다.");
                        return;
                    }
                } else {
                    System.out.println("인증되지 않은 사용자입니다.");
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "인증되지 않은 사용자입니다.");
                    return;
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "요청 URL에 문제가 있습니다: " + e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // URL에서 "/api/study/{studyId}" 패턴에 해당하는 studyId 추출
    private Long extractStudyId(String uri) {
        Pattern pattern = Pattern.compile("/api/study/(\\d+)");
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IllegalArgumentException("유효한 studyId를 URL에서 찾을 수 없습니다: " + uri);
    }
}
