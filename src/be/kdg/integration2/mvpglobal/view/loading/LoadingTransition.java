package be.kdg.integration2.mvpglobal.view.loading;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.util.Duration;

public class LoadingTransition extends Transition {
    private final LoadingView view;

    public LoadingTransition(LoadingView view, int maxDuration) {
        this.view = view;
        this.setCycleDuration(Duration.seconds(maxDuration));
        this.setCycleCount(1);
        Interpolator interpolator = Interpolator.EASE_BOTH;

        this.setInterpolator(interpolator);
    }

    @Override
    protected void interpolate(double frac) {
        int progress = (int) (frac * 100); // Round to whole numbers
        this.view.getTimeDisplay().setText("Loading: " + progress + "%");
        this.view.getTimeProgress().setProgress(frac);
    }
}
