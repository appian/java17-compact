/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-08-29.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). Tests Java17CompactLocaleNameProvider against the repackaged JDK 17 JRE/COMPAT implementation for every locale the JRE data ships, plus the behavior specific to this provider.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
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

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// The full set of JDK 17 JRE/COMPAT LocaleNames resource bundles is now ported
// (CurrencyNames/LocaleNames/CalendarData .properties compiled to
// ListResourceBundle subclasses, plus the en/en-US/root base bundles), so
// locale/country/language names resolve through the complete parent chain,
// matching java.locale.providers=COMPAT on JDK 17. Expected values below were
// captured from JDK 17 running with -Djava.locale.providers=COMPAT.
class Java17CompactLocaleNameProviderTest {

    private final LocaleNameProvider provider = new Java17CompactLocaleNameProvider();
    private final LocaleNameProvider jreProvider = LocaleProviderAdapter.forJRE().getLocaleNameProvider();

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
    void unicodeExtensionNamesMatchJre() {
        assertEquals(jreProvider.getDisplayUnicodeExtensionKey("ca", Locale.US),
            provider.getDisplayUnicodeExtensionKey("ca", Locale.US));
        assertEquals(jreProvider.getDisplayUnicodeExtensionType("japanese", "ca", Locale.US),
            provider.getDisplayUnicodeExtensionType("japanese", "ca", Locale.US));
    }

    @Test
    void displayScriptMatchesJre() {
        // LocaleNameProvider#getDisplayScript defaults to null; without the override, script
        // names ("Latin", "Simplified Han") are not answered at all.
        assertEquals(jreProvider.getDisplayScript("Latn", Locale.US), provider.getDisplayScript("Latn", Locale.US));
        assertEquals(jreProvider.getDisplayScript("Hans", Locale.CHINA), provider.getDisplayScript("Hans", Locale.CHINA));
        assertNotNull(provider.getDisplayScript("Latn", Locale.US));
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
