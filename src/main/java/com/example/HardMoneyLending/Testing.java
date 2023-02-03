package com.example.HardMoneyLending;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class Testing {

    public static void main(String[] args) {
        Set<String> str = new HashSet<>();
        str.add("Balti");
        str.add("Tahir");

        List<String> stringList = Arrays.asList("Balti 1", "Junaid");

        stringList.forEach(str::add);

        str.forEach(System.out::println);

        getTimePresentation(LocalDateTime.now());
    }

    private static String getTimePresentation(LocalDateTime created) {
        long second = created.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        long min = created.until(LocalDateTime.now(), ChronoUnit.MINUTES);
        long hour = created.until(LocalDateTime.now(), ChronoUnit.HOURS);
        long day = created.until(LocalDateTime.now(), ChronoUnit.DAYS);
        long week = created.until(LocalDateTime.now(), ChronoUnit.WEEKS);
        long month = created.until(LocalDateTime.now(), ChronoUnit.MONTHS);

        if(month > 0)
            return month + ( month > 1 ? " months" : " month");
        if(month <= 0 && week > 0)
            return week + ( week > 1 ? " weeks" : " week");
        if(month <= 0 && week <= 0 && day > 0)
            return week + ( week > 1 ? " weeks" : " week");
        if(month <= 0 && week <= 0 && day <= 0 && hour > 0)
            return hour + ( hour > 1 ? " hours" : " hour");
        if(month <= 0 && week <= 0 && day <= 0 && hour <= 0 && min > 0)
            return min + ( min > 1 ? " minutes" : " minute");
        if(month <= 0 && week <= 0 &&  day <= 0 && hour <= 0 && min <= 0 && second > 0)
            return second + ( second > 1 ? " seconds" : " second");
        return "On Universe Creation";
    }
}