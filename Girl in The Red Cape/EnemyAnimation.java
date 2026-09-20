import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EnemyAnimation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnemyAnimation extends Actor
{
    private GreenfootImage[] moveRightFrames;
    private GreenfootImage[] moveLeftFrames;
    private GreenfootImage[] deathRightFrames;
    private GreenfootImage[] deathLeftFrames;

    private int currentMoveFrame = 0;
    private int currentDeathFrame = 0;
    private int animCounter = 0;
    private int totalMoveFrames;
    private int totalDeathFrames;
    private double scaleMultiplier;

    public EnemyAnimation(String spriteSheet, int totalFrames, double scale)
    {
        this(spriteSheet, totalFrames, 1, totalFrames, 0, scale);
    }

    public EnemyAnimation(String spriteSheet, int cols, int rows, int moveFramesCount, int deathFramesCount, double scale)
    {
        this.totalMoveFrames = moveFramesCount;
        this.totalDeathFrames = deathFramesCount;
        this.scaleMultiplier = scale;

        moveRightFrames = new GreenfootImage[totalMoveFrames];
        moveLeftFrames = new GreenfootImage[totalMoveFrames];

        if (totalDeathFrames > 0) {
            deathRightFrames = new GreenfootImage[totalDeathFrames];
            deathLeftFrames = new GreenfootImage[totalDeathFrames];
        }

        GreenfootImage rawSheet = new GreenfootImage(spriteSheet);
        int frameWidth = rawSheet.getWidth() / cols;
        int frameHeight = rawSheet.getHeight() / rows;

        for (int i = 0; i < totalMoveFrames; i++) {
            GreenfootImage frame = extractFrame(rawSheet, i, 0, frameWidth, frameHeight);
            moveRightFrames[i] = new GreenfootImage(frame);
            moveRightFrames[i].mirrorHorizontally();
            moveLeftFrames[i] = new GreenfootImage(frame);
        }

        if (totalDeathFrames > 0) {
            int deathIdx = 0;
            for (int r = 1; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (deathIdx < totalDeathFrames) {
                        GreenfootImage frame = extractFrame(rawSheet, c, r, frameWidth, frameHeight);
                        deathRightFrames[deathIdx] = new GreenfootImage(frame);
                        deathRightFrames[deathIdx].mirrorHorizontally();
                        deathLeftFrames[deathIdx] = new GreenfootImage(frame);
                        deathIdx++;
                    }
                }
            }
        }
    }

    private GreenfootImage extractFrame(GreenfootImage sheet, int col, int row, int w, int h)
    {
        GreenfootImage frame = new GreenfootImage(w, h);
        frame.drawImage(sheet, -col * w, -row * h);
        frame.scale((int)(w * scaleMultiplier), (int)(h * scaleMultiplier));
        return frame;
    }

    public GreenfootImage animateLoop(boolean isFacingRight, int speed)
    {
        animCounter++;
        if (animCounter % speed == 0) {
            currentMoveFrame = (currentMoveFrame + 1) % totalMoveFrames;
        }
        return isFacingRight ? moveRightFrames[currentMoveFrame] : moveLeftFrames[currentMoveFrame];
    }

    public GreenfootImage animateDeath(boolean isFacingRight, int speed, Runnable onComplete)
    {
        if (totalDeathFrames == 0) return getFirstFrame(isFacingRight);

        animCounter++;
        if (animCounter % speed == 0) {
            if (currentDeathFrame < totalDeathFrames - 1) {
                currentDeathFrame++;
            } else if (onComplete != null) {
                onComplete.run();
            }
        }
        return isFacingRight ? deathRightFrames[currentDeathFrame] : deathLeftFrames[currentDeathFrame];
    }

    public GreenfootImage getFirstFrame(boolean isFacingRight)
    {
        return isFacingRight ? moveRightFrames[0] : moveLeftFrames[0];
    }

    public void resetDeath()
    {
        currentDeathFrame = 0;
        animCounter = 0;
    }
}