package ai.victorl.toda.data.entry.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ai.victorl.toda.data.entry.Entry;
import ai.victorl.toda.data.store.LocalKeyStore;
import rx.Observable;

public class EntryRepository implements EntryDataSource {

    private static final String KEY_ENTRIES = "ai.victorl.toda.hawk.KEY_ENTRIES";

    private final LocalKeyStore localKeyStore;

    public EntryRepository(LocalKeyStore localKeyStore) {
        this.localKeyStore = localKeyStore;
    }

    @Override
    public Observable<List<Entry>> getAllEntries() {
        Map<String, Entry> entries = localKeyStore.get(KEY_ENTRIES);
        List<Entry> entriesList = new ArrayList<>(entries.values());
        return Observable.just(entriesList);
    }

    @Override
    public Observable<Entry> getEntry(@NonNull final String entryDate) {
        Map<String, Entry> entries = localKeyStore.get(KEY_ENTRIES);
        if (entries.containsKey(entryDate)) {
            return Observable.just(entries.get(entryDate));
        }
        return Observable.just(null);
    }

    @Override
    public void saveEntry(@NonNull Entry entry) {
        Map<String, Entry> entries = localKeyStore.get(KEY_ENTRIES);
        entries.put(entry.date, entry);
        localKeyStore.put(KEY_ENTRIES, entries);
    }

    @Override
    public void deleteAllEntries() {
        localKeyStore.delete(KEY_ENTRIES);
    }

    @Override
    public void deleteEntry(@NonNull String entryDate) {
        Map<String, Entry> entries = localKeyStore.get(KEY_ENTRIES);
        entries.remove(entryDate);
        localKeyStore.put(KEY_ENTRIES, entries);
    }

    @Override
    public void deleteEntry(@NonNull Entry entry) {
        deleteEntry(entry.date);
    }
}
