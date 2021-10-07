package com.asura.nio.gateway.route;

import java.util.List;

public interface HttpRouter {
    String route(List<String> urls);
}
