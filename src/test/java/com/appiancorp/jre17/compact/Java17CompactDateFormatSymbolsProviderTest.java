package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

class Java17CompactDateFormatSymbolsProviderTest {

    private final DateFormatSymbolsProvider provider = new Java17CompactDateFormatSymbolsProvider();
    private final DateFormatSymbolsProvider jreProvider = LocaleProviderAdapter.forJRE().getDateFormatSymbolsProvider();

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
            DateFormatSymbols expected = jreProvider.getInstance(locale);
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

    @Test
    void everyAdvertisedLocaleMatchesAllJreDateFormatSymbols() {
        JreLocaleProviderTestSupport.useRepackagedJreProvider();
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            DateFormatSymbols actual = provider.getInstance(locale);
            DateFormatSymbols expected = jreProvider.getInstance(locale);
            assertEquals(Arrays.asList(expected.getEras()), Arrays.asList(actual.getEras()), "eras for " + locale);
            assertEquals(Arrays.asList(expected.getMonths()), Arrays.asList(actual.getMonths()), "months for " + locale);
            assertEquals(Arrays.asList(expected.getShortMonths()), Arrays.asList(actual.getShortMonths()),
                "short months for " + locale);
            assertEquals(Arrays.asList(expected.getWeekdays()), Arrays.asList(actual.getWeekdays()),
                "weekdays for " + locale);
            assertEquals(Arrays.asList(expected.getShortWeekdays()), Arrays.asList(actual.getShortWeekdays()),
                "short weekdays for " + locale);
            assertEquals(Arrays.asList(expected.getAmPmStrings()), Arrays.asList(actual.getAmPmStrings()),
                "AM/PM for " + locale);
            assertEquals(Arrays.deepToString(expected.getZoneStrings()), Arrays.deepToString(actual.getZoneStrings()),
                "zone strings for " + locale);
            assertEquals(expected.getLocalPatternChars(), actual.getLocalPatternChars(),
                "pattern chars for " + locale);
        }
    }
}
