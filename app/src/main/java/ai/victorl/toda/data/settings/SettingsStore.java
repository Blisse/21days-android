package ai.victorl.toda.data.settings;

import ai.victorl.toda.data.store.LocalKeyStore;

public class SettingsStore implements TodaSettings {
    private static final String KEY_SAVE_ON_EXIT = "ai.victorl.toda.settings.KEY_SAVE_ON_EXIT";
    private final LocalKeyStore localKeyStore;

    public SettingsStore(LocalKeyStore localKeyStore) {
        this.localKeyStore = localKeyStore;
    }

    @Override
    public void saveOnExit(boolean save) {
        localKeyStore.put(KEY_SAVE_ON_EXIT, save);
    }

    @Override
    public boolean shouldSaveOnBack() {
        return localKeyStore.get(KEY_SAVE_ON_EXIT, Boolean.TRUE);
    }
}
