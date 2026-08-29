package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class Java17CompactDateFormatSymbolsProviderTest {

    private final DateFormatSymbolsProvider provider = new Java17CompactDateFormatSymbolsProvider();

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesUs() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.US));
    }

    @Test
    void usInstanceHasExpectedMonthNames() {
        DateFormatSymbols symbols = provider.getInstance(Locale.US);
        assertEquals("January", symbols.getMonths()[0]);
        assertEquals("December", symbols.getMonths()[11]);
    }

    @Test
    void usInstanceHasExpectedAmPmStrings() {
        DateFormatSymbols symbols = provider.getInstance(Locale.US);
        assertEquals("AM", symbols.getAmPmStrings()[0]);
        assertEquals("PM", symbols.getAmPmStrings()[1]);
    }

    @Test
    void frenchInstanceUsesFrenchMonthNames() {
        DateFormatSymbols symbols = provider.getInstance(Locale.FRENCH);
        assertEquals("janvier", symbols.getMonths()[0]);
    }

    @Test
    void matchesRealJdkInstanceForMultipleLocales() {
        Locale[] samples = { Locale.US, Locale.FRANCE, Locale.GERMANY, Locale.JAPAN };
        for (Locale locale : samples) {
            DateFormatSymbols expected = DateFormatSymbols.getInstance(locale);
            DateFormatSymbols actual = provider.getInstance(locale);
            assertEquals(Arrays.asList(expected.getMonths()), Arrays.asList(actual.getMonths()),
                "month names mismatch for " + locale);
            assertEquals(Arrays.asList(expected.getShortWeekdays()), Arrays.asList(actual.getShortWeekdays()),
                "short weekday names mismatch for " + locale);
        }
    }

    @Test
    void nullLocaleThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getInstance(null));
    }

    @Test
    void returnsNewInstanceEachCall() {
        // DateFormatSymbols is mutable, so callers must not share a cached instance.
        DateFormatSymbols first = provider.getInstance(Locale.US);
        DateFormatSymbols second = provider.getInstance(Locale.US);
        assertEquals(first, second);
        first.setAmPmStrings(new String[] { "MUTATED", "MUTATED" });
        assertEquals("AM", second.getAmPmStrings()[0]);
    }
}
