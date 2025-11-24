package com.project.expensemanger.core.common.cache;

public class CacheNames {
    public static final String SEPARATOR = ":";

    public static final String CATEGORY = "category";
    public static final String CATEGORIES = "categoryList";
    public static final String SUMMARY = CATEGORY + SEPARATOR + "summary";
    public static final String AVERAGE_RATIO = "average_ratio";


    public static final String AUTH = "auth";
    public static final String REFRESH_TOKEN = AUTH + SEPARATOR + "refresh_token";
}
