package com.project.expensemanger.core.common.util;

import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class UrlCreator {

    private UrlCreator() {
    }

    public static URI createUri(String defaultUrl, Long resourceId) {
        return UriComponentsBuilder.newInstance()
                .path(defaultUrl + "/{resource-id}")
                .buildAndExpand(resourceId)
                .toUri();
    }

    public static URI createCollectionUri(String collectionUrl) {
        return UriComponentsBuilder.newInstance()
                .path(collectionUrl)
                .build()
                .toUri();
    }
}
