package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Locale;
import java.util.spi.CurrencyNameProvider;

import org.junit.jupiter.api.Test;

class Java17CompactCurrencyNameProviderTest {

    private final CurrencyNameProvider provider = new Java17CompactCurrencyNameProvider();

    // CurrencyNames is only ported for zh-HK and zh-SG in this repository (see
    // CurrencyNames_zh_HK.java / CurrencyNames_zh_SG.java) -- no "en"/"en-US",
    // "fr", or root/base bundle exists. Even zh-HK's own bundle reparents to
    // zh-TW for names it doesn't define directly, but no zh-TW bundle has been
    // ported either, so only the currency codes zh-HK defines directly
    // (HKD, TWD) are actually resolvable today.
    private static final Locale HONG_KONG = Locale.forLanguageTag("zh-HK");

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesHongKong() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(HONG_KONG));
    }

    @Test
    void hkdSymbolInHongKongLocaleThrowsBecauseParentChainIsIncomplete() {
        // CurrencyNames_zh_HK's constructor eagerly resolves its zh-TW parent
        // bundle (for reparenting) before getContents() is ever consulted, and
        // no CurrencyNames_zh_TW bundle has been ported, so even zh-HK's own
        // directly-defined entries (HKD, TWD) fail to resolve today.
        assertThrows(java.util.MissingResourceException.class, () -> provider.getSymbol("HKD", HONG_KONG));
    }

    @Test
    void twdSymbolInHongKongLocaleAlsoThrowsForTheSameReason() {
        assertThrows(java.util.MissingResourceException.class, () -> provider.getSymbol("TWD", HONG_KONG));
    }

    @Test
    void unknownCurrencyCodeInHongKongLocaleThrowsBecauseParentChainIsIncomplete() {
        // Fails the same way regardless of which currency code is requested,
        // since the failure happens during bundle construction, before any
        // code-specific lookup is attempted.
        assertThrows(java.util.MissingResourceException.class, () -> provider.getSymbol("ZZZ", HONG_KONG));
    }

    @Test
    void localeWithNoCurrencyNamesThrowsMissingResourceException() {
        // US has no ported CurrencyNames bundle and no root/base fallback either
        // (BaseLocaleDataMetaInfo's "CurrencyNames" entry is empty), so this
        // currently throws rather than falling back to Currency's own default
        // symbol -- this pins today's actual (incompletely-ported) behavior.
        assertThrows(java.util.MissingResourceException.class, () -> provider.getSymbol("USD", Locale.US));
    }

    @Test
    void nullArgumentsThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getSymbol(null, HONG_KONG));
        assertThrows(NullPointerException.class, () -> provider.getSymbol("USD", null));
    }
}
