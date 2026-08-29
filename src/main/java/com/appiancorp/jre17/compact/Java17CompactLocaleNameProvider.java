package com.appiancorp.jre17.compact;

import java.util.Locale;
import java.util.spi.LocaleNameProvider;

// stub implementation for jre17 compatibility
public class Java17CompactLocaleNameProvider extends LocaleNameProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public String getDisplayLanguage(String languageCode, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public String getDisplayCountry(String countryCode, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public String getDisplayVariant(String variant, Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
