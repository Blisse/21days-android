package ai.victorl.toda.data.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsStore implements TodaSettings {
    private static final String KEY_SHARED_PREFERENCES = "ai.victorl.toda.settings.KEY_SHARED_PREFS";
    private static final String KEY_SAVE_ON_EXIT = "ai.victorl.toda.settings.KEY_SAVE_ON_EXIT";
    private final SharedPreferences preferences;

    public SettingsStore(Context context) {
        this.preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void saveOnExit(boolean save) {
        preferences.edit()
                .putBoolean(KEY_SAVE_ON_EXIT, save)
                .apply();
    }

    @Override
    public boolean shouldSaveOnBack() {
        return preferences.getBoolean(KEY_SAVE_ON_EXIT, Boolean.TRUE);
    }
}
