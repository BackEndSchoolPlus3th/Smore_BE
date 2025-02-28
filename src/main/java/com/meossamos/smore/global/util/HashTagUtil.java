package com.meossamos.smore.global.util;

import java.util.*;

public class HashTagUtil {
    // 전체 해시태그 목록 (필요에 따라 수정)
    private static final List<String> ALL_TAGS = Arrays.asList(
            "java", "python", "백엔드", "프론트", "개발자",
            "docker", "kubernetes", "javascript", "react", "nodejs",
            "angular", "mysql", "postgresql", "git", "aws",
            "azure", "c++", "c#", "html", "css",
            "spring boot", "vue", "typescript", "mongodb", "redis"
    );

    public static String getRandomHashTag() {
        Random random = new Random();
        int index = random.nextInt(ALL_TAGS.size());
        return ALL_TAGS.get(index);
    }
    public static List<String> getRandomHashTags() {
        Random random = new Random();
        // 추출할 해시태그 개수를 1~5 사이로 랜덤 선택 (목록 크기를 초과하지 않도록)
        int maxCount = Math.min(5, ALL_TAGS.size());
        int count = random.nextInt(maxCount) + 1; // 1부터 maxCount까지

        // 원본 리스트를 수정하지 않도록 복사 후 셔플
        List<String> tagsCopy = new ArrayList<>(ALL_TAGS);
        Collections.shuffle(tagsCopy);

        // 앞에서부터 count개 선택
        return tagsCopy.subList(0, count);
    }
}
