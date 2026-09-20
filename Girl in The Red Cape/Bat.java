import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Bat here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bat extends Enemy
{
    private EnemyAnimation animFly;
    
    private int speed = 4;
    private int fallSpeed = 3;
    private boolean isFacingRight = false;
    private int targetY = 450; 
    private boolean hasLanded = false;

    public Bat()
    {
        hp = 1;
        hitboxRadius = 20;
        animFly = new EnemyAnimation("Bat-Run.png", 8, 2.0);
        setImage(animFly.getFirstFrame(isFacingRight));
    }

    /**
     * Act - do whatever the Bat wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        moveAndDespawn();
        setImage(animFly.animateLoop(isFacingRight, 4));
    }

    private void moveAndDespawn()
    {
        if (!hasLanded) {
            setLocation(getX() - (speed / 2), getY() + fallSpeed);
            if (getY() >= targetY) {
                setLocation(getX(), targetY);
                hasLanded = true;
            }
        } else {
            setLocation(getX() - speed, getY());
        }

        if (getX() <= 10 || isAtEdge()) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }

    @Override
    public void takeDamage(int damage)
    {
        hp -= damage;
        if (hp <= 0) {
            Greenfoot.playSound("BatDeath.mp3");
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }
}