package com.example.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ServerApplicationTests {

    @Test
    void alwaysPasses() {
        assertTrue(true); // Tento test vždy projde
    }

}
