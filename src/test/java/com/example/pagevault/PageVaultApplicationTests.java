package com.example.pagevault;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EnabledIf(expression = "#{systemProperties['springContextTest'] == 'true'}", reason = "Opt-in: loads full Spring context (slower)")
class PageVaultApplicationTests {

    @Test
    void contextLoads() {
    }

}
