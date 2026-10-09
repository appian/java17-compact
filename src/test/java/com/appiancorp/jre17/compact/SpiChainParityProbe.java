/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-10-09.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). Probe main class run in a forked JVM by SpiChainParityTest; prints locale facts obtained through the public JDK API so they can be compared with JDK 17 JRE data.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
package com.appiancorp.jre17.compact;

import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Prints locale facts through the public JDK API. Run by {@link SpiChainParityTest} in a forked JVM
 * with {@code -Djava.locale.providers=SPI} and this library on the system class path, i.e. the
 * way it is deployed, as opposed to the providers being called directly.
 */
public final class SpiChainParityProbe {

    private SpiChainParityProbe() {
    }

    public static void main(String[] args) {
        // Arguments are language tags whose own-language display name is printed as name.<tag>=...
        // with non-ASCII characters escaped, so the output does not depend on the console charset.
        for (String tag : args) {
            Locale locale = Locale.forLanguageTag(tag);
            print("name." + tag, escape(locale.getDisplayName(locale)));
        }

        Locale frFr = Locale.forLanguageTag("fr-FR");
        print("providers", System.getProperty("java.locale.providers"));
        print("fr_FR.group", hex(DecimalFormatSymbols.getInstance(frFr).getGroupingSeparator()));
        print("fr_BE.group", hex(DecimalFormatSymbols.getInstance(Locale.forLanguageTag("fr-BE")).getGroupingSeparator()));
        print("de_CH.group", hex(DecimalFormatSymbols.getInstance(Locale.forLanguageTag("de-CH")).getGroupingSeparator()));
        print("ar_AD.eur", Currency.getInstance("EUR").getSymbol(Locale.forLanguageTag("ar-AD")));
        print("ar.eur", Currency.getInstance("EUR").getSymbol(Locale.forLanguageTag("ar")));
        print("hi.currencyPattern", ((DecimalFormat) NumberFormat.getCurrencyInstance(Locale.forLanguageTag("hi"))).toPattern());
        print("zh_CN.firstDay", String.valueOf(Calendar.getInstance(Locale.forLanguageTag("zh-CN")).getFirstDayOfWeek()));
        print("af_ZA.january", java.text.DateFormatSymbols.getInstance(Locale.forLanguageTag("af-ZA")).getMonths()[0]);
        print("hebrew", Locale.forLanguageTag("he").getDisplayLanguage(Locale.US));
        print("ca_ES.tokyo", TimeZone.getTimeZone("Asia/Tokyo").getDisplayName(false, TimeZone.LONG, Locale.forLanguageTag("ca-ES")));
        print("compact", NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT).format(1234567));
        BreakIterator thai = BreakIterator.getWordInstance(Locale.forLanguageTag("th"));
        thai.setText("\u0e2a\u0e27\u0e31\u0e2a\u0e14\u0e35\u0e04\u0e23\u0e31\u0e1a");
        print("thai.firstBoundary", String.valueOf(thai.next()));
    }

    private static String escape(String value) {
        StringBuilder escaped = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c > 0x7e) {
                escaped.append(String.format("\\u%04X", (int) c));
            } else {
                escaped.append(c);
            }
        }
        return escaped.toString();
    }

    private static String hex(char c) {
        return String.format("U+%04X", (int) c);
    }

    private static void print(String key, String value) {
        System.out.println(key + "=" + value);
    }
}
