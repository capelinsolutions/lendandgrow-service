package com.example.HardMoneyLending.utils;

import com.example.HardMoneyLending.exception.general_exception.BadRequestException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final Date toDate(String date) throws BadRequestException {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            throw new BadRequestException("Date formate should be dd-MM-yyyy.");
        }
    }
}
