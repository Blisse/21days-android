package ai.victorl.toda.ui;

import android.view.View;

public class EntryChangeViewDecorator {
    private final View view;
    private final int baseColor;
    private final int oneColor;
    private final int twoColor;

    public EntryChangeViewDecorator(View view, int baseColour, int oneColour, int twoColour) {
        this.view = view;
        this.baseColor = baseColour;
        this.oneColor = oneColour;
        this.twoColor = twoColour;
    }

    public void onEntryChanged(int status) {
        if (status < 20) {
            view.setBackgroundColor(baseColor);
        } else if (status < 100) {
            view.setBackgroundColor(oneColor);
        } else {
            view.setBackgroundColor(twoColor);
        }
    }
}
