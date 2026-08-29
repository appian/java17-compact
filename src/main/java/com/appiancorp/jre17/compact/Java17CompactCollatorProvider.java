package com.appiancorp.jre17.compact;

import java.text.Collator;
import java.text.spi.CollatorProvider;
import java.util.Locale;

// stub implementation for jre17 compatibility
public class Java17CompactCollatorProvider extends CollatorProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Collator getInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
