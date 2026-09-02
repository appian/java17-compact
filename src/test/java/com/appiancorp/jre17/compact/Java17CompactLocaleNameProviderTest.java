package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Locale;
import java.util.spi.LocaleNameProvider;

import org.junit.jupiter.api.Test;

// The full set of JDK 17 JRE/COMPAT LocaleNames resource bundles is now ported
// (CurrencyNames/LocaleNames/CalendarData .properties compiled to
// ListResourceBundle subclasses, plus the en/en-US/root base bundles), so
// locale/country/language names resolve through the complete parent chain,
// matching java.locale.providers=COMPAT on JDK 17. Expected values below were
// captured from JDK 17 running with -Djava.locale.providers=COMPAT.
class Java17CompactLocaleNameProviderTest {

    private final LocaleNameProvider provider = new Java17CompactLocaleNameProvider();

    private static final Locale HONG_KONG = Locale.forLanguageTag("zh-HK");

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesHongKong() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(HONG_KONG));
    }

    @Test
    void displayCountryInHongKongLocaleResolvesThroughParentChain() {
        // zh-HK reparents to zh-TW; the country name resolves to the Traditional
        // Chinese form, matching JDK 17 COMPAT.
        assertEquals("\u6cd5\u570b", provider.getDisplayCountry("FR", HONG_KONG));
    }

    @Test
    void displayLanguageInHongKongLocaleResolvesThroughParentChain() {
        assertEquals("\u6cd5\u6587", provider.getDisplayLanguage("fr", HONG_KONG));
    }

    @Test
    void displayVariantWithUnknownKeyReturnsNull() {
        // JRE/COMPAT has no variant display names; an unknown variant key
        // resolves to null (it does not throw).
        assertNull(provider.getDisplayVariant("SOME_VARIANT", HONG_KONG));
    }

    @Test
    void displayNamesInUsLocaleResolveToEnglish() {
        // en/en-US base bundles are now present, so English names resolve.
        assertEquals("French", provider.getDisplayLanguage("fr", Locale.US));
        assertEquals("France", provider.getDisplayCountry("FR", Locale.US));
    }

    @Test
    void nullArgumentsThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getDisplayLanguage(null, HONG_KONG));
        assertThrows(NullPointerException.class, () -> provider.getDisplayLanguage("fr", null));
        assertThrows(NullPointerException.class, () -> provider.getDisplayCountry(null, HONG_KONG));
        assertThrows(NullPointerException.class, () -> provider.getDisplayCountry("FR", null));
    }

    @Test
    void everyAdvertisedLocaleResolvesLanguageAndCountryNames() {
        // With the complete bundle set, every advertised locale resolves a
        // (non-null) language and country name via its parent chain, while an
        // unknown variant key uniformly resolves to null.
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            assertNotNull(provider.getDisplayLanguage("fr", locale), "language for " + locale);
            assertNotNull(provider.getDisplayCountry("FR", locale), "country for " + locale);
            assertNull(provider.getDisplayVariant("VARIANT", locale), "variant for " + locale);
        }
    }

    @Test
    void nullVariantReturnsNull() {
        // A null variant key resolves to null, matching the JRE provider.
        assertNull(provider.getDisplayVariant(null, HONG_KONG));
    }
}
