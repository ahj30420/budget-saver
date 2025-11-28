package com.project.expensemanger.core.common.cache;

import static com.project.expensemanger.core.common.cache.CacheNames.*;

public class RedisKeyGenerator {

    public static String getCategorySummaryKey() { return SUMMARY; }

    public static String getRefreshTokenKey(Long userId) {
        return REFRESH_TOKEN + SEPARATOR + userId;
    }
}
