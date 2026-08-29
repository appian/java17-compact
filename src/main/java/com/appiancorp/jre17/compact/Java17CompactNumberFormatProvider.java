package com.appiancorp.jre17.compact;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;

// stub implementation for jre17 compatibility
public class Java17CompactNumberFormatProvider extends NumberFormatProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public NumberFormat getCurrencyInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public NumberFormat getIntegerInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public NumberFormat getNumberInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public NumberFormat getPercentInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
