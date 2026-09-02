package com.appiancorp.jre17.compact;

import java.text.DateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactDateFormatProvider extends DateFormatProvider {

  private final DateFormatProvider delegate = LocaleProviderAdapter.forJRE().getDateFormatProvider();

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
  public DateFormat getTimeInstance(int style, Locale locale) {
    return delegate.getTimeInstance(style, locale);
  }

  @Override
  public DateFormat getDateInstance(int style, Locale locale) {
    return delegate.getDateInstance(style, locale);
  }

  @Override
  public DateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale) {
    return delegate.getDateTimeInstance(dateStyle, timeStyle, locale);
  }
}
