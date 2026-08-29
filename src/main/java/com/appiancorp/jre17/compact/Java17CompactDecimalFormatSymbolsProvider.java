package com.appiancorp.jre17.compact;

import java.text.DecimalFormatSymbols;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Locale;

// stub implementation for jre17 compatibility
public class Java17CompactDecimalFormatSymbolsProvider extends DecimalFormatSymbolsProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public DecimalFormatSymbols getInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
