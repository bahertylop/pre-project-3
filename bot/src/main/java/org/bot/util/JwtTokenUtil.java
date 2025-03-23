package org.bot.util;

public class JwtTokenUtil {

    public static String bearerToken(String token) {
        return "Bearer " + token;
    }
}
