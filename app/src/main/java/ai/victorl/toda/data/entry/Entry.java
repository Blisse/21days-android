package ai.victorl.toda.data.entry;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable model class for an Entry.
 */
public class Entry {
    private static final int MIN_ENTRY_LENGTH = 5;

    @NonNull
    public final String date;

    @NonNull
    public final String id;

    @NonNull
    public final String journal;

    @NonNull
    public final List<String> gratitudes;

    @NonNull
    public final String exercise;

    @NonNull
    public final String meditation;

    @NonNull
    public final String kindness;

    private Entry(UUID id, String date, String journal, List<String> gratitudes, String exercise, String meditation, String kindness) {
        this.id = id.toString();
        this.date = date;
        this.journal = journal;
        this.gratitudes = new ArrayList<>(gratitudes);
        this.exercise = exercise;
        this.meditation = meditation;
        this.kindness = kindness;
    }

    public Entry(Entry entry) {
        this(UUID.randomUUID(), entry.date, entry.journal, entry.gratitudes, entry.exercise, entry.meditation, entry.kindness);
    }

    public Entry() {
        this(UUID.randomUUID(), "", "", new ArrayList<String>(), "", "", "");
    }

    public boolean isComplete() {
        for (String gratitude : gratitudes) {
            if (gratitude.length() <= MIN_ENTRY_LENGTH) {
                return false;
            }
        }
        return journal.length() > MIN_ENTRY_LENGTH
                && exercise.length() > MIN_ENTRY_LENGTH
                && meditation.length() > MIN_ENTRY_LENGTH
                && kindness.length() > MIN_ENTRY_LENGTH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry e = (Entry) o;
        return this.id.equals(e.id)
                && this.date.equals(e.date)
                && this.journal.equals(e.journal)
                && this.gratitudes.equals(e.gratitudes)
                && this.meditation.equals(e.meditation)
                && this.exercise.equals(e.exercise)
                && this.kindness.equals(e.kindness);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Entry for date %s.", date);
    }

}
