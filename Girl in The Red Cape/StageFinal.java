import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StageFinal here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StageFinal extends MyWorld
{
    private Boss boss;

    public StageFinal(int hp)
    {
        super();
        setHp(hp);
        setGameBackground("BG_StageBoss.png");
        prepareStageFinal();
    }

    public void act()
    {
        checkStageTransition(() -> new VictoryStage(), isBossDefeated());
    }

    public boolean isBossDefeated()
    {
        return getObjects(Boss.class).isEmpty();
    }

    private void prepareStageFinal()
    {
        setMainChar(new MainChar(), getHp(), 100, 450);
        setGoTUI();
        boss = new Boss();
        addObject(boss, 900, 380);
        HealthPoint bossHpUI = new HealthPoint(10);
        addObject(bossHpUI, getWidth() - 180, 50);
        boss.setHpBar(bossHpUI);
    }
}