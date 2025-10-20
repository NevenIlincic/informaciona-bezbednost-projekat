package com.example.PublicKeyInfrastructure.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DateUtils {

    public Date convertToDate(int day, int month, int year){
        // 1. Kreirajte LocalDate objekat iz zadatih parametara
        LocalDate specificDate = LocalDate.of(year, month, day);

        // 2. Konvertujte LocalDate u ZonedDateTime (dodajte informaciju o zoni)
        // atStartOfDay() postavlja vreme na 00:00:00 tog dana
        // ZoneId.systemDefault() koristi vremensku zonu vaseg sistema
        return Date.from(
                specificDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }
}
