import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BossAnimation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BossAnimation extends Actor
{
    private GreenfootImage[] flyRightFrames;
    private GreenfootImage[] flyLeftFrames;
    
    private GreenfootImage[] hurtRightFrames;
    private GreenfootImage[] hurtLeftFrames;

    private GreenfootImage[] attackRightFrames;
    private GreenfootImage[] attackLeftFrames;

    private GreenfootImage[] dieRightFrames;
    private GreenfootImage[] dieLeftFrames;

    private int currentFlyFrame = 0;
    private int currentHurtFrame = 0;
    private int currentAttackFrame = 0;
    private int currentDieFrame = 0;
    private int animCounter = 0;
    
    private int totalFlyFrames;
    private int totalHurtFrames;
    private int totalAttackFrames;
    private int totalDieFrames;
    private double scaleMultiplier;

    public BossAnimation(String flySheet, String hurtSheet, String attackSheet, String dieSheet, double scale)
    {
        this.scaleMultiplier = scale;
        
        this.totalFlyFrames = 4;
        flyRightFrames = new GreenfootImage[totalFlyFrames];
        flyLeftFrames = new GreenfootImage[totalFlyFrames];
        GreenfootImage rawFly = new GreenfootImage(flySheet);
        int flyW = rawFly.getWidth() / totalFlyFrames;
        int flyH = rawFly.getHeight();
        for (int i = 0; i < totalFlyFrames; i++) {
            GreenfootImage frame = extractFrame(rawFly, i, flyW, flyH);
            flyLeftFrames[i] = new GreenfootImage(frame);
            flyRightFrames[i] = new GreenfootImage(frame);
            flyRightFrames[i].mirrorHorizontally();
        }

        this.totalHurtFrames = 3;
        hurtRightFrames = new GreenfootImage[totalHurtFrames];
        hurtLeftFrames = new GreenfootImage[totalHurtFrames];
        GreenfootImage rawHurt = new GreenfootImage(hurtSheet);
        int hurtW = rawHurt.getWidth() / 4;
        int hurtH = rawHurt.getHeight();
        int[] validHurtCols = {0, 2, 3};
        for (int i = 0; i < totalHurtFrames; i++) {
            GreenfootImage frame = extractFrame(rawHurt, validHurtCols[i], hurtW, hurtH);
            hurtLeftFrames[i] = new GreenfootImage(frame);
            hurtRightFrames[i] = new GreenfootImage(frame);
            hurtRightFrames[i].mirrorHorizontally();
        }

        this.totalAttackFrames = 8;
        attackRightFrames = new GreenfootImage[totalAttackFrames];
        attackLeftFrames = new GreenfootImage[totalAttackFrames];
        GreenfootImage rawAttack = new GreenfootImage(attackSheet);
        int attW = rawAttack.getWidth() / totalAttackFrames;
        int attH = rawAttack.getHeight();
        for (int i = 0; i < totalAttackFrames; i++) {
            GreenfootImage frame = extractFrame(rawAttack, i, attW, attH);
            attackLeftFrames[i] = new GreenfootImage(frame);
            attackRightFrames[i] = new GreenfootImage(frame);
            attackRightFrames[i].mirrorHorizontally();
        }

        this.totalDieFrames = 6;
        dieRightFrames = new GreenfootImage[totalDieFrames];
        dieLeftFrames = new GreenfootImage[totalDieFrames];
        GreenfootImage rawDie = new GreenfootImage(dieSheet);
        int dieW = rawDie.getWidth() / 7;
        int dieH = rawDie.getHeight();
        for (int i = 0; i < totalDieFrames; i++) {
            GreenfootImage frame = extractFrame(rawDie, i, dieW, dieH);
            dieLeftFrames[i] = new GreenfootImage(frame);
            dieRightFrames[i] = new GreenfootImage(frame);
            dieRightFrames[i].mirrorHorizontally();
        }
    }

    private GreenfootImage extractFrame(GreenfootImage sheet, int col, int w, int h)
    {
        GreenfootImage frame = new GreenfootImage(w, h);
        frame.drawImage(sheet, -col * w, 0);
        frame.scale((int)(w * scaleMultiplier), (int)(h * scaleMultiplier));
        return frame;
    }

    public GreenfootImage animateLoop(boolean isFacingRight, int speed)
    {
        animCounter++;
        if (animCounter % speed == 0) {
            currentFlyFrame = (currentFlyFrame + 1) % totalFlyFrames;
        }
        return isFacingRight ? flyRightFrames[currentFlyFrame] : flyLeftFrames[currentFlyFrame];
    }

    public GreenfootImage animateOnceHurt(boolean isFacingRight, int speed, Runnable onComplete)
    {
        animCounter++;
        if (animCounter % speed == 0) {
            if (currentHurtFrame < totalHurtFrames - 1) {
                currentHurtFrame++;
            } else if (onComplete != null) {
                onComplete.run();
            }
        }
        return isFacingRight ? hurtRightFrames[currentHurtFrame] : hurtLeftFrames[currentHurtFrame];
    }

    public GreenfootImage animateAttack(boolean isFacingRight, int speed, Runnable onComplete)
    {
        animCounter++;
        if (animCounter % speed == 0) {
            if (currentAttackFrame < totalAttackFrames - 1) {
                currentAttackFrame++;
            } else if (onComplete != null) {
                onComplete.run();
            }
        }
        return isFacingRight ? attackRightFrames[currentAttackFrame] : attackLeftFrames[currentAttackFrame];
    }

    public GreenfootImage animateDeath(boolean isFacingRight, int speed, Runnable onComplete)
    {
        animCounter++;
        if (animCounter % speed == 0) {
            if (currentDieFrame < totalDieFrames - 1) {
                currentDieFrame++;
            } else if (onComplete != null) {
                onComplete.run();
            }
        }
        return isFacingRight ? dieRightFrames[currentDieFrame] : dieLeftFrames[currentDieFrame];
    }

    public int getCurrentAttackFrame()
    {
        return currentAttackFrame;
    }

    public GreenfootImage getFirstFrame(boolean isFacingRight)
    {
        return isFacingRight ? flyRightFrames[0] : flyLeftFrames[0];
    }

    public void resetHurt()
    {
        currentHurtFrame = 0;
        animCounter = 0;
    }

    public void resetAttack()
    {
        currentAttackFrame = 0;
        animCounter = 0;
    }

    public void resetDeath()
    {
        currentDieFrame = 0;
        animCounter = 0;
    }
}