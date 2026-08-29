package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.CurrencyNameProvider;

// stub implementation for jre17 compatibility
public class Java17CompactCurrencyNameProvider extends CurrencyNameProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public String getSymbol(String currencyCode, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
