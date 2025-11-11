package com.project.expensemanger.core.common.util;

import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class UrlCreator {

    private UrlCreator() {}

    public static URI createUri(String defaultUrl, Long resourceId) {
        return UriComponentsBuilder.newInstance()
                .path(defaultUrl + "/{resource-id}")
                .buildAndExpand(resourceId)
                .toUri();
    }
}
