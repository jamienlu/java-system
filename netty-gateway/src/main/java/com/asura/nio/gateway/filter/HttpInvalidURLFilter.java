package com.asura.nio.gateway.filter;

import com.asura.nio.gateway.util.URLUtil;

import java.util.List;
import java.util.stream.Collectors;

public class HttpInvalidURLFilter implements HttpURLFilter {
    public List<String> filter(List<String> proxys) {
        return proxys.stream().map(url -> URLUtil.formatUrl(url))
                .filter(url -> URLUtil.isURL(url))
                .collect(Collectors.toList());
    }
}
