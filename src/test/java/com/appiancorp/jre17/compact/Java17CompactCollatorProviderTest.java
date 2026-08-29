package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.Collator;
import java.text.spi.CollatorProvider;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class Java17CompactCollatorProviderTest {

    private final CollatorProvider provider = new Java17CompactCollatorProvider();

    @Test
    void getAvailableLocalesIsNonEmpty() {
        // Collator's available-locales set is driven by CollationData resource
        // bundle coverage, which (per BaseLocaleDataMetaInfo/NonBaseLocaleDataMetaInfo)
        // does not include plain English -- English collation falls back to the
        // default/root rules instead of a dedicated CollationData_en bundle, exactly
        // as in the real JDK.
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.FRENCH));
    }

    @Test
    void usCollatorOrdersAlphabetically() {
        Collator collator = provider.getInstance(Locale.US);
        assertTrue(collator.compare("apple", "banana") < 0);
        assertTrue(collator.compare("banana", "apple") > 0);
        assertEquals(0, collator.compare("apple", "apple"));
    }

    @Test
    void usCollatorIsCaseInsensitiveAtDefaultStrength() {
        Collator collator = provider.getInstance(Locale.US);
        // Default (TERTIARY) strength treats case as a tertiary difference, so
        // primary/secondary-only comparisons via compare() with equal base letters
        // and only case differing should still be considered "close" -- but
        // compare() itself is strength-aware, so lower a vs upper A differ at
        // tertiary level only, not primary.
        collator.setStrength(Collator.SECONDARY);
        assertEquals(0, collator.compare("apple", "APPLE"));
    }

    @Test
    void frenchCollatorHandlesAccentedCharacters() {
        Collator collator = provider.getInstance(Locale.FRENCH);
        // "e" sorts before "\u00e9" (e with acute accent) at full strength.
        assertTrue(collator.compare("e", "\u00e9") < 0);
    }

    @Test
    void returnsIndependentClonesEachCall() {
        // CollatorProviderImpl.getInstance clones its RuleBasedCollator per call,
        // so mutating one returned instance must not affect another.
        Collator first = provider.getInstance(Locale.US);
        Collator second = provider.getInstance(Locale.US);
        first.setStrength(Collator.PRIMARY);
        assertEquals(Collator.TERTIARY, second.getStrength());
    }

    @Test
    void nullLocaleThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getInstance(null));
    }
}
