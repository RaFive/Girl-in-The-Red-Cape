import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.function.Supplier;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    protected MainChar mainChar;
    protected HealthPoint playerHpUI;
    protected TUI goTUI;

    private int currentHp = 4;
    private final int maxHp = 4;

    public MyWorld()
    {    
        super(1200, 600, 1);
        setGameBackground("BG_StageBase.png");
        prepare();
    }

    public int getHp()
    {
        return this.currentHp;
    }

    public void setHp(int hp)
    {
        if (hp > maxHp) {
            this.currentHp = maxHp;
        } else if (hp < 0) {
            this.currentHp = 0;
        } else {
            this.currentHp = hp;
        }

        if (this.mainChar != null) {
            this.mainChar.setHp(this.currentHp);
        }
        if (this.playerHpUI != null) {
            this.playerHpUI.updateHP(this.currentHp);
        }
    }

    public void setMainChar(MainChar mc, int hp, int x, int y)
    {
        removeObjects(getObjects(MainChar.class));
        removeObjects(getObjects(HealthPoint.class));

        this.mainChar = mc;
        addObject(this.mainChar, x, y);

        setPlayerHpUI(new HealthPoint());
        setHp(hp);
        
        if (this.playerHpUI != null && this.mainChar != null) {
            this.mainChar.setHealthPoint(this.playerHpUI);
        }
    }

    public void setPlayerHpUI(HealthPoint hpUI)
    {
        this.playerHpUI = hpUI;
        if (this.playerHpUI != null) {
            addObject(this.playerHpUI, 80, 70);
            this.playerHpUI.updateHP(this.currentHp);
        }
    }

    public void setGoTUI()
    {
        removeObjects(getObjects(TUI.class));
        this.goTUI = new TUI();
        addObject(this.goTUI, 1160, 230);
    }

    public void triggerGoTUI()
    {
        if (this.goTUI != null) {
            this.goTUI.triggerGoWithDelay();
        }
    }

    protected void setGameBackground(String imageName)
    {
        GreenfootImage bg = new GreenfootImage(imageName);
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
    }

    public void act()
    {
        checkStageTransition(() -> new Stage01(getHp()), true);
    }

    public void checkStageTransition(Supplier<World> nextStageSupplier, boolean isCleared)
    {
        if (mainChar != null && mainChar.getX() >= getWidth() - 20) {
            if (isCleared) {
                setHp(mainChar.getHp());
                Greenfoot.setWorld(nextStageSupplier.get());
            } else {
                mainChar.setLocation(getWidth() - 25, mainChar.getY());
            }
        }
    }

    private void prepare()
    {
        mainChar = new MainChar();
        mainChar.enableAutoMove(10);
        addObject(mainChar, 30, 450);
    }
}