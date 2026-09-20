import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StageFinal here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stage02 extends MyWorld
{
    private EnemySpawn slimeSpawner;
    private EnemySpawn batSpawner;

    public Stage02(int hp)
    {
        super();
        setHp(hp);
        setGameBackground("BG_Stage02.png");
        prepareStage02();
    }

    public void act()
    {
        checkStageTransition(() -> new StageFinal(mainChar.getHp()), isStageCleared());
    }

    public boolean isStageCleared()
    {
        boolean noEnemiesLeft = getObjects(Enemy.class).isEmpty();
        boolean slimeFinished = (slimeSpawner != null) && slimeSpawner.isWaveFinished();
        boolean batFinished = (batSpawner != null) && batSpawner.isWaveFinished();        
        return noEnemiesLeft && slimeFinished && batFinished;
    }

    private void prepareStage02()
    {
        setMainChar(new MainChar(), getHp(), 30, 450);
        setGoTUI();
        slimeSpawner = new EnemySpawn('s', 5);
        addObject(slimeSpawner, 0, 0);
        batSpawner = new EnemySpawn('b', 4);
        addObject(batSpawner, 0, 0);
    }
}