package com.inn.cafe.Cafe.Management.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CafeUtils {

    // Use a Windows-safe format: replace colons with dashes
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    public static String getUUID(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return "BILL-" + localDateTime.format(FORMATTER);
    }

    public static Boolean isFileEists(String path){
        log.info("Inside FilesExists {}",path);
        try{
            File file  =  new File(path);
            return (file!=null && file.exists())? Boolean.TRUE:Boolean.FALSE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
