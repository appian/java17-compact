package com.appiancorp.jre17.compact;

import java.text.DecimalFormatSymbols;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Locale;

import com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider.LocaleProviderAdapter;

// Delegates to the repackaged JRE locale provider stack (see
// com.appiancorp.jre17.compact.thirdparty.sun.util.locale.provider) instead
// of the real JRE-internal implementation, which JDK 17 no longer ships.
public class Java17CompactDecimalFormatSymbolsProvider extends DecimalFormatSymbolsProvider {

  private final DecimalFormatSymbolsProvider delegate = LocaleProviderAdapter.forJRE().getDecimalFormatSymbolsProvider();

  @Override
  public Locale[] getAvailableLocales() {
    return delegate.getAvailableLocales();
  }

  @Override
  public DecimalFormatSymbols getInstance(Locale locale) {
    return delegate.getInstance(locale);
  }
}
