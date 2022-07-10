package com.asura.nio.gateway.filter;

import java.util.List;

public interface HttpURLFilter {
    List<String> filter(List<String> proxys);
}
