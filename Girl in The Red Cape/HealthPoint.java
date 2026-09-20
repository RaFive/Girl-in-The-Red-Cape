import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HealthPoint here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HealthPoint extends Actor
{
    private enum UIType { PLAYER, BOSS }
    private UIType uiType;

    private GreenfootImage[] hpFrames = new GreenfootImage[5];

    private int bossMaxHp = 10;

    public HealthPoint()
    {
        this.uiType = UIType.PLAYER;
        loadFrames();
        updateHP(4);
    }

    public HealthPoint(int maxHp)
    {
        this.uiType = UIType.BOSS;
        this.bossMaxHp = maxHp;
        updateBossHP(maxHp);
    }

    private void loadFrames()
    {
        GreenfootImage rawSheet = new GreenfootImage("MC_HP.png");
        
        int frameWidth = rawSheet.getWidth() / 3;
        int frameHeight = rawSheet.getHeight() / 2;

        hpFrames[4] = cropFrame(rawSheet, 0 * frameWidth, 0 * frameHeight, frameWidth, frameHeight);
        hpFrames[3] = cropFrame(rawSheet, 1 * frameWidth, 0 * frameHeight, frameWidth, frameHeight);
        hpFrames[2] = cropFrame(rawSheet, 2 * frameWidth, 0 * frameHeight, frameWidth, frameHeight);
        hpFrames[1] = cropFrame(rawSheet, 0 * frameWidth, 1 * frameHeight, frameWidth, frameHeight);
        hpFrames[0] = cropFrame(rawSheet, 1 * frameWidth, 1 * frameHeight, frameWidth, frameHeight);
    }

    private GreenfootImage cropFrame(GreenfootImage sheet, int x, int y, int width, int height)
    {
        GreenfootImage frame = new GreenfootImage(width, height);
        frame.drawImage(sheet, -x, -y);
        frame.scale(width * 6, height * 6); 
        return frame;
    }

    public void updateHP(int currentHp)
    {
        if (uiType != UIType.PLAYER) return;
        if (currentHp > 4) currentHp = 4;
        if (currentHp < 0) currentHp = 0;
        
        setImage(hpFrames[currentHp]);
    }

    public void updateBossHP(int currentHp)
    {
        if (uiType != UIType.BOSS) return;
        if (currentHp < 0) currentHp = 0;

        double percentage = ((double) currentHp / bossMaxHp) * 100;
        int roundedPercent = (int) (Math.round(percentage / 10.0) * 10);

        if (roundedPercent <= 0 && currentHp > 0) {
            roundedPercent = 10;
        }

        String imageName = "BossHP" + roundedPercent + ".png";

        try {
            GreenfootImage img = new GreenfootImage(imageName);
            img.scale((int)(img.getWidth() * 0.1), (int)(img.getHeight() * 0.075));
            setImage(img);
        } catch (Exception e) {
            setImage((GreenfootImage) null);
        }
    }
}