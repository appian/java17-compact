package com.appiancorp.jre17.compact;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.BreakIterator;
import java.text.spi.BreakIteratorProvider;
import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

class Java17CompactBreakIteratorProviderTest {

    private final BreakIteratorProvider provider = new Java17CompactBreakIteratorProvider();
    private final BreakIteratorProvider jreProvider = LocaleProviderAdapter.forJRE().getBreakIteratorProvider();

    @Test
    void getAvailableLocalesIsNonEmptyAndIncludesUs() {
        Locale[] locales = provider.getAvailableLocales();
        assertTrue(locales.length > 0);
        assertTrue(Arrays.asList(locales).contains(Locale.US));
    }

    @Test
    void wordInstanceSplitsOnWhitespace() {
        BreakIterator iterator = provider.getWordInstance(Locale.US);
        iterator.setText("Hello world");
        int first = iterator.first();
        int next = iterator.next();
        assertTrue(next > first);
        String firstWord = "Hello world".substring(first, next);
        assertTrue(firstWord.equals("Hello"));
    }

    @Test
    void sentenceInstanceSplitsOnPeriod() {
        BreakIterator iterator = provider.getSentenceInstance(Locale.US);
        String text = "First sentence. Second sentence.";
        iterator.setText(text);
        int first = iterator.first();
        int next = iterator.next();
        assertTrue(text.substring(first, next).trim().equals("First sentence."));
    }

    @Test
    void characterInstanceIteratesOneCodePointAtATime() {
        BreakIterator iterator = provider.getCharacterInstance(Locale.US);
        iterator.setText("ab");
        int first = iterator.first();
        int next = iterator.next();
        assertTrue(next - first == 1);
    }

    @Test
    void lineInstanceIsUsableForLineBreaking() {
        BreakIterator iterator = provider.getLineInstance(Locale.US);
        iterator.setText("Hello world, this is a test.");
        assertNotNull(iterator);
        int boundary = iterator.first();
        assertTrue(boundary == 0);
    }

    @Test
    void thaiLocaleUsesItsOwnRuleBasedBreakIteratorData() {
        // Thai is the one locale in this codebase with its own break-iterator
        // data -- see generateBreakIteratorDataTh in build.gradle.kts. Its word/line
        // break classes are dictionary-based and require the binary "thai_dict"
        // resource, which has not been ported into this repository, so this
        // exercises the character-break iterator instead, which for Thai is
        // rule-based (RuleBasedBreakIterator) and needs no such dictionary.
        BreakIterator iterator = provider.getCharacterInstance(Locale.forLanguageTag("th"));
        iterator.setText("\u0e2a\u0e27\u0e31\u0e2a\u0e14\u0e35");
        assertTrue(iterator.first() >= 0);
        assertTrue(iterator.next() > iterator.first());
    }

    @Test
    void nullLocaleThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> provider.getWordInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getLineInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getCharacterInstance(null));
        assertThrows(NullPointerException.class, () -> provider.getSentenceInstance(null));
    }

    @Test
    void everyAdvertisedLocaleMatchesJreForEveryIteratorKind() {
        JreLocaleProviderTestSupport.useRepackagedJreProvider();
        for (Locale locale : JreLocaleProviderTestSupport.sortedLocales(provider.getAvailableLocales())) {
            JreLocaleProviderTestSupport.assertBreakIteratorMatches("sentence", locale,
                () -> provider.getSentenceInstance(locale), () -> jreProvider.getSentenceInstance(locale));
            JreLocaleProviderTestSupport.assertBreakIteratorMatches("character", locale,
                () -> provider.getCharacterInstance(locale), () -> jreProvider.getCharacterInstance(locale));

            // The port intentionally lacks OpenJDK's thai_dict resource. The JRE
            // word/line iterators therefore fail at construction for Thai, while
            // the rule-based character/sentence iterators above remain comparable.
            if (!locale.getLanguage().equals("th")) {
                JreLocaleProviderTestSupport.assertBreakIteratorMatches("word", locale,
                    () -> provider.getWordInstance(locale), () -> jreProvider.getWordInstance(locale));
                JreLocaleProviderTestSupport.assertBreakIteratorMatches("line", locale,
                    () -> provider.getLineInstance(locale), () -> jreProvider.getLineInstance(locale));
            } else {
                    assertThrows(java.lang.InternalError.class,
                        () -> provider.getWordInstance(locale));
                assertThrows(java.lang.InternalError.class,
                    () -> provider.getLineInstance(locale));
            }
        }
    }
}
