package com.meossamos.smore.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    // 사용할 문자 집합 정의 (대문자, 소문자, 숫자, 특수문자)
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+<>?";
    // 위의 문자들을 모두 합친 문자열
    private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SYMBOLS;

    private static final Random RANDOM = new Random();
    private static final List<String> ALL_TITLES = List.of(
            "Java 기초 스터디 모집",
            "Spring Boot 심화 스터디 함께해요",
            "파이썬 입문 스터디 진행중",
            "데이터 분석 기초 스터디 모집",
            "웹 개발 실습 스터디",
            "모바일 앱 개발 스터디",
            "알고리즘 문제 풀이 스터디",
            "머신러닝 기초 스터디 모집",
            "딥러닝 프로젝트 스터디",
            "인공지능 기술 스터디",
            "ReactJS 프론트엔드 스터디",
            "Node.js 백엔드 스터디",
            "풀스택 개발 스터디 모집",
            "DevOps & 클라우드 스터디",
            "Git & GitHub 활용 스터디",
            "SQL 데이터베이스 스터디",
            "NoSQL & MongoDB 스터디",
            "AWS 클라우드 스터디",
            "Azure 실무 스터디",
            "Google Cloud 스터디",
            "스타트업 창업 아이디어 스터디",
            "프로젝트 관리 & 협업 스터디",
            "UI/UX 디자인 스터디",
            "코딩 테스트 대비 스터디",
            "해커톤 준비 스터디",
            "보안 및 해킹 스터디",
            "네트워크 기초 스터디",
            "리눅스 운영체제 스터디",
            "오픈 소스 기여 스터디",
            "블록체인 기술 스터디",
            "사물인터넷(IoT) 스터디",
            "VR/AR 개발 스터디",
            "게임 개발 스터디",
            "Unity 3D 스터디",
            "Unreal Engine 스터디",
            "멀티미디어 콘텐츠 제작 스터디",
            "디지털 마케팅 스터디",
            "빅데이터 분석 스터디",
            "R 프로그래밍 스터디",
            "Scala & Spark 스터디",
            "컴퓨터 비전 스터디",
            "자바스크립트 심화 스터디",
            "타입스크립트 입문 스터디",
            "PHP 웹 개발 스터디",
            "Ruby on Rails 스터디",
            "Go 언어 스터디",
            "Kotlin 개발 스터디",
            "Swift iOS 개발 스터디",
            "안드로이드 앱 스터디",
            "C++ 알고리즘 스터디"
    );

    private static final List<String> ALL_CONTENTS = List.of(
            "안녕하세요, Java 기초 스터디입니다. 초보자도 쉽게 이해할 수 있도록 기초부터 차근차근 공부합니다. 함께 성장할 분들을 기다립니다.",
            "Spring Boot 심화 스터디 모집합니다. 실무에서 활용 가능한 기술과 다양한 프로젝트 경험을 공유하며 스킬을 업그레이드해요.",
            "파이썬 입문자를 위한 스터디입니다. 기본 문법부터 시작해 데이터 처리 및 간단한 프로젝트까지 함께 진행할 예정입니다.",
            "데이터 분석 기초 스터디에 참여해보세요. 엑셀부터 시작해 Python과 R을 활용한 데이터 분석 방법을 배웁니다.",
            "웹 개발 실습 스터디에서는 HTML, CSS, JavaScript를 활용해 실제 웹 페이지를 만들어봅니다. 실습 중심으로 진행됩니다.",
            "모바일 앱 개발 스터디에 참여하여 Android와 iOS 개발의 기초부터 심화 과정까지 함께 배워보세요.",
            "알고리즘 문제 풀이 스터디입니다. 다양한 문제를 함께 풀어보며 알고리즘적 사고력을 기르고, 코딩 테스트 대비도 가능합니다.",
            "머신러닝 기초 스터디를 통해 데이터 모델링, 분류 및 예측 기술을 실습 중심으로 배워보세요. 기초 이론부터 실습까지 체계적으로 진행합니다.",
            "딥러닝 프로젝트 스터디 모집합니다. 신경망 모델 설계와 구현을 통해 실무 프로젝트 경험을 쌓아볼 수 있는 기회입니다.",
            "인공지능 기술 스터디에서는 최신 AI 기술 동향과 실습을 통해 인공지능의 기초를 배우고 응용할 수 있도록 돕습니다.",
            "ReactJS 프론트엔드 스터디에 참여해 실무에서 필요한 React 개발 기법을 학습하고, 프로젝트 경험을 쌓아보세요.",
            "Node.js 백엔드 스터디를 통해 서버 사이드 프로그래밍 및 비동기 처리를 효율적으로 구현하는 방법을 익힙니다.",
            "풀스택 개발 스터디에서는 프론트엔드와 백엔드 기술을 모두 다루며, 종합적인 개발 역량을 키울 수 있습니다.",
            "DevOps & 클라우드 스터디입니다. CI/CD, Docker, Kubernetes 등 최신 인프라 기술을 함께 학습하며 실습합니다.",
            "Git & GitHub 활용 스터디로 버전 관리 및 협업 도구의 효율적인 사용법을 익혀 프로젝트 진행에 도움을 줍니다.",
            "SQL 데이터베이스 스터디에서는 기본 SQL 문법부터 고급 쿼리 작성법까지 실습과 이론을 겸비한 학습을 진행합니다.",
            "NoSQL & MongoDB 스터디에 참여해 다양한 데이터베이스 기술을 비교하고, 실제 프로젝트에 적용하는 방법을 배워봅니다.",
            "AWS 클라우드 스터디는 클라우드 인프라 구축과 운영에 필요한 핵심 기술을 실습과 함께 배우는 기회입니다.",
            "Azure 실무 스터디입니다. Microsoft Azure의 다양한 서비스를 활용해 클라우드 환경을 구축하는 방법을 학습합니다.",
            "Google Cloud 스터디에서는 구글의 클라우드 플랫폼 서비스를 활용한 인프라 구성과 운영 방법을 심도 있게 다룹니다.",
            "스타트업 창업 아이디어 스터디입니다. 창업 아이디어를 공유하고, 팀 빌딩 및 사업 모델에 대해 함께 고민해보는 시간을 가집니다.",
            "프로젝트 관리 & 협업 스터디에서는 효율적인 프로젝트 관리 기법과 팀 협업 도구 사용법을 학습하며 실무 경험을 쌓습니다.",
            "UI/UX 디자인 스터디에 참여해 사용자의 경험을 개선하는 디자인 원칙과 실전 디자인 방법을 배워봅니다.",
            "코딩 테스트 대비 스터디로 알고리즘 문제 풀이 및 인터뷰 준비를 체계적으로 진행하여 취업에 도움을 드립니다.",
            "해커톤 준비 스터디입니다. 짧은 기간 내에 아이디어를 구현하는 방법과 팀워크의 중요성을 경험할 수 있습니다.",
            "보안 및 해킹 스터디에 참여하여 최신 보안 기술과 해킹 기법을 학습하며, 안전한 시스템 구축 방법을 모색합니다.",
            "네트워크 기초 스터디는 인터넷의 기본 구조와 통신 원리를 이해하고, 실습을 통해 네트워크 구축 및 문제 해결 능력을 기릅니다.",
            "리눅스 운영체제 스터디에 참여해 명령어 기초부터 시스템 관리까지 리눅스 활용 능력을 키워봅니다.",
            "오픈 소스 기여 스터디로 Git 사용법과 오픈 소스 프로젝트 참여 방법을 학습하고, 실제 기여 경험을 쌓아봅니다.",
            "블록체인 기술 스터디에서는 분산 원장 기술과 스마트 컨트랙트를 이해하고, 실습을 통해 블록체인 시스템을 구축해봅니다.",
            "사물인터넷(IoT) 스터디에 참여해 센서 및 네트워크 기술을 활용한 IoT 기기 제작과 데이터 처리 방법을 배워봅니다.",
            "VR/AR 개발 스터디입니다. 가상현실 및 증강현실 기술을 활용한 콘텐츠 제작과 개발 방법을 함께 학습합니다.",
            "게임 개발 스터디에 참여해 Unity, Unreal Engine 등 다양한 게임 엔진을 활용한 게임 디자인 및 개발을 경험해봅니다.",
            "Unity 3D 스터디는 게임 개발 및 3D 모델링에 관심 있는 분들을 위해 실습과 프로젝트를 진행합니다.",
            "Unreal Engine 스터디에서 고품질 게임 개발 기술을 배우고, 실제 프로젝트를 통해 실력을 향상시켜 보세요.",
            "멀티미디어 콘텐츠 제작 스터디는 영상, 음향, 디자인 등 다양한 분야의 기술을 융합하여 창의적인 콘텐츠를 만들어갑니다.",
            "디지털 마케팅 스터디에 참여해 온라인 마케팅 전략과 최신 트렌드를 분석하며 실전 마케팅 역량을 강화합니다.",
            "빅데이터 분석 스터디에서는 대규모 데이터를 효율적으로 처리하는 기술과 도구를 실습과 함께 학습합니다.",
            "R 프로그래밍 스터디로 통계 분석과 데이터 시각화 기법을 R 언어를 통해 체계적으로 배워봅니다.",
            "Scala & Spark 스터디에 참여하여 빅데이터 처리 기술을 배우고, 실무에서 활용 가능한 데이터 처리 기법을 익힙니다.",
            "컴퓨터 비전 스터디에서는 이미지 처리와 딥러닝을 결합한 최신 기술 동향을 학습하고, 실제 사례를 분석합니다.",
            "자바스크립트 심화 스터디로 프론트엔드 기술의 핵심 개념을 심도 있게 다루며, 다양한 웹 애플리케이션을 개발합니다.",
            "타입스크립트 입문 스터디에 참여하여 자바스크립트의 확장 언어를 배우고, 보다 안전한 코딩 방법을 익힙니다.",
            "PHP 웹 개발 스터디에서는 서버 사이드 프로그래밍의 기초부터 심화 기술까지 다양한 주제를 다룹니다.",
            "Ruby on Rails 스터디로 웹 애플리케이션 개발의 효율적인 방법을 배우고, 실제 프로젝트를 진행해봅니다.",
            "Go 언어 스터디에 참여해 고성능 서버 개발과 효율적인 코드 작성법을 학습하며 실습 경험을 쌓습니다.",
            "Kotlin 개발 스터디는 최신 안드로이드 개발 언어를 활용해 간결하고 효율적인 코딩 방법을 배워봅니다.",
            "Swift iOS 개발 스터디에 참여해 애플 생태계의 다양한 기술을 학습하고, 실제 앱 개발 프로젝트를 진행합니다.",
            "안드로이드 앱 스터디에서는 모바일 앱 개발의 최신 트렌드를 반영한 실습과 프로젝트를 함께 진행합니다.",
            "C++ 알고리즘 스터디에 참여해 효율적인 문제 해결 방법과 고성능 코드 작성법을 체계적으로 배우는 기회를 제공합니다."
    );

    private static final List<String> ALL_INTRODUCTIONS = List.of(
            "함께 성장하는 스터디입니다.",
            "기초부터 차근차근 배웁니다.",
            "실습 위주의 학습을 진행합니다.",
            "초보자도 환영합니다.",
            "실무 경험을 쌓을 수 있습니다.",
            "열정적인 분들의 참여를 기다립니다.",
            "함께 도전하며 발전합니다.",
            "협업과 소통을 중시합니다.",
            "최신 기술을 함께 학습합니다.",
            "아이디어를 공유하는 시간입니다.",
            "문제 해결 능력을 키웁니다.",
            "꾸준한 학습이 목표입니다.",
            "실전 프로젝트를 경험합니다.",
            "창의적인 아이디어를 환영합니다.",
            "성공적인 커리어를 위한 준비입니다.",
            "열린 마음으로 함께합니다.",
            "지식을 나누는 소중한 자리입니다.",
            "도전과 성장을 응원합니다.",
            "매주 꾸준히 모임을 가집니다.",
            "친목 도모와 학습을 동시에 합니다.",
            "효과적인 협업을 경험할 수 있습니다.",
            "함께 문제를 해결해나갑니다.",
            "서로의 경험을 공유합니다.",
            "집중적인 실습 시간을 마련합니다.",
            "전문가의 조언을 들을 수 있습니다.",
            "실력을 키우는 기회입니다.",
            "함께 배우며 성장합니다.",
            "긍정적인 에너지가 넘칩니다.",
            "새로운 도전을 환영합니다.",
            "함께하는 학습의 즐거움입니다.",
            "스스로 발전하는 기회입니다.",
            "경험과 지식을 나눕니다.",
            "열정적인 모임입니다.",
            "자신감을 키울 수 있습니다.",
            "도전 정신을 응원합니다.",
            "체계적인 학습을 진행합니다.",
            "실전 경험을 쌓아갑니다.",
            "꾸준한 노력이 있습니다.",
            "서로의 성장을 지원합니다.",
            "함께 목표를 달성합니다.",
            "열린 소통의 장입니다.",
            "효과적인 학습 환경입니다.",
            "성취감을 느낄 수 있습니다.",
            "함께 꿈을 이루어갑니다.",
            "긍정적이고 열정적입니다.",
            "미래를 위한 준비입니다.",
            "함께 도전하는 시간입니다.",
            "전문성을 향상시킵니다.",
            "협력하며 배우는 모임입니다.",
            "지속적인 발전을 추구합니다."
    );

    private static final List<String> ALL_MEMBERS = List.of(
            "TechGuru", "CodeMaster", "DevNinja", "JavaWizard", "Pythonista",
            "WebSlinger", "BugHunter", "DesignPro", "StackHero", "BinaryBoss",
            "CodeCrusader", "SyntaxSamurai", "AlgoAce", "CyberSage", "CloudSurfer",
            "DataDiver", "NetNavigator", "ScriptSlinger", "PixelPioneer", "DigitalDreamer",
            "CodeCaptain", "ServerSultan", "FunctionFreak", "VariableViking", "LoopLord",
            "CompileKing", "ErrorExterminator", "JavaJedi", "CSharpChampion", "DebugDiva",
            "TechTactician", "ProgramProdigy", "CodeCommander", "AlgorithmArtist", "BugBuster",
            "ScriptSensei", "LogicLancer", "DigitalDynamo", "NetworkNomad", "InterfaceInventor",
            "SystemSavant", "DataDynamo", "VirtualVirtuoso", "InterfaceIlluminator", "SyntaxSleuth",
            "RuntimeRanger", "TechTrailblazer", "CloudConqueror", "AlgorithmAdventurer", "CyberChampion",
            "김민수", "이영희", "박지훈", "최서연", "정민재",
            "장민지", "윤서현", "오지훈", "한지민", "서지호",
            "임수빈", "류민재", "강태현", "홍길동", "유은정",
            "안지훈", "조수민", "민경민", "곽현진", "류예린",
            "정재훈", "나혜진", "전민호", "신예림", "진승우",
            "문서현", "서윤지", "손현주", "황지은", "이정후",
            "김소희", "박세진", "송민석", "임태경", "조다은",
            "오하늘", "정은채", "한승민", "신민지", "노준호",
            "장수빈", "변진호", "배서현", "김재윤", "이수민",
            "박지연", "홍민수", "김하늘", "이현우", "박예린"
    );

    private static final List<String> ALL_KOREAN_REGIONS = List.of(
            "서울",
            "부산",
            "대구",
            "인천",
            "광주",
            "대전",
            "울산",
            "세종",
            "경기",
            "강원",
            "충청북도",
            "충청남도",
            "전라북도",
            "전라남도",
            "경상북도",
            "경상남도",
            "제주"
    );
    private static final List<String> ALL_RANDOM_EMAILS = List.of(
            "minsu.kim@gmail.com",
            "yeonghee.lee@naver.com",
            "jihun.park@daum.net",
            "seoyeon.choi@hanmail.net",
            "minjae.jung@outlook.com",
            "minji.jang@naver.com",
            "seohyun.yoon@gmail.com",
            "jihyun.oh@daum.net",
            "jimin.han@hanmail.net",
            "jiho.lim@outlook.com",
            "suhyun.ryu@gmail.com",
            "taehyun.kang@naver.com",
            "gil.dong.hong@daum.net",
            "eunjeong.yoo@hanmail.net",
            "jihun.ahn@outlook.com",
            "sumin.cho@naver.com",
            "mingyeong.gwak@gmail.com",
            "yerin.ryu@daum.net",
            "jaehun.jeong@hanmail.net",
            "hyejin.na@outlook.com",
            "minho.jeon@gmail.com",
            "yerim.sin@naver.com",
            "seunghwoo.jin@daum.net",
            "seohee.moon@hanmail.net",
            "yoonji.suh@outlook.com",
            "joosoo.han@gmail.com",
            "seojin.hong@naver.com",
            "hyunwoo.kim@daum.net",
            "suhyun.lee@hanmail.net",
            "somin.park@outlook.com",
            "sohee.cho@naver.com",
            "minseok.song@gmail.com",
            "taehee.park@daum.net",
            "jinwoo.kim@hanmail.net",
            "hyeri.lee@outlook.com",
            "soojin.choi@gmail.com",
            "minyoung.jung@naver.com",
            "jiwoo.oh@daum.net",
            "haneul.lee@hanmail.net",
            "dohyun.kim@outlook.com",
            "seojin.park@gmail.com",
            "eunsoo.lee@naver.com",
            "youngmin.cho@daum.net",
            "sihyun.lee@hanmail.net",
            "junseo.kim@outlook.com",
            "haeun.lee@gmail.com",
            "yuna.park@naver.com",
            "minji.oh@daum.net",
            "seojin.jung@hanmail.net",
            "jiyeon.lee@outlook.com"
    );

    public static String getRandomTitle() {
        int index = RANDOM.nextInt(ALL_TITLES.size());
        return ALL_TITLES.get(index);
    }

    public static String getRandomContent() {
        int index = RANDOM.nextInt(ALL_CONTENTS.size());
        return ALL_CONTENTS.get(index);
    }

    public static String getRandomIntroduction() {
        int index = RANDOM.nextInt(ALL_INTRODUCTIONS.size());
        return ALL_INTRODUCTIONS.get(index);
    }

    public static String getRandomMemberName() {
        int index = RANDOM.nextInt(ALL_MEMBERS.size());
        return ALL_MEMBERS.get(index);
    }

    public static String getRandomRegion() {
        int index = RANDOM.nextInt(ALL_KOREAN_REGIONS.size());
        return ALL_KOREAN_REGIONS.get(index);
    }

    public static String getRandomEmail() {
        int index = RANDOM.nextInt(ALL_RANDOM_EMAILS.size());
        return ALL_RANDOM_EMAILS.get(index);
    }

    public static String getRandomPassword(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("비밀번호 길이는 1 이상이어야 합니다.");
        }
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALL_CHARS.length());
            password.append(ALL_CHARS.charAt(index));
        }
        return password.toString();
    }

    public static LocalDate getRandomBirthday() {
        LocalDate today = LocalDate.now();
        LocalDate earliestDate = today.minusYears(90); // 90년 전 날짜
        // LocalDate를 epoch day(1970-01-01 기준 일수)로 변환
        long startEpochDay = earliestDate.toEpochDay();
        long endEpochDay = today.toEpochDay();
        // startEpochDay와 endEpochDay 사이의 랜덤한 값을 생성
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay + 1);
        // 생성된 epoch day를 LocalDate로 변환
        return LocalDate.ofEpochDay(randomEpochDay);
    }

    public static LocalDateTime getRandomEndDate() {
        LocalDateTime now = LocalDateTime.now();
        // 하루: 24시간 * 60분 * 60초 = 86,400초
        long minSeconds = 24 * 60 * 60L;
        // 1년: 365일 * 86,400초 = 31,536,000초
        long maxSeconds = 365 * minSeconds;
        // minSeconds 이상 maxSeconds 이하의 임의의 초 단위를 생성
        long randomSeconds = ThreadLocalRandom.current().nextLong(minSeconds, maxSeconds + 1);
        return now.plusSeconds(randomSeconds);
    }
}
