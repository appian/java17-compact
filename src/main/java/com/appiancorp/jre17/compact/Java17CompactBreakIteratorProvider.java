package com.appiancorp.jre17.compact;

import java.text.BreakIterator;
import java.text.spi.BreakIteratorProvider;
import java.util.Locale;

// stub implementation for jre17 compatibility
public class Java17CompactBreakIteratorProvider extends BreakIteratorProvider {

  @Override
  public Locale[] getAvailableLocales() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public BreakIterator getWordInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public BreakIterator getLineInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public BreakIterator getCharacterInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public BreakIterator getSentenceInstance(Locale locale) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
