import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Slime here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Slime extends Enemy
{
    private EnemyAnimation animSlime;

    private int speed = 2;
    private boolean isFacingRight;
    
    private final int MIN_X = 100;
    private final int MAX_X = 1100;

    private boolean isDying = false;

    public Slime()
    {
        hp = 1;
        hitboxRadius = 30;
        
        isFacingRight = Greenfoot.getRandomNumber(2) == 0;
        animSlime = new EnemyAnimation("Slimes.png", 4, 3, 2, 8, 3.5);
        setImage(animSlime.getFirstFrame(isFacingRight));
    }

    /**
     * Act - do whatever the Slime wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (isDying) {
            setImage(animSlime.animateDeath(isFacingRight, 4, () -> {
                if (getWorld() != null) {
                    getWorld().removeObject(this);
                }
            }));
            return;
        }
        patrolHorizontal();
        setImage(animSlime.animateLoop(isFacingRight, 6));
    }

    private void patrolHorizontal()
    {
        if (isFacingRight) {
            setLocation(getX() + speed, getY());
            if (getX() >= MAX_X || isAtEdge()) {
                isFacingRight = false;
            }
        } else {
            setLocation(getX() - speed, getY());
            if (getX() <= MIN_X || isAtEdge()) {
                isFacingRight = true;
            }
        }
    }

    @Override
    public void takeDamage(int damage)
    {
        if (!isDying) {
            hp -= damage;
            if (hp <= 0) {
                isDying = true;
                animSlime.resetDeath();
                Greenfoot.playSound("SlimeDeath.mp3");
            }
        }
    }

    @Override
    public boolean isDying()
    {
        return isDying || hp <= 0;
    }
}