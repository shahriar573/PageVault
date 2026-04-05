package com.example.pagevault.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Small helper to generate BCrypt hashes locally.
 *
 * Usage:
 *   java com.example.pagevault.util.BcryptHashPrinter "Admin@1234"
 */
public final class BcryptHashPrinter {

    private BcryptHashPrinter() {
    }

    public static void main(String[] args) {
        String raw = args != null && args.length > 0 ? args[0] : "";
        if (raw.isBlank()) {
            System.err.println("Please provide a password argument. Example: Admin@1234");
            System.exit(2);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode(raw));
    }
}

