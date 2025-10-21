package be.kdg.integration2.mvpglobal.view.paricles;

import be.kdg.integration2.mvpglobal.view.UISettings;
import javafx.scene.paint.Color;

import java.util.Random;

public class Particles {

    private static final Random rand = new Random();
    private double posX, posY, speedX, speedY;
    private Color color;
    private ParticlesType type;
    private final Particles[] fireParticles = new Particles[getPARTICLE_COUNT()];
    private final Particles[] iceParticles = new Particles[getPARTICLE_COUNT()];

    public Particles(){
    }

    //private constructor used in initializeParticles
    private Particles(ParticlesType particleType) {
        setColor(getColorForType(particleType));
        setType(particleType);
        initializeEachParticle(particleType);
    }

    // setters
    public void setPosX(double posX) {
        this.posX = posX;
    }
    public void setPosY(double posY) {
        this.posY = posY;
    }
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }
    public void setType(ParticlesType type) {
        this.type = type;
    }
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    // getters
    public double getPosX() {
        return posX;
    }
    public double getPosY() {
        return posY;
    }
    public double getSpeedX() {
        return speedX;
    }
    public double getSpeedY() {
        return speedY;
    }
    public ParticlesType getType() {
        return type;
    }
    public Color getColor() {
        return color;
    }
    public Particles[] getFireParticles() {
        return fireParticles;
    }
    public Particles[] getIceParticles() {
        return iceParticles;
    }
    public int getPARTICLE_COUNT() {
        return 150;
    }

    // returns the color of a particle
    private Color getColorForType(ParticlesType type) {
        if (type == ParticlesType.FIRE) {
            return Color.rgb(255, rand.nextInt(150), 0, 0.8);
        } else {
            return Color.rgb(0, rand.nextInt(100) + 155, 255, 0.8);
        }
    }

    // Sets the position and the speed of each particle
    private void initializeEachParticle(ParticlesType particleType) {
        if (particleType == ParticlesType.FIRE){
            setPosX(rand.nextInt((int) (UISettings.SCREEN_WIDTH / 2)));
            setPosY((int) UISettings.SCREEN_HEIGHT -
                    rand.nextInt((int) (UISettings.SCREEN_HEIGHT / 4)));
            setSpeedX(rand.nextDouble() * 3 - 1.5);
            setSpeedY(-rand.nextDouble() * 3 - 1);
        }
        else if (particleType == ParticlesType.ICE) {
            setPosX((UISettings.SCREEN_WIDTH / 2) + rand.nextInt((int) (UISettings.SCREEN_WIDTH / 2)));
            setPosY(rand.nextInt((int) (UISettings.SCREEN_HEIGHT / 4)));
            setSpeedX(-rand.nextDouble() * 3 + 1.5);
            setSpeedY(rand.nextDouble() * 2 + 1);
        }
    }

    // Updates position and if it goes out of bounds
    // it's given new position and speed
    private void update() {
        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());

        if (getPosX() < 0 || getPosX() > UISettings.SCREEN_WIDTH
        || getPosY() < 0 || getPosY() > UISettings.SCREEN_HEIGHT) {
            initializeEachParticle(getType());
        }
    }

    // Updates each particle in the arrays
    public void updateAll(){
        for (Particles particle : fireParticles) {
            if (particle != null) {
                particle.update();
            }
        }
        for (Particles particle : iceParticles) {
            if (particle != null) {
                particle.update();
            }
        }
    }

    // initializes the fire and ice arrays
    public void initializeParticlesArray() {
        for (int i = 0; i < getPARTICLE_COUNT(); i++) {
            fireParticles[i] = new Particles(ParticlesType.FIRE);
            iceParticles[i] = new Particles(ParticlesType.ICE);
        }
    }
}
