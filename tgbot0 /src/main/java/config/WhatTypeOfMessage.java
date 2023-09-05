package config;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
public class WhatTypeOfMessage {
    public static Map<Long, String> contextMap = new HashMap<>();
    public static String contextAddUser(Long chatId, String typeOfMessage) {
        String userExistsInDB = contextMap.get(chatId);
        if(userExistsInDB == null)
            contextMap.put(chatId, typeOfMessage);
        return contextMap.get(chatId);
    }
    public static void changeTypeForUser(Long chatId, String typeOfMessage){
        contextMap.put(chatId, typeOfMessage);
        System.out.println(contextMap.get(chatId));
    }

}
