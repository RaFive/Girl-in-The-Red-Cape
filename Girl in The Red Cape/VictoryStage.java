import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class VictoryStage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VictoryStage extends MyWorld
{
    private MCAnimation animWin;
    private boolean isWalkingToCenter = true;
    private int walkSpeed = 10;

    public VictoryStage()
    {
        super();
        GreenfootImage bg = new GreenfootImage(getWidth(), getHeight());
        bg.setColor(Color.WHITE);
        bg.fill();
        setBackground(bg);
        
        animWin = new MCAnimation("Red_win.png", 5);
        prepareVictoryStage();
    }

    private void prepareVictoryStage()
    {
        removeObjects(getObjects(Actor.class));

        mainChar = new MainChar();
        addObject(mainChar, 50, 450);
        mainChar.setHealthPoint(null);
        mainChar.enableAutoMove(walkSpeed);
    }

    @Override
    public void act()
    {
        if (mainChar == null) return;

        if (!getObjects(HealthPoint.class).isEmpty()) {
            removeObjects(getObjects(HealthPoint.class));
        }

        if (isWalkingToCenter) {
            if (mainChar.getX() >= 600) {
                mainChar.setLocation(600, 450);
                mainChar.setVictoryState(true);
                isWalkingToCenter = false;
                animWin.reset();
                Greenfoot.playSound("VictorySound.mp3");
            }
        } 
        else {
            mainChar.setImage(animWin.animateOnce(true, 15, () -> {}));
            showText("You Win!", 600, 300);
        }
    }
}