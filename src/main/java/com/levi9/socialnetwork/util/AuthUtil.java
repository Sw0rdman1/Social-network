package com.levi9.socialnetwork.util;

import com.levi9.socialnetwork.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthUtil {

    public static String getPrincipalId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserEntity user) {
            return user.getId();
        }
        return null;
    }

    public static String getPrincipalUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication != null && authentication.getPrincipal() instanceof UserEntity user) {
            return user.getUsername();
        }
        return null;
    }
}