package com.novibe.common.data_sources;

import com.novibe.common.config.EnvironmentVariables;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class DomainFilter {

    private static final List<Pattern> EXCLUDE_PATTERNS = parseExcludePatterns();

    private DomainFilter() {
    }

    public static boolean isExcluded(String domain) {
        if (EXCLUDE_PATTERNS.isEmpty()) {
            return false;
        }
        String lower = domain.toLowerCase();
        for (Pattern p : EXCLUDE_PATTERNS) {
            if (p.matcher(lower).find()) {
                return true;
            }
        }
        return false;
    }

    private static List<Pattern> parseExcludePatterns() {
        String exclude = EnvironmentVariables.EXCLUDE;
        if (exclude == null || exclude.isBlank()) {
            return List.of();
        }
        return Stream.of(exclude.split(","))
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .map(s -> Pattern.compile(s, Pattern.CASE_INSENSITIVE))
                .toList();
    }

}
