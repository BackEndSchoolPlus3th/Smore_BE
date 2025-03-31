package com.meossamos.smore.domain.chat.livekit.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import com.meossamos.smore.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class LiveKitService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;

    public Map<String, String> getUserInfo(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
//        System.out.println(authentication.toString());
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long userID = Long.valueOf(Integer.parseInt(principal.getUsername()));

        Member targetMember = memberRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("해당 ID의 회원을 찾을 수 없습니다." + userID));
        System.out.println(targetMember.toString());
        String userEmail = targetMember.getEmail();

        //스터디 이름 찾기
        List<StudyMember> SMList =studyMemberRepository.findByMember(targetMember);
//        for(StudyMember sm:SMList){
//            System.out.println(sm.toString());
//        }
//        System.out.println(SMList.get(0));
        StudyMember targetStudyMember = SMList.get(0);
//        String userId = principal.getUsername();
        String studyTitle = targetStudyMember.getStudy().getTitle();

        return Map.of(
                "studyTitle", studyTitle,
                "userEmail", userEmail
//                "userId", userId
        );
    }

}
