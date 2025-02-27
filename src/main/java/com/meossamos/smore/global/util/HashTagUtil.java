package com.meossamos.smore.global.util;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 입력된 해시태그 문자열을 정규화
     * 쉼표(,)로 분리 후, 각 태그를 trim, 소문자 변환하고, 중복을 제거하여 다시 쉼표로 결합합
     *
     * @param hashTags 원본 해시태그 문자열 (예: "Java, Spring, java")
     * @return 정규화된 해시태그 문자열 (예: "java,spring")
     */
    public static String normalizeHashTags(String hashTags) {
        if (hashTags == null || hashTags.isEmpty()) {
            return "";
        }
        return Arrays.stream(hashTags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.joining(","));
    }

    /**
     * 기존 해시태그와 새 해시태그를 병합하여 중복 없이 반환
     * 각 해시태그 문자열은 먼저 정규화한 후, 두 문자열에 있는 태그들을 합침
     *
     * @param existingHashTags 기존에 저장된 해시태그 문자열
     * @param newHashTags 새로 추가할 해시태그 문자열
     * @return 병합된 해시태그 문자열
     */
    public static String mergeHashTags(String existingHashTags, String newHashTags) {
        Set<String> tags = new HashSet<>();

        // 기존 해시태그가 있을 경우 정규화 후 Set에 추가
        if (existingHashTags != null && !existingHashTags.isEmpty()) {
            tags.addAll(Arrays.asList(normalizeHashTags(existingHashTags).split(",")));
        }

        // 새 해시태그가 있을 경우 정규화 후 Set에 추가
        if (newHashTags != null && !newHashTags.isEmpty()) {
            tags.addAll(Arrays.asList(normalizeHashTags(newHashTags).split(",")));
        }

        return String.join(",", tags);
    }

    public static String mergeHashTagList(List<String> hashTags) {
        return hashTags.stream()
                .map(HashTagUtil::normalizeHashTags)
                .collect(Collectors.joining(","));
    }
}
