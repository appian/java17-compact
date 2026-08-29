package com.appiancorp.jre17.compact;

import java.text.DateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Locale;

// stub implementation for jre17 compatibility
public class Java17CompactDateFormatProvider extends DateFormatProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public DateFormat getTimeInstance(int style, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public DateFormat getDateInstance(int style, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public DateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
