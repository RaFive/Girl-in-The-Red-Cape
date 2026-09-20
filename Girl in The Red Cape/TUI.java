import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TUI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TUI extends Actor
{
    private boolean isWaiting = false;
    private int delayTimer = 0;
    private boolean isBlinking = false;
    private int blinkTimer = 0;
    private boolean isTriggered = false;
    private GreenfootSound goSound;

    public TUI()
    {
        setImage(new GreenfootImage(1, 1));
        goSound = new GreenfootSound("GoSFX.mp3");
    }

    public void act()
    {
        if (!isTriggered && getWorld() != null) {
            boolean noEnemies = getWorld().getObjects(Enemy.class).isEmpty();
            boolean noBoss = getWorld().getObjects(Boss.class).isEmpty();

            if (noEnemies && noBoss) {
                if (getWorld() instanceof Stage01) {
                    if (((Stage01) getWorld()).isStageCleared()) {
                        triggerGoWithDelay();
                    }
                } else if (getWorld() instanceof Stage02) {
                    if (((Stage02) getWorld()).isStageCleared()) {
                        triggerGoWithDelay();
                    }
                } else if (getWorld() instanceof StageFinal) {
                    if (((StageFinal) getWorld()).isBossDefeated()) {
                        triggerGoWithDelay();
                    }
                }
            }
        }

        if (isWaiting) {
            delayTimer++;
            if (delayTimer >= 60) {
                isWaiting = false;
                showGoText();
            }
        }

        if (isBlinking) {
            blinkTimer++;
            if (blinkTimer % 20 == 0) {
                int trans = getImage().getTransparency();
                int newTrans = (trans == 0) ? 255 : 0;
                getImage().setTransparency(newTrans);

                if (newTrans == 255) {
                    if (goSound != null) {
                        goSound.stop();
                        goSound.play();
                    }
                }
            }
        }
    }

    public void removedFromWorld(World world)
    {
        stopSound();
    }

    public void triggerGoWithDelay()
    {
        if (!isTriggered) {
            this.isTriggered = true;
            this.isWaiting = true;
            this.delayTimer = 0;
        }
    }

    private void showGoText()
    {
        String text = "GO >";
        int fontSize = 32;
        Color textColor = Color.WHITE;
        Color outlineColor = Color.BLACK;

        GreenfootImage tempText = new GreenfootImage(text, fontSize, textColor, new Color(0, 0, 0, 0));
        int width = tempText.getWidth() + 8;
        int height = tempText.getHeight() + 8;

        GreenfootImage image = new GreenfootImage(width, height);

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                if (x != 0 || y != 0) {
                    GreenfootImage border = new GreenfootImage(text, fontSize, outlineColor, new Color(0, 0, 0, 0));
                    image.drawImage(border, 4 + x, 4 + y);
                }
            }
        }

        image.drawImage(tempText, 4, 4);
        setImage(image);
        this.isBlinking = true;

        if (goSound != null) {
            goSound.stop();
            goSound.play();
        }
    }

    public void stopSound()
    {
        if (goSound != null && goSound.isPlaying()) {
            goSound.stop();
        }
    }
}