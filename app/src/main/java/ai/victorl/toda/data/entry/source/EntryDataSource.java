package ai.victorl.toda.data.entry.source;

import android.support.annotation.NonNull;

import java.util.List;

import ai.victorl.toda.data.entry.Entry;
import rx.Observable;

public interface EntryDataSource {

    Observable<List<Entry>> getAllEntries();

    Observable<Entry> getEntry(@NonNull String entryDate);

    void saveEntry(@NonNull Entry entry);

    void deleteAllEntries();

    void deleteEntry(@NonNull String entryDate);

    void deleteEntry(@NonNull Entry entry);
}
