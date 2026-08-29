package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DecimalFormatSymbols;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class Java17CompactDecimalFormatSymbolsProviderTest {

    private final DecimalFormatSymbolsProvider provider = new Java17CompactDecimalFormatSymbolsProvider();

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesUs() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.US));
    }

    @Test
    void usInstanceUsesDotDecimalSeparator() {
        DecimalFormatSymbols symbols = provider.getInstance(Locale.US);
        assertEquals('.', symbols.getDecimalSeparator());
        assertEquals(',', symbols.getGroupingSeparator());
    }

    @Test
    void frenchInstanceUsesCommaDecimalSeparator() {
        DecimalFormatSymbols symbols = provider.getInstance(Locale.FRANCE);
        assertEquals(',', symbols.getDecimalSeparator());
    }

    @Test
    void matchesRealJdkInstanceForMultipleLocales() {
        Locale[] samples = { Locale.US, Locale.FRANCE, Locale.GERMANY, Locale.JAPAN };
        for (Locale locale : samples) {
            DecimalFormatSymbols expected = DecimalFormatSymbols.getInstance(locale);
            DecimalFormatSymbols actual = provider.getInstance(locale);
            assertEquals(expected.getDecimalSeparator(), actual.getDecimalSeparator(), "decimal sep mismatch for " + locale);
            assertEquals(expected.getGroupingSeparator(), actual.getGroupingSeparator(), "grouping sep mismatch for " + locale);
            assertEquals(expected.getCurrencySymbol(), actual.getCurrencySymbol(), "currency symbol mismatch for " + locale);
        }
    }

    @Test
    void nullLocaleThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getInstance(null));
    }

    @Test
    void returnsMutableIndependentInstances() {
        DecimalFormatSymbols first = provider.getInstance(Locale.US);
        first.setDecimalSeparator('X');
        DecimalFormatSymbols second = provider.getInstance(Locale.US);
        assertEquals('.', second.getDecimalSeparator());
    }
}
