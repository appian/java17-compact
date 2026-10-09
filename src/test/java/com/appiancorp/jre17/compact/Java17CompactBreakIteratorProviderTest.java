/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-08-29.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). Tests Java17CompactBreakIteratorProvider against the repackaged JDK 17 JRE/COMPAT implementation for every locale the JRE data ships, plus the behavior specific to this provider. Includes the Thai dictionary-based word and line iterators.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.BreakIterator;
import java.text.spi.BreakIteratorProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

class Java17CompactBreakIteratorProviderTest {

    private final BreakIteratorProvider provider = new Java17CompactBreakIteratorProvider();
    private final BreakIteratorProvider jreProvider = LocaleProviderAdapter.forJRE().getBreakIteratorProvider();

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesUs() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.US));
    }

    @Test
    void wordInstanceSplitsOnWhitespace() {
        BreakIterator iterator = provider.getWordInstance(Locale.US);
        iterator.setText("Hello world");
        int first = iterator.first();
        int next = iterator.next();
        assertTrue(next > first);
        String firstWord = "Hello world".substring(first, next);
        assertTrue(firstWord.equals("Hello"));
    }

    @Test
    void sentenceInstanceSplitsOnPeriod() {
        BreakIterator iterator = provider.getSentenceInstance(Locale.US);
        String text = "First sentence. Second sentence.";
        iterator.setText(text);
        int first = iterator.first();
        int next = iterator.next();
        assertTrue(text.substring(first, next).trim().equals("First sentence."));
    }

    @Test
    void characterInstanceIteratesOneCodePointAtATime() {
        BreakIterator iterator = provider.getCharacterInstance(Locale.US);
        iterator.setText("ab");
        int first = iterator.first();
        int next = iterator.next();
        assertTrue(next - first == 1);
    }

    @Test
    void lineInstanceIsUsableForLineBreaking() {
        BreakIterator iterator = provider.getLineInstance(Locale.US);
        iterator.setText("Hello world, this is a test.");
        assertNotNull(iterator);
        int boundary = iterator.first();
        assertTrue(boundary == 0);
    }

    @Test
    void thaiLocaleUsesItsOwnDictionaryBasedBreakIteratorData() {
        // Thai is the one locale with its own break-iterator data -- see
        // generateBreakIteratorDataTh in build.gradle.kts. Its word/line iterators are
        // dictionary-based and need the binary "thai_dict" resource; without it they failed with
        // InternalError("Can't load .../ext/thai_dict"). Expected boundaries were captured from
        // JDK 17 with java.locale.providers=JRE.
        Locale thai = Locale.forLanguageTag("th");
        String text = "Hello, w\u00f6rld! \u0e2a\u0e27\u0e31\u0e2a\u0e14\u0e35\u0e04\u0e23\u0e31\u0e1a"
            + " \u65e5\u672c\u8a9e it's 3.14";
        BreakIterator word = provider.getWordInstance(thai);
        word.setText(text);
        List<Integer> boundaries = new ArrayList<>();
        for (int b = word.next(); b != BreakIterator.DONE; b = word.next()) {
            boundaries.add(b);
        }
        assertEquals(List.of(5, 6, 7, 12, 13, 14, 20, 24, 25, 28, 29, 33, 34, 38), boundaries);
        assertNotNull(provider.getLineInstance(thai));
        assertNotNull(provider.getCharacterInstance(thai));
    }

    @Test
    void nullLocaleThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getWordInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getLineInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getCharacterInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getSentenceInstance(null));
    }

    @Test
    void everyAdvertisedLocaleMatchesJreForEveryIteratorKind() {
        JreLocaleProviderTestSupport.useRepackagedJreProvider();
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            JreLocaleProviderTestSupport.assertBreakIteratorMatches("sentence", locale,
                () -> provider.getSentenceInstance(locale), () -> jreProvider.getSentenceInstance(locale));
            JreLocaleProviderTestSupport.assertBreakIteratorMatches("character", locale,
                () -> provider.getCharacterInstance(locale), () -> jreProvider.getCharacterInstance(locale));

            JreLocaleProviderTestSupport.assertBreakIteratorMatches("word", locale,
                () -> provider.getWordInstance(locale), () -> jreProvider.getWordInstance(locale));
            JreLocaleProviderTestSupport.assertBreakIteratorMatches("line", locale,
                () -> provider.getLineInstance(locale), () -> jreProvider.getLineInstance(locale));
        }
    }
}
