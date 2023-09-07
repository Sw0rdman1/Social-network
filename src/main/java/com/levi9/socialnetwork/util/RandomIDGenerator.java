package com.levi9.socialnetwork.util;

import java.security.SecureRandom;

public class RandomIDGenerator {

    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder randomString = new StringBuilder(15);

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 15; i++) {
            int randomIndex = random.nextInt(characters.length());

            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }


}
