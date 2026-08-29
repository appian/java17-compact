package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DecimalFormatSymbols;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

class Java17CompactDecimalFormatSymbolsProviderTest {

    private final DecimalFormatSymbolsProvider provider = new Java17CompactDecimalFormatSymbolsProvider();
    private final DecimalFormatSymbolsProvider jreProvider = LocaleProviderAdapter.forJRE().getDecimalFormatSymbolsProvider();

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
            DecimalFormatSymbols expected = jreProvider.getInstance(locale);
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

    @Test
    void everyAdvertisedLocaleMatchesAllJreDecimalSymbols() {
        JreLocaleProviderTestSupport.useRepackagedJreProvider();
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            DecimalFormatSymbols actual = provider.getInstance(locale);
            DecimalFormatSymbols expected = jreProvider.getInstance(locale);
            assertEquals(expected.getCurrency(), actual.getCurrency(), "currency for " + locale);
            assertEquals(expected.getCurrencySymbol(), actual.getCurrencySymbol(), "currency symbol for " + locale);
            assertEquals(expected.getInternationalCurrencySymbol(), actual.getInternationalCurrencySymbol(),
                "international currency for " + locale);
            assertEquals(expected.getDecimalSeparator(), actual.getDecimalSeparator(), "decimal for " + locale);
            assertEquals(expected.getGroupingSeparator(), actual.getGroupingSeparator(), "grouping for " + locale);
            assertEquals(expected.getMonetaryDecimalSeparator(), actual.getMonetaryDecimalSeparator(),
                "monetary decimal for " + locale);
            assertEquals(expected.getPercent(), actual.getPercent(), "percent for " + locale);
            assertEquals(expected.getPerMill(), actual.getPerMill(), "per mille for " + locale);
            assertEquals(expected.getZeroDigit(), actual.getZeroDigit(), "zero digit for " + locale);
            assertEquals(expected.getDigit(), actual.getDigit(), "digit for " + locale);
            assertEquals(expected.getPatternSeparator(), actual.getPatternSeparator(), "pattern separator for " + locale);
            assertEquals(expected.getMinusSign(), actual.getMinusSign(), "minus sign for " + locale);
            assertEquals(expected.getInfinity(), actual.getInfinity(), "infinity for " + locale);
            assertEquals(expected.getNaN(), actual.getNaN(), "NaN for " + locale);
            assertEquals(expected.getExponentSeparator(), actual.getExponentSeparator(), "exponent for " + locale);
        }
    }
}
