package ai.victorl.toda.data.entry.source;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.Collections;
import java.util.List;

import ai.victorl.toda.data.entry.Entry;
import rx.Observable;
import rx.schedulers.Schedulers;

class FirebaseEntryDatabase implements EntryDataSource {

    private static final String KEY_USERS = "users";
    private static final String KEY_DATE = "date";

    FirebaseEntryDatabase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private DatabaseReference getCurrentUserDatabaseReference() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }
        return FirebaseDatabase.getInstance()
                .getReference()
                .child(KEY_USERS)
                .child(user.getUid());
    }

    @Override
    public Observable<List<Entry>> getAllEntries() {
        DatabaseReference databaseReference = getCurrentUserDatabaseReference();
        if (databaseReference == null) {
            return Observable.empty();
        }
        return RxFirebaseDatabase.observeSingleValueEvent(databaseReference, DataSnapshotMapper.listOf(Entry.class))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    @Override
    public Observable<Entry> getEntry(@NonNull String entryDate) {
        DatabaseReference databaseReference = getCurrentUserDatabaseReference();
        if (databaseReference == null) {
            return Observable.empty();
        }

        Query query = databaseReference.orderByChild(KEY_DATE);

        return Observable.create(subscriber -> {
            RxFirebaseDatabase.observeSingleValueEvent(query, DataSnapshotMapper.listOf(Entry.class))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(entries -> {
                        if (!subscriber.isUnsubscribed()) {
                            int index = Collections.binarySearch(entries, entryDate, (o1, o2) -> {
                                    Entry e = (Entry) o1;
                                    String d = (String) o2;
                                    return e.date.compareTo(d);
                            });
                            if (index > -1) {
                                subscriber.onNext(entries.get(index));
                            } else {
                                subscriber.onNext(null);
                            }
                            subscriber.onCompleted();
                        }
                    });
        });
    }

    @Override
    public void saveEntry(@NonNull Entry entry) {
        DatabaseReference databaseReference = getCurrentUserDatabaseReference();
        if (databaseReference == null) {
            return;
        }

        RxFirebaseDatabase.observeSingleValueEvent(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(dataSnapshot -> {
                    boolean found = false;
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Entry snapshotValue = snapshot.getValue(Entry.class);
                        if (snapshotValue.date.equals(entry.date)) {
                            snapshot.getRef().setValue(entry);
                            found = true;
                        }
                    }
                    if (!found) {
                        dataSnapshot.getRef()
                                .push()
                                .setValue(entry);
                    }
                });
    }

    @Override
    public void deleteAllEntries() {
        DatabaseReference databaseReference = getCurrentUserDatabaseReference();
        if (databaseReference == null) {
            return;
        }
        RxFirebaseDatabase.observeSingleValueEvent(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(dataSnapshot -> {
                    dataSnapshot.getRef().removeValue();
                });
    }

    @Override
    public void deleteEntry(@NonNull String entryDate) {
        DatabaseReference databaseReference = getCurrentUserDatabaseReference();
        if (databaseReference == null) {
            return;
        }

        RxFirebaseDatabase.observeSingleValueEvent(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(dataSnapshot -> {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Entry snapshotValue = snapshot.getValue(Entry.class);
                        if (snapshotValue.date.equals(entryDate)) {
                            snapshot.getRef().removeValue();
                        }
                    }
                });
    }

    @Override
    public void deleteEntry(@NonNull Entry entry) {
        deleteEntry(entry.date);
    }
}
