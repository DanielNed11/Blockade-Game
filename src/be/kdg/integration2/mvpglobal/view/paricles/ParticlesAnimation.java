package be.kdg.integration2.mvpglobal.view.paricles;

import javafx.animation.AnimationTimer;

public class ParticlesAnimation {
    private final Particles particles;
    private final ParticlesCanvas particlesCanvas;

    public ParticlesAnimation(ParticlesCanvas particlesCanvas) {
        this.particles = new Particles();
        this.particlesCanvas = particlesCanvas;
    }

    public void startParticles() {
        particles.initializeParticlesArray();

        // creates an animation that is called 60 times per second
        // (60 FPS)
        AnimationTimer particleAnimation = new AnimationTimer() {
            @Override
            public void handle(long l) {
                particles.updateAll();
                particlesCanvas.drawParticles(particles);
            }
        };
        // Starts a loop that execute the handle()
        particleAnimation.start();
    }


}
