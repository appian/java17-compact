package com.appiancorp.jre17.compact;

import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactDateFormatSymbolsProvider extends DateFormatSymbolsProvider {

  private final DateFormatSymbolsProvider delegate = LocaleProviderAdapter.forJRE().getDateFormatSymbolsProvider();

  @Override
  public Locale[] getAvailableLocales() {
    return delegate.getAvailableLocales();
  }

  @Override
  public DateFormatSymbols getInstance(Locale locale) {
    return delegate.getInstance(locale);
  }
}
