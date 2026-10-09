/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-08-29.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). Tests Java17CompactNumberFormatProvider against the repackaged JDK 17 JRE/COMPAT implementation for every locale the JRE data ships, plus the behavior specific to this provider.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

class Java17CompactNumberFormatProviderTest {

    private final NumberFormatProvider provider = new Java17CompactNumberFormatProvider();
    private final NumberFormatProvider jreProvider = LocaleProviderAdapter.forJRE().getNumberFormatProvider();

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesUs() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.US));
    }

    @Test
    void usNumberInstanceUsesCommaGrouping() {
        NumberFormat format = provider.getNumberInstance(Locale.US);
        assertEquals("1,234.5", format.format(1234.5));
    }

    @Test
    void usCurrencyInstanceUsesDollarSign() {
        NumberFormat format = provider.getCurrencyInstance(Locale.US);
        assertEquals("$1,234.50", format.format(1234.5));
    }

    @Test
    void usPercentInstanceAppendsPercentSign() {
        NumberFormat format = provider.getPercentInstance(Locale.US);
        assertEquals("50%", format.format(0.5));
    }

    @Test
    void usIntegerInstanceRoundsToNearestWithHalfEven() {
        NumberFormat format = provider.getIntegerInstance(Locale.US);
        assertEquals("2", format.format(2.5));
        assertEquals("2", format.format(1.5));
        assertTrue(format.isParseIntegerOnly());
    }

    @Test
    void frenchNumberInstanceUsesCommaDecimalSeparator() {
        NumberFormat format = provider.getNumberInstance(Locale.FRANCE);
        // French uses a non-breaking space for grouping and comma for the decimal
        // point; only assert the decimal separator to avoid brittleness around the
        // exact grouping character (narrow no-break space vs. regular space across
        // JDK/CLDR versions).
        assertTrue(format.format(1234.5).contains(","));
    }

    @Test
    void compactNumberInstanceIsSupported() {
        // NumberFormatProvider#getCompactNumberInstance throws UnsupportedOperationException
        // unless overridden; JDK 17's JRE provider supported it.
        for (Locale locale : new Locale[] { Locale.US, Locale.FRANCE, Locale.GERMANY, Locale.JAPAN }) {
            for (NumberFormat.Style style : NumberFormat.Style.values()) {
                NumberFormat actual = provider.getCompactNumberInstance(locale, style);
                NumberFormat expected = jreProvider.getCompactNumberInstance(locale, style);
                assertEquals(expected.format(1234567), actual.format(1234567), style + " for " + locale);
            }
        }
        assertEquals("1M", provider.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT).format(1234567));
    }

    @Test
    void nullLocaleThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getNumberInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getCurrencyInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getPercentInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getIntegerInstance(null));
    }

    @Test
    void everyAdvertisedLocaleAndNumberStyleMatchesJre() {
        JreLocaleProviderTestSupport.useRepackagedJreProvider();
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            assertNumberFormatMatches("number", locale, provider.getNumberInstance(locale),
                jreProvider.getNumberInstance(locale));
            assertNumberFormatMatches("currency", locale, provider.getCurrencyInstance(locale),
                jreProvider.getCurrencyInstance(locale));
            assertNumberFormatMatches("percent", locale, provider.getPercentInstance(locale),
                jreProvider.getPercentInstance(locale));
            assertNumberFormatMatches("integer", locale, provider.getIntegerInstance(locale),
                jreProvider.getIntegerInstance(locale));
        }
    }

    private static void assertNumberFormatMatches(String kind, Locale locale,
                                                   NumberFormat actual, NumberFormat expected) {
        Object[] values = { 0, 1, 1.5, 2.5, 1234.567, -0.25, Double.NaN,
            Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY };
        for (Object value : values) {
            assertEquals(expected.format(value), actual.format(value),
                kind + " formatting mismatch for " + locale + " value=" + value);
        }
        assertEquals(expected.getMaximumIntegerDigits(), actual.getMaximumIntegerDigits(), kind + " max integer for " + locale);
        assertEquals(expected.getMinimumIntegerDigits(), actual.getMinimumIntegerDigits(), kind + " min integer for " + locale);
        assertEquals(expected.getMaximumFractionDigits(), actual.getMaximumFractionDigits(), kind + " max fraction for " + locale);
        assertEquals(expected.getMinimumFractionDigits(), actual.getMinimumFractionDigits(), kind + " min fraction for " + locale);
        assertEquals(expected.isGroupingUsed(), actual.isGroupingUsed(), kind + " grouping for " + locale);
        assertEquals(expected.isParseIntegerOnly(), actual.isParseIntegerOnly(), kind + " integer-only for " + locale);
        assertEquals(expected.getRoundingMode(), actual.getRoundingMode(), kind + " rounding for " + locale);
        assertEquals(expected.getCurrency(), actual.getCurrency(), kind + " currency for " + locale);
    }
}
