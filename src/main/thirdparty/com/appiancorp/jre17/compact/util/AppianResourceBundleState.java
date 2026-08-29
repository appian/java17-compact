/*
 * Not part of the original OpenJDK sources. The real JDK's
 * sun.util.resources.Bundles sets a loaded ResourceBundle's parent, locale,
 * and base name via the JDK-internal jdk.internal.access.SharedSecrets/
 * JavaUtilResourceBundleAccess bridge (java.util.ResourceBundle.setParent is
 * protected, and its locale/name fields have no accessor at all, reachable
 * from outside java.util only via that internal bridge or reflection with
 * setAccessible(true), which since JPMS strong encapsulation requires the
 * consuming JVM to be launched with --add-opens java.base/java.util=
 * ALL-UNNAMED -- not something a library can require of its callers, and not
 * guaranteed stable across JDK versions).
 *
 * This interface lets our own ResourceBundle base classes
 * (OpenListResourceBundle, ParallelListResourceBundle,
 * BreakIteratorResourceBundle, AppianListResourceBundle) track this state
 * themselves as plain instance fields, with getLocale() overridden to
 * return it, and setParent(ResourceBundle) invoked directly (it's
 * `protected`, so callable from within these classes' own instance methods
 * without reflection). sun.util.resources.Bundles calls these methods
 * through this interface instead of jdk.internal.access.SharedSecrets.
 */
package com.appiancorp.jre17.compact.util;

import java.util.Locale;
import java.util.ResourceBundle;

public interface AppianResourceBundleState {
    void appianSetParent(ResourceBundle parent);

    ResourceBundle appianGetParent();

    void appianSetLocale(Locale locale);
}
