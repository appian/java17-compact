package com.appiancorp.jre17.compact;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactNumberFormatProvider extends NumberFormatProvider {

  private final NumberFormatProvider delegate = LocaleProviderAdapter.forJRE().getNumberFormatProvider();

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
  public NumberFormat getCurrencyInstance(Locale locale) {
    return delegate.getCurrencyInstance(locale);
  }

  @Override
  public NumberFormat getIntegerInstance(Locale locale) {
    return delegate.getIntegerInstance(locale);
  }

  @Override
  public NumberFormat getNumberInstance(Locale locale) {
    return delegate.getNumberInstance(locale);
  }

  @Override
  public NumberFormat getPercentInstance(Locale locale) {
    return delegate.getPercentInstance(locale);
  }
}
