import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BossProjectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BossFire extends Actor
{
    private int speed = 7;
    private boolean isFacingRight;
    private int hitboxRadius = 20;

    public BossFire(boolean isFacingRight)
    {
        this.isFacingRight = isFacingRight;
        
        GreenfootImage img = new GreenfootImage("projectile.png");
        img.scale((int)(img.getWidth() * 1.5), (int)(img.getHeight() * 1.5));
        
        if (isFacingRight) {
            img.mirrorHorizontally();
        }
        
        setImage(img);
    }

    public void act()
    {
        if (isFacingRight) {
            setLocation(getX() + speed, getY());
        } else {
            setLocation(getX() - speed, getY());
        }

        checkHitboxCollision();
    }

    private void checkHitboxCollision()
    {
        if (getWorld() == null) return;

        for (MainChar mc : getObjectsInRange(hitboxRadius + 20, MainChar.class)) {
            mc.takeDamage(1);
            getWorld().removeObject(this);
            return;
        }

        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }
}