package ai.victorl.toda.data.settings;

public interface TodaSettings {
    void saveOnExit(boolean save);
    boolean shouldSaveOnBack();
}
