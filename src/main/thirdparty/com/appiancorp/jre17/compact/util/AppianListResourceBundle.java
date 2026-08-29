/*
 * Not part of the original OpenJDK sources. sun.text.resources.
 * BreakIteratorInfo, sun.text.resources.BreakIteratorRules, and
 * sun.text.resources.CollationData (and their per-locale subclasses under
 * sun.text.resources.ext) extend java.util.ListResourceBundle directly in
 * the original OpenJDK sources. Since we can't add our
 * AppianResourceBundleState tracking to java.util.ListResourceBundle itself,
 * this class stands in as their common superclass instead, giving them the
 * same getLocale()/appianSetParent/appianSetLocale support as our other
 * ResourceBundle base classes (OpenListResourceBundle,
 * ParallelListResourceBundle, BreakIteratorResourceBundle) -- see
 * AppianResourceBundleState for why. It otherwise behaves exactly like
 * java.util.ListResourceBundle (same handleGetObject/handleKeySet
 * implementation, copied from ListResourceBundle.java).
 */
package com.appiancorp.jre17.compact.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.appiancorp.jre17.compact.thirdparty.sun.util.ResourceBundleEnumeration;

public abstract class AppianListResourceBundle extends ResourceBundle
        implements AppianResourceBundleState {

    protected AppianListResourceBundle() {
    }

    private volatile Locale appianLocale;

    @Override
    public Locale getLocale() {
        return appianLocale;
    }

    @Override
    public void appianSetLocale(Locale locale) {
        this.appianLocale = locale;
    }

    @Override
    public void appianSetParent(ResourceBundle parent) {
        setParent(parent);
    }

    @Override
    public ResourceBundle appianGetParent() {
        return this.parent;
    }

    /**
     * See {@link java.util.ListResourceBundle#getContents()}.
     */
    protected abstract Object[][] getContents();

    @Override
    protected final Object handleGetObject(String key) {
        // lazily load the lookup hashtable.
        if (lookup == null) {
            loadLookup();
        }
        if (key == null) {
            throw new NullPointerException();
        }
        return lookup.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        if (lookup == null) {
            loadLookup();
        }
        ResourceBundle parentBundle = this.parent;
        return new ResourceBundleEnumeration(
                lookup.keySet(),
                (parentBundle != null) ? parentBundle.getKeys() : null);
    }

    @Override
    protected Set<String> handleKeySet() {
        if (lookup == null) {
            loadLookup();
        }
        return lookup.keySet();
    }

    private synchronized void loadLookup() {
        if (lookup != null) {
            return;
        }
        Object[][] contents = getContents();
        Map<String, Object> temp = new HashMap<>(contents.length);
        for (Object[] content : contents) {
            String key = (String) content[0];
            Object value = content[1];
            if (key == null || value == null) {
                throw new NullPointerException();
            }
            temp.put(key, value);
        }
        lookup = temp;
    }

    private volatile Map<String, Object> lookup;
}
