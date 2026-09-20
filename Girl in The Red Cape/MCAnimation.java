import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MCAnimation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MCAnimation extends Actor
{
    private GreenfootImage[] rightFrames;
    private GreenfootImage[] leftFrames;
    private int animationIndex = 0;
    private int animationCounter = 0;

    public MCAnimation(String fileName, int frameCount)
    {
        rightFrames = new GreenfootImage[frameCount];
        leftFrames = new GreenfootImage[frameCount];
        loadAndMirror(fileName, frameCount);
    }

    private void loadAndMirror(String fileName, int frameCount)
    {
        GreenfootImage spriteSheet = new GreenfootImage(fileName);
        int frameWidth = 192;
        int frameHeight = 128;

        for (int i = 0; i < frameCount; i++) {
            GreenfootImage frameRight = new GreenfootImage(frameWidth, frameHeight);
            frameRight.drawImage(spriteSheet, -i * frameWidth, 0);
            rightFrames[i] = frameRight;

            GreenfootImage frameLeft = new GreenfootImage(frameRight);
            frameLeft.mirrorHorizontally();
            leftFrames[i] = frameLeft;
        }
    }

    public GreenfootImage animateLoop(boolean isFacingRight, int delay)
    {
        animationCounter++;
        if (animationCounter % delay == 0) {
            animationIndex = (animationIndex + 1) % rightFrames.length;
        }
        return isFacingRight ? rightFrames[animationIndex] : leftFrames[animationIndex];
    }

    public GreenfootImage animateOnce(boolean isFacingRight, int delay, Runnable onComplete)
    {
        animationCounter++;
        if (animationCounter % delay == 0) {
            animationIndex++;
            if (animationIndex >= rightFrames.length) {
                animationIndex = 0;
                onComplete.run();
            }
        }
        return isFacingRight ? rightFrames[animationIndex] : leftFrames[animationIndex];
    }

    public void reset()
    {
        animationIndex = 0;
        animationCounter = 0;
    }

    public GreenfootImage getFirstFrame(boolean isFacingRight)
    {
        return isFacingRight ? rightFrames[0] : leftFrames[0];
    }

    public void act()
    {
        // Add your action code here.
    }
}