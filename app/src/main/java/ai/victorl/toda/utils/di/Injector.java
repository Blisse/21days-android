package ai.victorl.toda.utils.di;

import android.content.Context;

import ai.victorl.toda.app.TodaApp;
import ai.victorl.toda.screens.entry.AddEditEntryModule;
import ai.victorl.toda.screens.entry.DaggerAddEditEntryComponent;
import ai.victorl.toda.screens.entry.AddEditEntryComponent;
import ai.victorl.toda.screens.entry.AddEditEntryContract;

public class Injector {

    private Injector() {}

    public static AddEditEntryComponent buildEntryComponent(Context context, AddEditEntryContract.View view, String entryDate) {
        return DaggerAddEditEntryComponent.builder()
                .dataComponent(TodaApp.from(context).getDataComponent())
                .addEditEntryModule(new AddEditEntryModule(entryDate, view))
                .build();
    }
}
