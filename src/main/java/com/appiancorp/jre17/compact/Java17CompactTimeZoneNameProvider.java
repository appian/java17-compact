package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.TimeZoneNameProvider;

// stub implementation for jre17 compatibility
public class Java17CompactTimeZoneNameProvider extends TimeZoneNameProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public String getDisplayName(String id, boolean daylight, int style, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
