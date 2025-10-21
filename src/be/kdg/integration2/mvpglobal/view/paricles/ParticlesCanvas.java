package be.kdg.integration2.mvpglobal.view.paricles;

import be.kdg.integration2.mvpglobal.view.UISettings;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ParticlesCanvas extends Canvas {

    private final GraphicsContext gc;

    public ParticlesCanvas() {
        super(UISettings.SCREEN_WIDTH, UISettings.SCREEN_HEIGHT);
        this.gc = getGraphicsContext2D();
    }

    public void drawParticles(Particles particles) {
        // creates clear transparent
        gc.clearRect(0, 0, UISettings.SCREEN_WIDTH, UISettings.SCREEN_HEIGHT);

        // Sets the color, the shape and the size of a particle
        for (Particles fireParticle : particles.getFireParticles()) {
            gc.setFill(fireParticle.getColor());
            gc.fillOval(fireParticle.getPosX(),
                    fireParticle.getPosY(),
                    10, 10);
        }
        for (Particles iceParticle : particles.getIceParticles()) {
            gc.setFill(iceParticle.getColor());
            gc.fillOval(iceParticle.getPosX(),
                    iceParticle.getPosY(),
                    10, 10);
        }
    }

}
