package org.api.rest_api_grupo2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ping controller for test.
 */
@RestController
public class PingController {
    /**
     * @return "pong" String.
     */
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
