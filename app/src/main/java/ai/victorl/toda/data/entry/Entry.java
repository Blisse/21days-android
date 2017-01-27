package ai.victorl.toda.data.entry;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.primitives.Doubles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Immutable model class for an Entry.
 */
public class Entry {
    private static final int MIN_ENTRY_LENGTH = 5;

    public String date;

    public String journal;

    public List<String> gratitudes;

    public String exercise;

    public String meditation;

    public String kindness;

    private Entry(@NonNull String date, @NonNull String journal, @NonNull List<String> gratitudes, @NonNull String exercise, @NonNull String meditation, @NonNull String kindness) {
        this.date = date;
        this.journal = journal;
        this.gratitudes = new ArrayList<>(gratitudes);
        this.exercise = exercise;
        this.meditation = meditation;
        this.kindness = kindness;
    }

    public Entry(@NonNull String date) {
        this(date, "", Arrays.asList("", "", ""), "", "", "");
    }

    public Entry(@NonNull Entry entry) {
        this(entry.date, entry.journal, entry.gratitudes, entry.exercise, entry.meditation, entry.kindness);
    }

    public Entry() {
        this("");
    }

    public long percentageComplete() {
        Double status = 0.0;
        Double maxSinglePercentage = 20.0;
        status += Doubles.min(journal.length()*maxSinglePercentage/MIN_ENTRY_LENGTH, maxSinglePercentage);
        status += Doubles.min(exercise.length()*maxSinglePercentage/MIN_ENTRY_LENGTH, maxSinglePercentage);
        status += Doubles.min(meditation.length()*maxSinglePercentage/MIN_ENTRY_LENGTH, maxSinglePercentage);
        status += Doubles.min(kindness.length()*maxSinglePercentage/MIN_ENTRY_LENGTH, maxSinglePercentage);

        Double gratitudePercentage = 0.0;
        Double maxSingleGratitude = 6.6667;
        for (String gratitude: gratitudes) {
            gratitudePercentage += Doubles.min(gratitude.length()*maxSingleGratitude/MIN_ENTRY_LENGTH, maxSingleGratitude);
        }

        status += Doubles.min(gratitudePercentage, maxSinglePercentage);

        return Math.round(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry e = (Entry) o;
        return this.date.equals(e.date)
                && this.journal.equals(e.journal)
                && this.gratitudes.equals(e.gratitudes)
                && this.meditation.equals(e.meditation)
                && this.exercise.equals(e.exercise)
                && this.kindness.equals(e.kindness);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date, journal, gratitudes, meditation, exercise, kindness);
    }

    @Override
    public String toString() {
        return String.format(Locale.CANADA, "Entry for date %s.", date);
    }
}
