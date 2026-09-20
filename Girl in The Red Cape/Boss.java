import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Boss here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Boss extends Enemy
{
    private BossAnimation animBoss;
    private HealthPoint hpBar;
    
    private int maxHp = 10;
    private int speed = 3;
    private boolean isFacingRight = false;
    
    private int startY;
    private int hoverRange = 10;
    private int hoverDir = 1;

    private int moveDir = -1;
    private int changeDirTimer = 0;
    private int changeDirInterval;

    private boolean isHurt = false;
    private boolean isAttacking = false;
    private boolean isDying = false;
    private boolean hasShotProjectile = false;

    private int hurtTimer = 0;
    private final int HURT_DURATION = 15;

    private int attackTimer = 0;
    private final int ATTACK_COOLDOWN = 120;

    public Boss()
    {
        hp = maxHp;
        hitboxRadius = 50; 
        
        animBoss = new BossAnimation("Boss-Fly.png", "Boss-Hurt.png", "Boss-Att.png", "Boss-Die.png", 2.5);
        setImage(animBoss.getFirstFrame(isFacingRight));
        
        resetChangeDirInterval();
    }

    @Override
    protected void addedToWorld(World world)
    {
        startY = getY();
    }

    public void setHpBar(HealthPoint hpBar)
    {
        this.hpBar = hpBar;
        if (this.hpBar != null) {
            this.hpBar.updateBossHP(this.hp);
        }
    }

    public void act()
    {
        if (isDying) {
            setImage(animBoss.animateDeath(isFacingRight, 4, () -> {
                if (getWorld() != null) {
                    getWorld().removeObject(this); // Cukup hapus objek boss dari world
                }
            }));
            return;
        }

        updateFacingDirection();

        // 2. Animasi Hurt / Hit Reaction
        if (isHurt) {
            hurtTimer--;
            
            int knockbackDir = isFacingRight ? -3 : 3;
            setLocation(getX() + knockbackDir, getY());

            if (hurtTimer % 4 < 2) {
                getImage().setTransparency(100);
            } else {
                getImage().setTransparency(255);
            }

            setImage(animBoss.animateOnceHurt(isFacingRight, 3, null));

            if (hurtTimer <= 0) {
                isHurt = false;
                getImage().setTransparency(255);
            }
            return;
        }

        if (isAttacking) {
            if (animBoss.getCurrentAttackFrame() == 4 && !hasShotProjectile) {
                shootProjectile();
                hasShotProjectile = true;
            }

            setImage(animBoss.animateAttack(isFacingRight, 4, () -> {
                isAttacking = false;
                hasShotProjectile = false;
            }));
            return;
        }

        // 4. Patroli Normal & Timer Serangan
        patrolRandomHorizontal();
        setImage(animBoss.animateLoop(isFacingRight, 5));

        attackTimer++;
        if (attackTimer >= ATTACK_COOLDOWN) {
            isAttacking = true;
            hasShotProjectile = false;
            animBoss.resetAttack();
            attackTimer = 0;
        }
    }

    private void updateFacingDirection()
    {
        World world = getWorld();
        if (world != null) {
            MainChar mc = (MainChar) world.getObjects(MainChar.class).stream().findFirst().orElse(null);
            if (mc != null) {
                isFacingRight = (mc.getX() > getX());
            }
        }
    }

    private void patrolRandomHorizontal()
    {
        int newY = getY() + hoverDir;
        if (Math.abs(newY - startY) > hoverRange) {
            hoverDir *= -1;
        }

        changeDirTimer++;
        if (changeDirTimer >= changeDirInterval) {
            moveDir = (Greenfoot.getRandomNumber(2) == 0) ? 1 : -1;
            resetChangeDirInterval();
        }

        if (getX() <= 150) {
            moveDir = 1;
        } else if (getX() >= 1050) {
            moveDir = -1;
        }

        setLocation(getX() + (moveDir * speed), newY);
    }

    private void resetChangeDirInterval()
    {
        changeDirTimer = 0;
        changeDirInterval = 60 + Greenfoot.getRandomNumber(90);
    }

    private void shootProjectile()
    {
        if (getWorld() != null) {
            int offsetX = isFacingRight ? 50 : -50;
            BossFire projectile = new BossFire(isFacingRight);
            getWorld().addObject(projectile, getX() + offsetX, getY() + 10);
            Greenfoot.playSound("Fireball.mp3");
        }
    }

    @Override
    public void takeDamage(int damage)
    {
        if (!isDying && !isHurt) {
            hp -= damage;
            
            if (hpBar != null) {
                hpBar.updateBossHP(hp);
            }

            if (hp <= 0) {
                isDying = true;
                isAttacking = false;
                isHurt = false;
                getImage().setTransparency(255);
                animBoss.resetDeath();
                Greenfoot.playSound("BossDeath.mp3");
            } else {
                isHurt = true;
                isAttacking = false;
                hurtTimer = HURT_DURATION;
                animBoss.resetHurt();
                Greenfoot.playSound("TakingDamageBoss.mp3");
            }
        }
    }

    @Override
    public boolean isDying()
    {
        return isDying || hp <= 0;
    }
}