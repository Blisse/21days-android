package ai.victorl.toda.data.store;

public interface LocalKeyStore {
    <T> T get(String key);

    <T> T get(String key, T defaultValue);

    <T> boolean put(String key, T value);

    boolean delete(String key);

    boolean clear();

    boolean contains(String key);
}
