    import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor
{
    protected int hp = 0;
    protected int hitboxRadius = 20;

    public void takeDamage(int damage)
    {
        hp -= damage;
        if (hp <= 0 && getWorld() != null) {
            World currentWorld = getWorld();
            
            // Hapus musuh dari world
            currentWorld.removeObject(this);

            // Cek kondisi stage cleared via method stage masing-masing
            boolean isCleared = false;
            if (currentWorld instanceof Stage01) {
                isCleared = ((Stage01) currentWorld).isStageCleared();
            } else if (currentWorld instanceof Stage02) {
                isCleared = ((Stage02) currentWorld).isStageCleared();
            }

            // Pemicu TUI hanya jika GELOMBANG MUSUH SUDAH BENAR-BENAR HABIS
            if (isCleared && currentWorld instanceof MyWorld) {
                ((MyWorld) currentWorld).triggerGoTUI();
            }
        }
    }

    public int getHitboxRadius()
    {
        return hitboxRadius;
    }

    public int getHp()
    {
        return hp;
    }

    public boolean isDying()
    {
        return hp <= 0;
    }
}