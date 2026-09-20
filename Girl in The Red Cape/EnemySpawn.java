import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EnemySpawn here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnemySpawn extends Actor
{
    private int timer = 0;
    private int spawnDelay;
    
    private int spawnedCount = 0;
    private int maxEnemy;
    private char enemyType;

    public EnemySpawn(char type, int maxCount)
    {
        this.enemyType = Character.toLowerCase(type);
        this.maxEnemy = maxCount;
        setImage((GreenfootImage) null);
        resetTimer();
    }

    @Override
    protected void addedToWorld(World world)
    {
        if (enemyType == 's') {
            int initialSlimes = Math.min(2, maxEnemy);
            for (int i = 0; i < initialSlimes; i++) {
                if (i == 0) spawnSlimeInZone(350, 600);
                else spawnSlimeInZone(750, 1000);
                spawnedCount++;
            }
        } 
        else if (enemyType == 'b') {
            if (spawnedCount < maxEnemy) {
                spawnFromSide();
                spawnedCount++;
            }
        }
    }

    /**
     * Act - do whatever the EnemySpawn wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (spawnedCount < maxEnemy) {
            timer++;
            if (timer >= spawnDelay) {
                spawnFromSide();
                spawnedCount++;
                resetTimer();
            }
        }
    }

    private void resetTimer()
    {
        timer = 0;
        spawnDelay = Greenfoot.getRandomNumber(100) + 80;
    }

    private void spawnSlimeInZone(int minX, int maxX)
    {
        if (getWorld() == null) return;
        
        int range = maxX - minX;
        int randomX = minX + Greenfoot.getRandomNumber(range);
        int spawnY = 475;
        
        Slime slime = new Slime();
        getWorld().addObject(slime, randomX, spawnY);
    }

    private void spawnFromSide()
    {
        if (getWorld() == null) return;
        Enemy enemy;
        if (enemyType == 's') {
            int spawnX = 1150 + Greenfoot.getRandomNumber(50);
            enemy = new Slime();
            getWorld().addObject(enemy, spawnX, 475);
        } 
        else if (enemyType == 'b') {
            int spawnX = 1100 + Greenfoot.getRandomNumber(80);
            int spawnY = 50 + Greenfoot.getRandomNumber(100);
            enemy = new Bat();
            getWorld().addObject(enemy, spawnX, spawnY);
        }
    }

    public boolean isWaveFinished()
    {
        return spawnedCount >= maxEnemy;
    }
}