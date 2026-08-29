package com.appiancorp.jre17.compact;

import com.appiancorp.jre17.compact.thirdparty.FloatingDecimal;

// implements with jre17 behaviour
public class LegacyFloat {
  public static String toString(float f) {
    return FloatingDecimal.toJavaFormatString(f);
  }
}
