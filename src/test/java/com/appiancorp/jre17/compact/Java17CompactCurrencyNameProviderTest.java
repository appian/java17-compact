package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Locale;
import java.util.spi.CurrencyNameProvider;

import org.junit.jupiter.api.Test;

// The full set of JDK 17 JRE/COMPAT CurrencyNames resource bundles is now ported
// (CurrencyNames*.properties compiled to ListResourceBundle subclasses, plus the
// en/en-US/root base bundles), so currency symbols and display names resolve
// through the complete parent chain, matching java.locale.providers=COMPAT on
// JDK 17. Expected values were captured from JDK 17 with COMPAT enabled.
class Java17CompactCurrencyNameProviderTest {

    private final CurrencyNameProvider provider = new Java17CompactCurrencyNameProvider();

    private static final Locale HONG_KONG = Locale.forLanguageTag("zh-HK");

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesHongKong() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(HONG_KONG));
    }

    @Test
    void hkdSymbolInHongKongLocaleResolves() {
        assertEquals("HK$", provider.getSymbol("HKD", HONG_KONG));
    }

    @Test
    void twdSymbolInHongKongLocaleResolves() {
        assertEquals("TWD", provider.getSymbol("TWD", HONG_KONG));
    }

    @Test
    void unknownCurrencyCodeReturnsNull() {
        // An unknown currency code resolves to null (it does not throw), so the
        // JDK can fall back to the default symbol.
        assertNull(provider.getSymbol("ZZZ", HONG_KONG));
    }

    @Test
    void usdSymbolAndDisplayNameResolveInBaseLocales() {
        assertEquals("$", provider.getSymbol("USD", Locale.US));
        assertEquals("US Dollar", provider.getDisplayName("USD", Locale.US));
        assertEquals("dollar des \u00c9tats-Unis", provider.getDisplayName("USD", Locale.FRENCH));
    }

    @Test
    void nullArgumentsThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getSymbol(null, HONG_KONG));
        assertThrows(NullPointerException.class, () -> provider.getSymbol("USD", null));
    }

    @Test
    void unknownCurrencyCodeResolvesToNullForEveryAdvertisedLocale() {
        // Across every advertised locale, an unknown code uniformly resolves to
        // null for both symbol and display-name lookups (never throwing).
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            assertNull(provider.getSymbol("ZZZ", locale), "symbol for " + locale);
            assertNull(provider.getDisplayName("ZZZ", locale), "display name for " + locale);
        }
    }
}
