/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-10-09.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). End-to-end test of the deployed java.locale.providers=SPI configuration with this library on the system class path, checked against values captured from JDK 17 with JRE,SPI; documents why CLDR must not be configured next to this library.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * End-to-end check of the deployed configuration: this library on the system class path and
 * {@code -Djava.locale.providers=SPI}, queried through the public JDK API. The other tests call the
 * providers directly and so cannot see how the JDK selects between providers.
 *
 * <p>The library must be used with {@code SPI} and <em>without</em> {@code CLDR}. JDK 17's
 * {@code JRE,SPI} had no CLDR behind it, and a provider jar cannot win against CLDR in general:
 * the JDK asks the SPI first and CLDR second for every candidate of a locale, but selects the SPI
 * only for locales it lists exactly, so every locale the jar does not list (the bare language
 * {@code ar}, the country locale {@code ar_AD}, languages the JRE had no data for, ...) is answered
 * by CLDR. Likewise {@code Locale#getDisplayName} reads its join patterns from the first
 * resource-bundle-based adapter, which is CLDR whenever CLDR is configured. With {@code SPI} alone
 * none of that happens.
 *
 * <p>Expected values were captured from JDK 17 with {@code -Djava.locale.providers=JRE,SPI} (the
 * behavior this library reproduces). They intentionally differ from what CLDR returns.
 *
 * <p>Known remaining difference: the name of a Unicode {@code rg} extension type
 * ({@code fr_FR-u-rg-chzzzz}) is resolved by {@code Locale} through the resource-bundle-based
 * adapter (the JDK's built-in root data) instead of this library, so it is "Switzerland" instead of
 * JDK 17's "Suisse". A provider cannot change that.
 */
class SpiChainParityTest {

    @Test
    void publicApiReturnsJdk17JreDataNotCldr() throws Exception {
        Map<String, String> actual = runProbe();

        assertEquals("SPI", actual.get("providers"));
        // Grouping separators that CLDR changes (U+202F, U+2019, ...).
        assertEquals("U+00A0", actual.get("fr_FR.group"));
        assertEquals("U+002E", actual.get("fr_BE.group"));
        assertEquals("U+0027", actual.get("de_CH.group"));
        // Country locale the JRE has no bundle for, and bare languages that exist only per country.
        assertEquals("EUR", actual.get("ar_AD.eur"));
        assertEquals("EUR", actual.get("ar.eur"));
        assertEquals("\u00a4 #,##0.00", actual.get("hi.currencyPattern"));
        // The JDK looks up zh_Hans_CN before zh_CN.
        assertEquals("1", actual.get("zh_CN.firstDay"));
        // Languages without JRE data fall back to root (English) instead of CLDR translations.
        assertEquals("January", actual.get("af_ZA.january"));
        assertEquals("Japan Standard Time", actual.get("ca_ES.tokyo"));
        assertEquals("Hebrew", actual.get("hebrew"));
        // Missed in the first port: compact number format and the Thai word dictionary.
        assertEquals("1M", actual.get("compact"));
        assertEquals("6", actual.get("thai.firstBoundary"));
    }

    @Test
    void combinedDisplayNamesMatchJdk17ExactlyIncludingSeparators() throws Exception {
        // Names with two or more qualifiers are joined with patterns the JDK reads from its
        // resource-bundle-based adapter. With CLDR configured those come from CLDR (", ", full-width
        // and ideographic commas, "ca: japanese"); with SPI alone they match JDK 17.
        Map<String, String> expectedJdk17 = new LinkedHashMap<>();
        expectedJdk17.put("sr-Latn-RS", "Srpski (Latin,Srbija)");
        expectedJdk17.put("sr-Latn-BA", "Srpski (Latin,Bosna i Hercegovina)");
        expectedJdk17.put("sr-Latn-ME", "Srpski (Latin,Crna Gora)");
        expectedJdk17.put("zh-Hans-CN", "\u4E2D\u6587 (\u7B80\u4F53\u4E2D\u6587,\u4E2D\u56FD)");
        expectedJdk17.put("zh-Hans-SG", "\u4E2D\u6587 (\u7B80\u4F53\u4E2D\u6587,\u65B0\u52A0\u5761)");
        expectedJdk17.put("zh-Hant-HK", "\u4E2D\u6587 (\u7E41\u9AD4\u4E2D\u6587,\u9999\u6E2F)");
        expectedJdk17.put("zh-Hant-TW", "\u4E2D\u6587 (\u7E41\u9AD4\u4E2D\u6587,\u53F0\u7063)");
        expectedJdk17.put("ja-JP-u-ca-japanese-x-lvariant-JP",
            "\u65E5\u672C\u8A9E (\u65E5\u672C,JP,ca:japanese)");
        expectedJdk17.put("th-TH-u-nu-thai-x-lvariant-TH",
            "\u0E44\u0E17\u0E22 (\u0E1B\u0E23\u0E30\u0E40\u0E17\u0E28\u0E44\u0E17\u0E22,TH,nu:thai)");

        Map<String, String> actual = runProbe(expectedJdk17.keySet().toArray(new String[0]));

        for (Map.Entry<String, String> entry : expectedJdk17.entrySet()) {
            assertEquals(entry.getValue(), unescape(actual.get("name." + entry.getKey())),
                "display name for " + entry.getKey());
        }
    }

    private static String unescape(String escaped) {
        Matcher matcher = Pattern.compile("\\\\u([0-9A-Fa-f]{4})").matcher(escaped);
        StringBuilder out = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(out,
                Matcher.quoteReplacement(String.valueOf((char) Integer.parseInt(matcher.group(1), 16))));
        }
        matcher.appendTail(out);
        return out.toString();
    }

    private static Map<String, String> runProbe(String... probeArguments) throws IOException, InterruptedException {
        String java = new File(System.getProperty("java.home"), "bin/java").getPath();
        List<String> command = new ArrayList<>(List.of(java,
            "-Djava.locale.providers=SPI",
            "-Djava.locale.useOldISOCodes=true",
            "-Dfile.encoding=UTF-8",
            "-cp", System.getProperty("java.class.path"),
            SpiChainParityProbe.class.getName()));
        command.addAll(List.of(probeArguments));
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        if (!process.waitFor(120, TimeUnit.SECONDS) || process.exitValue() != 0) {
            throw new AssertionError("probe JVM failed:\n" + output);
        }
        Map<String, String> values = new HashMap<>();
        for (String line : output.split("\\R")) {
            int separator = line.indexOf('=');
            if (separator > 0) {
                values.put(line.substring(0, separator), line.substring(separator + 1));
            }
        }
        return values;
    }
}
