package ai.victorl.toda.data.entry;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;

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

    public Integer getJournalComplete() {
        return Math.min(100, (journal.length()*100) / MIN_ENTRY_LENGTH);
    }

    public Integer getGratitudesComplete() {
        Integer gratitudeComplete = 0;

        for (String gratitude: gratitudes) {
            gratitudeComplete += Math.max(100, (gratitude.length()*100) / MIN_ENTRY_LENGTH);
        }

        return Math.min(100, Long.valueOf(Math.round(gratitudeComplete / 3.0)).intValue());
    }

    public Integer getExerciseComplete() {
        return Math.min(100, (meditation.length()*100) / MIN_ENTRY_LENGTH);
    }

    public Integer getMeditationComplete() {
        return Math.min(100, (meditation.length()*100) / MIN_ENTRY_LENGTH);
    }

    public Integer getKindnessComplete() {
        return Math.min(100, (kindness.length()*100) / MIN_ENTRY_LENGTH);
    }

    public Integer getPercentageComplete() {
        Integer complete = 0;

        complete += getJournalComplete();
        complete += getGratitudesComplete();
        complete += getExerciseComplete();
        complete += getMeditationComplete();
        complete += getKindnessComplete();
        complete /= 5;

        return complete;
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
