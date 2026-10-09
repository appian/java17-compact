/*
 * Copyright (c) 2026 Appian Corporation. All rights reserved.
 *
 * This file has been modified by Appian Corporation on 2026-08-29.
 * Brief description of changes: Appian-original code (not derived from OpenJDK source). Reproduces JDK 17 Float.toString output by delegating to the ported FloatingDecimal.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, with
 * the Classpath Exception, as published by the Free Software Foundation.
 */
package com.appiancorp.jre17.compact;

import com.appiancorp.jre17.compact.thirdparty.FloatingDecimal;

// implements with jre17 behaviour
public class LegacyFloat {
  public static String toString(float f) {
    return FloatingDecimal.toJavaFormatString(f);
  }
}
