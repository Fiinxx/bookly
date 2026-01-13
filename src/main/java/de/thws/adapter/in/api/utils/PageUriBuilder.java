package de.thws.adapter.in.api.utils;

import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class PageUriBuilder {
    public static URI buildPageUri(UriInfo uriInfo, int page, int size) {
        return uriInfo.getRequestUriBuilder()
                .replaceQueryParam("page", page)
                .replaceQueryParam("size", size)
                .scheme(null)
                .host(null)
                .port(-1)
                .build();
    }
}
