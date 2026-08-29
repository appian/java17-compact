package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/** Shared assertions for the repackaged JRE locale-provider stack. */
final class JreLocaleProviderTestSupport {

    private JreLocaleProviderTestSupport() {
    }

    static void useRepackagedJreProvider() {
        // The compact providers select the repackaged JRE stack directly. This
        // intentionally does not mutate java.locale.providers, which controls
        // unrelated JVM-global public facade selection.
    }

    static List<Locale> sortedLocales(Locale[] locales) {
        List<Locale> result = new ArrayList<>(Arrays.asList(locales));
        result.sort(Comparator.comparing(Locale::toLanguageTag));
        return result;
    }

    static void assertBreakIteratorMatches(String operation, Locale locale,
                                           Supplier<BreakIterator> compactSupplier,
                                           Supplier<BreakIterator> jreSupplier) {
        BreakIterator compact = compactSupplier.get();
        BreakIterator jre = jreSupplier.get();
        assertNotNull(compact, operation + " compact iterator for " + locale);
        assertNotNull(jre, operation + " JRE iterator for " + locale);

        String[] texts = {
            "Hello, world! This is a test.",
            "A\u0301BC \uD83D\uDE00\uD83D\uDC4D",
            "one\ntwo\r\nthree",
            "\u4E2D\u6587\u3002\u65E5\u672C\u8A9E",
            "\u0627\u0644\u0639\u0631\u0628\u064A\u0629",
            "muthu palaniyappan olagappan is awesome"
        };
        for (String text : texts) {
            assertArrayEquals(boundaries(compact, text), boundaries(jre, text),
                operation + " boundary mismatch for " + locale + " text=" + text);
        }
    }

    static int[] boundaries(BreakIterator iterator, String text) {
        iterator.setText(text);
        int[] result = new int[text.length() + 1];
        int count = 0;
        for (int boundary = iterator.first(); boundary != BreakIterator.DONE;
             boundary = iterator.next()) {
            result[count++] = boundary;
        }
        return Arrays.copyOf(result, count);
    }

    static <T> void assertSame(String description, T expected, T actual) {
        assertEquals(expected, actual, description);
    }
}
