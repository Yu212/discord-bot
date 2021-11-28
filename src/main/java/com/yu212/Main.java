package com.yu212;

import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        String token = args[0];
        JDABuilder.createDefault(token)
                .build();
    }
}
