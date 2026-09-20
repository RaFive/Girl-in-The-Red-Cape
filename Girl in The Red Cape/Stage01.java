import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Stage01 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stage01 extends MyWorld
{
    private EnemySpawn slimeSpawner;

    public Stage01(int hp)
    {
        super();
        setHp(hp);
        setGameBackground("BG_Stage01.png");
        prepareStage01();
    }

    public void act()
    {
        checkStageTransition(() -> new Stage02(mainChar.getHp()), isStageCleared());
    }

    public boolean isStageCleared()
    {
        boolean noEnemiesLeft = getObjects(Enemy.class).isEmpty();
        boolean spawnerFinished = (slimeSpawner != null) && slimeSpawner.isWaveFinished();
        return noEnemiesLeft && spawnerFinished;
    }

    private void prepareStage01()
    {
        setMainChar(new MainChar(), getHp(), 30, 450);
        setGoTUI();
        slimeSpawner = new EnemySpawn('s', 5);
        addObject(slimeSpawner, 0, 0);
    }
}