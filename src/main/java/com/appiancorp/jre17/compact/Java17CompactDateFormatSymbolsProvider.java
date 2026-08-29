package com.appiancorp.jre17.compact;

import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Locale;

// stub implementation for jre17 compatibility
public class Java17CompactDateFormatSymbolsProvider extends DateFormatSymbolsProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public DateFormatSymbols getInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
