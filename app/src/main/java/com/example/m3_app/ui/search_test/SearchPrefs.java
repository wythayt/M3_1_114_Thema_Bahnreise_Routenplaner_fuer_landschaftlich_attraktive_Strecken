package com.example.m3_app.ui.search_test;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;


public class SearchPrefs {
    private static final String PREFS_NAME = "search_prefs";
    private static final String KEY_LAST_FROM = "last_from";
    private static final String KEY_LAST_TO = "last_to";
    private static final String KEY_HISTORY_FROM = "history_from";
    private static final String KEY_HISTORY_TO = "history_to";
    private static final int MAX_HISTORY_SIZE = 10;

    private final SharedPreferences prefs;

    public SearchPrefs(Context ctx) {
        prefs = ctx.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveLastPair(String from, String to) {
        prefs.edit()
                .putString(KEY_LAST_FROM, from)
                .putString(KEY_LAST_TO, to)
                .apply();
    }

    public String getLastFrom() {
        return prefs.getString(KEY_LAST_FROM, "");
    }
    public String getLastTo() {
        return prefs.getString(KEY_LAST_TO, "");
    }

    public void addToHistoryFrom(String from) {
        if (from == null || from.isEmpty()) return;

        Set<String> oldSet = prefs.getStringSet(KEY_HISTORY_FROM, null);
        LinkedHashSet<String> history;
        if (oldSet == null) {
            history = new LinkedHashSet<>();
        } else {
            history = new LinkedHashSet<>(oldSet);
        }

        if (history.contains(from)) {
            history.remove(from);
        }
        history.add(from);

        while (history.size() > MAX_HISTORY_SIZE) {
            String first = history.iterator().next();
            history.remove(first);
        }

        prefs.edit()
                .putStringSet(KEY_HISTORY_FROM, history)
                .apply();
    }

    public void addToHistoryTo(String to) {
        if (to == null || to.isEmpty()) return;

        Set<String> oldSet = prefs.getStringSet(KEY_HISTORY_TO, null);
        LinkedHashSet<String> history;
        if (oldSet == null) {
            history = new LinkedHashSet<>();
        } else {
            history = new LinkedHashSet<>(oldSet);
        }

        if (history.contains(to)) {
            history.remove(to);
        }
        history.add(to);

        while (history.size() > MAX_HISTORY_SIZE) {
            String first = history.iterator().next();
            history.remove(first);
        }

        prefs.edit()
                .putStringSet(KEY_HISTORY_TO, history)
                .apply();
    }

    public Set<String> getHistoryFrom() {
        Set<String> saved = prefs.getStringSet(KEY_HISTORY_FROM, null);
        if (saved == null) return new LinkedHashSet<>();
        return new LinkedHashSet<>(saved);
    }

    public Set<String> getHistoryTo() {
        Set<String> saved = prefs.getStringSet(KEY_HISTORY_TO, null);
        if (saved == null) return new LinkedHashSet<>();
        return new LinkedHashSet<>(saved);
    }

    public void clearHistory() {
        prefs.edit()
                .remove(KEY_HISTORY_FROM)
                .remove(KEY_HISTORY_TO)
                .apply();
    }
}
