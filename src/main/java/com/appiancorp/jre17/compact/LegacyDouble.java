package com.appiancorp.jre17.compact;

import com.appiancorp.jre17.compact.thirdparty.FloatingDecimal;

// implements with jre17 behaviour
public class LegacyDouble {
  public static String toString(double d) {
    return FloatingDecimal.toJavaFormatString(d);
  }
}
