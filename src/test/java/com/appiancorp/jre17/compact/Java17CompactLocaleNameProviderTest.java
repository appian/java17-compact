package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Locale;
import java.util.spi.LocaleNameProvider;

import org.junit.jupiter.api.Test;

class Java17CompactLocaleNameProviderTest {

    private final LocaleNameProvider provider = new Java17CompactLocaleNameProvider();

    // LocaleNames is only ported for zh-HK in this repository (see
    // LocaleNames_zh_HK.java) -- no "en"/"en-US" or "fr" bundle exists. zh-HK's
    // own bundle defines no entries directly (getContents() returns {}) and
    // reparents to zh-TW, but no zh-TW LocaleNames bundle has been ported
    // either, so no locale/country/language name is actually resolvable today.
    private static final Locale HONG_KONG = Locale.forLanguageTag("zh-HK");

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesHongKong() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(HONG_KONG));
    }

    @Test
    void displayCountryInHongKongLocaleThrowsBecauseParentChainIsIncomplete() {
        // zh-HK's bundle defines no entries directly and its zh-TW parent bundle
        // has not been ported, so resolution fails today even though zh-HK is
        // listed as an "available" locale for this category.
        assertThrows(java.util.MissingResourceException.class, () -> provider.getDisplayCountry("FR", HONG_KONG));
    }

    @Test
    void displayLanguageInHongKongLocaleThrowsBecauseParentChainIsIncomplete() {
        assertThrows(java.util.MissingResourceException.class, () -> provider.getDisplayLanguage("fr", HONG_KONG));
    }

    @Test
    void displayVariantInHongKongLocaleThrowsBecauseParentChainIsIncomplete() {
        assertThrows(java.util.MissingResourceException.class,
            () -> provider.getDisplayVariant("SOME_VARIANT", HONG_KONG));
    }

    @Test
    void localeWithNoLocaleNamesThrowsMissingResourceException() {
        // US has no ported LocaleNames bundle and no root/base fallback either
        // (BaseLocaleDataMetaInfo's "LocaleNames" entry is empty), so this
        // currently throws rather than falling back to a built-in default --
        // this pins today's actual (incompletely-ported) behavior.
        assertThrows(java.util.MissingResourceException.class, () -> provider.getDisplayLanguage("fr", Locale.US));
    }

    @Test
    void nullArgumentsThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getDisplayLanguage(null, HONG_KONG));
        assertThrows(NullPointerException.class, () -> provider.getDisplayLanguage("fr", null));
        assertThrows(NullPointerException.class, () -> provider.getDisplayCountry(null, HONG_KONG));
        assertThrows(NullPointerException.class, () -> provider.getDisplayCountry("FR", null));
    }

    @Test
    void everyAdvertisedLocaleHasTheKnownSparseDataBehaviorForAllMethods() {
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            assertThrows(java.util.MissingResourceException.class,
                () -> provider.getDisplayLanguage("fr", locale), "language for " + locale);
            assertThrows(java.util.MissingResourceException.class,
                () -> provider.getDisplayCountry("FR", locale), "country for " + locale);
            assertThrows(java.util.MissingResourceException.class,
                () -> provider.getDisplayVariant("VARIANT", locale), "variant for " + locale);
        }
    }

    @Test
    void nullVariantRetainsTheJreProviderLookupBehavior() {
        assertThrows(java.util.MissingResourceException.class,
            () -> provider.getDisplayVariant(null, HONG_KONG));
    }
}
