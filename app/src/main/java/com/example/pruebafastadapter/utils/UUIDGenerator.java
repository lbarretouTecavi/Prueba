package com.example.pruebafastadapter.utils;

import java.util.UUID;

public class UUIDGenerator {

    public static String generateIdUnique(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
