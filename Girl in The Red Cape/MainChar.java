import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.HashSet;
import java.util.Set;

/**
 * Write a description of class MainChar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainChar extends Actor
{
    private MCAnimation animIdle = new MCAnimation("Red_idle.png", 5);
    private MCAnimation animRun = new MCAnimation("Red_Running.png", 8);
    private MCAnimation animJump = new MCAnimation("Red_jumping.png", 4);
    private MCAnimation animFall = new MCAnimation("Red_falling.png", 4);
    private MCAnimation animAttack1 = new MCAnimation("Red_attack_1.png", 6);
    private MCAnimation animAttack2 = new MCAnimation("Red_attack_2.png", 6);
    private MCAnimation animAttack3 = new MCAnimation("Red_attack_3_Dash.png", 7);
    private MCAnimation animHurt = new MCAnimation("Red_hurt.png", 5);
    private MCAnimation animDeath = new MCAnimation("Red_Death.png", 6);

    private int vSpeed = 0;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -15;
    private final int MOVE_SPEED = 6;
    private final int DASH_SPEED = 10;
    private final int KNOCKBACK_SPEED = 4;

    private int dashTimer = 0;
    private final int DASH_COOLDOWN = 30;
    private boolean wasSpaceDown = false;
    private boolean wasShiftDown = false;

    private Set<Enemy> hitPerEnemy = new HashSet<>();

    private int invincibleTimer = 0;
    private final int INVINCIBLE_DURATION = 60;
    private int blinkTimer = 0;

    private boolean isAutoMoving = false;
    private int autoMoveSpeed = 6;
    private boolean isVictoryState = false;

    private int hp = 4;
    private HealthPoint hpBar;

    private boolean isGrounded = true;
    private boolean isAttacking = false;
    private boolean isHurt = false;
    private boolean isDead = false;
    private boolean isFacingRight = true;
    private int currentAttackType = 1;

    public MainChar()
    {
        setImage(animIdle.getFirstFrame(true));
    }

    /**
     * Act - do whatever the MainChar wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (isVictoryState) {
            return;
        }

        if (isDead) {
            setImage(animDeath.animateOnce(isFacingRight, 4, () -> {
                if (getWorld() != null) {
                    getWorld().showText("Game Over", getWorld().getWidth() / 2, 300);
                    Greenfoot.playSound("GameOver.mp3");
                }
                Greenfoot.stop();
            }));
            return;
        }

        applyPhysics();

        if (dashTimer > 0) {
            dashTimer--;
        }

        // Kelola Timer Invincibility
        if (invincibleTimer > 0) {
            invincibleTimer--;
            handleBlinkEffect();
        } else {
            getImage().setTransparency(255);
        }

        if (isHurt) {
            setLocation(getX() + (isFacingRight ? -KNOCKBACK_SPEED : KNOCKBACK_SPEED), getY());
            setImage(animHurt.animateOnce(isFacingRight, 3, () -> isHurt = false));
            return;
        }

        if (invincibleTimer == 0) {
            checkEnemyCollision();
        }

        if (isAutoMoving) {
            setLocation(getX() + autoMoveSpeed, getY());
            setImage(animRun.animateLoop(isFacingRight, 3));
            return;
        }

        handleJump();
        handleAttack();

        if (isAttacking) {
            checkAttackHitbox();

            MCAnimation currentAttackAnim;
            if (currentAttackType == 3) {
                currentAttackAnim = animAttack3;
                setLocation(getX() + (isFacingRight ? DASH_SPEED : -DASH_SPEED), getY());
            } else if (currentAttackType == 2) {
                currentAttackAnim = animAttack2;
            } else {
                currentAttackAnim = animAttack1;
            }

            setImage(currentAttackAnim.animateOnce(isFacingRight, 3, () -> isAttacking = false));
            
            if (!isGrounded && currentAttackType != 3) {
                handleMovement();
            }
        } else if (!isGrounded) {
            setImage((vSpeed < 0) ? animJump.animateLoop(isFacingRight, 3) : animFall.animateLoop(isFacingRight, 3));
            handleMovement();
        } else if (isMoving()) {
            handleMovement();
            setImage(animRun.animateLoop(isFacingRight, 3));
        } else {
            setImage(animIdle.animateLoop(isFacingRight, 4));
        }
    }

    private void handleBlinkEffect()
    {
        blinkTimer++;
        if (blinkTimer % 6 < 3) {
            getImage().setTransparency(60);
        } else {
            getImage().setTransparency(255);
        }
    }

    private void checkEnemyCollision()
    {
        int myBodyRadius = 30;
        
        for (Enemy enemy : getObjectsInRange(60, Enemy.class)) {
            if (enemy.isDying()) {
                continue;
            }

            boolean enemyInFront = isFacingRight ? (enemy.getX() >= getX()) : (enemy.getX() <= getX());
            if (isAttacking && currentAttackType == 3 && enemyInFront) {
                continue;
            }

            int distanceX = Math.abs(getX() - enemy.getX());
            int distanceY = Math.abs(getY() - enemy.getY());
            
            if (distanceX <= (myBodyRadius + enemy.getHitboxRadius()) &&
                distanceY <= (myBodyRadius + enemy.getHitboxRadius())) {
                takeDamage(1);
                break;
            }
        }
    }

    private void checkAttackHitbox()
    {
        int attackRadius = (currentAttackType == 3) ? 80 : 60;

        for (Enemy enemy : getObjectsInRange(attackRadius + 30, Enemy.class)) {
            boolean enemyInFront = isFacingRight ? (enemy.getX() >= getX()) : (enemy.getX() <= getX());
            
            if (enemyInFront && !enemy.isDying()) {
                // Hanya beri damage jika musuh ini belum terdaftar di hitPerEnemy
                if (!hitPerEnemy.contains(enemy)) {
                    enemy.takeDamage(1);
                    hitPerEnemy.add(enemy); // Masukkan musuh ke daftar
                }
            }
        }
    }

    public void enableAutoMove(int speed)
    {
        isAutoMoving = true;
        isFacingRight = true;
        autoMoveSpeed = speed;
    }

    public void disableAutoMove()
    {
        isAutoMoving = false;
        autoMoveSpeed = 0;
    }

    public void setVictoryState(boolean victory)
    {
        this.isVictoryState = victory;
        if (victory) {
            disableAutoMove();
        }
    }

    public void setHealthPoint(HealthPoint hpBar)
    {
        this.hpBar = hpBar;
    }

    public void setHp(int newHp)
    {
        this.hp = newHp;
        if (hpBar != null) {
            hpBar.updateHP(this.hp);
        }
        if (this.hp <= 0) {
            triggerDeath();
        }
    }

    public void takeDamage(int damage)
    {
        if (!isHurt && !isDead && invincibleTimer == 0) {
            hp -= damage;
            Greenfoot.playSound("TakingDamageMC.wav");

            if (hp <= 0) {
                hp = 0;
                triggerDeath();
            } else {
                isHurt = true;
                isAttacking = false;
                invincibleTimer = INVINCIBLE_DURATION;
                animHurt.reset();
            }

            if (hpBar != null) {
                hpBar.updateHP(hp);
            }
        }
    }

    private void triggerDeath()
    {
        isDead = true;
        isAttacking = false;
        isHurt = false;
        animDeath.reset();
        getImage().setTransparency(255);
    }

    public int getHp()
    {
        return hp;
    }

    public boolean isDead()
    {
        return isDead;
    }

    private void handleMovement()
    {
        if (Greenfoot.isKeyDown("right")) {
            setLocation(getX() + MOVE_SPEED, getY());
            isFacingRight = true;
        } else if (Greenfoot.isKeyDown("left")) {
            setLocation(getX() - MOVE_SPEED, getY());
            isFacingRight = false;
        }
    }

    private boolean isMoving()
    {
        return Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("left");
    }

    private void applyPhysics()
    {
        setLocation(getX(), getY() + vSpeed);

        if (getY() >= 450) {
            setLocation(getX(), 450);
            isGrounded = true;
            vSpeed = 0;
        } else {
            isGrounded = false;
            vSpeed += GRAVITY;
        }
    }

    private void handleJump()
    {
        if (Greenfoot.isKeyDown("up") && isGrounded && !isAttacking) {
            vSpeed = JUMP_STRENGTH;
            isGrounded = false;
        }
    }

    private void handleAttack()
    {
        boolean isSpaceDown = Greenfoot.isKeyDown("space");
        boolean isShiftDown = Greenfoot.isKeyDown("shift");

        if (!isAttacking) {
            if (isShiftDown && !wasShiftDown && isGrounded && dashTimer == 0) {
                isAttacking = true;
                hitPerEnemy.clear();
                currentAttackType = 3;
                dashTimer = DASH_COOLDOWN;
                animAttack3.reset();
                Greenfoot.playSound("Dash.mp3");
            } 
            else if (isSpaceDown && !wasSpaceDown) {
                isAttacking = true;
                hitPerEnemy.clear();

                if (!isGrounded || Greenfoot.isKeyDown("up")) {
                    currentAttackType = 2;
                    animAttack2.reset();
                } else {
                    currentAttackType = 1;
                    animAttack1.reset();
                }
                Greenfoot.playSound("SwordSwing.wav");
            }
        }

        wasSpaceDown = isSpaceDown;
        wasShiftDown = isShiftDown;
    }
}