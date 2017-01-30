package ai.victorl.toda.data.store;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

public class HawkKeyStore implements LocalKeyStore {
    public HawkKeyStore(Context context) {
        if (!Hawk.isBuilt()) {
            Hawk.init(context)
                    .build();
        }
    }

    @Override
    public <T> T get(String key) {
        return Hawk.get(key);
    }

    @Override
    public <T> T get(String key, T defaultValue) {
        return Hawk.get(key, defaultValue);
    }

    @Override
    public <T> boolean put(String key, T value) {
        return Hawk.put(key, value);
    }

    @Override
    public boolean delete(String key) {
        return Hawk.delete(key);
    }

    @Override
    public boolean clear() {
        return Hawk.deleteAll();
    }

    @Override
    public boolean contains(String key) {
        return Hawk.contains(key);
    }
}
