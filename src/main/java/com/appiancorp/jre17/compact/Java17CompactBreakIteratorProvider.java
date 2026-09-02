package com.appiancorp.jre17.compact;

import java.text.BreakIterator;
import java.text.spi.BreakIteratorProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactBreakIteratorProvider extends BreakIteratorProvider {

  private final BreakIteratorProvider delegate = LocaleProviderAdapter.forJRE().getBreakIteratorProvider();

  // Broadened to include country variants so this provider is selected (ahead of
  // CLDR) for country locales under java.locale.providers=SPI,CLDR. See CompactLocaleSupport.
  private final Locale[] availableLocales = CompactLocaleSupport.withCountryVariants(delegate.getAvailableLocales());

  @Override
  public Locale[] getAvailableLocales() {
    return availableLocales.clone();
  }

  @Override
  public boolean isSupportedLocale(Locale locale) {
    return CompactLocaleSupport.isSupportedLocale(delegate, locale);
  }

  @Override
  public BreakIterator getWordInstance(Locale locale) {
    return delegate.getWordInstance(locale);
  }

  @Override
  public BreakIterator getLineInstance(Locale locale) {
    return delegate.getLineInstance(locale);
  }

  @Override
  public BreakIterator getCharacterInstance(Locale locale) {
    return delegate.getCharacterInstance(locale);
  }

  @Override
  public BreakIterator getSentenceInstance(Locale locale) {
    return delegate.getSentenceInstance(locale);
  }
}
