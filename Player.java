import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is a subclass of {@link Entity} to be used for storing the data of
 * the player character
 */
public class Player extends Entity {
    KeyHandler keyH = Window.keyH;
    public Entity currentInteraction;
    public int strength = 10;
    public boolean attacking = false;
    public Rectangle attackArea;
    // new Rectangle(8, 16, window.tileSize, window.tileSize);
    public int hasKey = 0;
    public HashMap<Item, Integer> inventory = new HashMap<Item, Integer>(); // this hashMap will be used to store the
                                                                            // player's inventory. Item is the item,
                                                                            // Integer is the quantity of the item.
    public int inventorySize = 10; // default inventory size.
    public BufferedImage image = getImage();
    int tempScreenX = SCREEN_X;
    int tempScreenY = SCREEN_Y;

    public Weapon longSword = new Weapon(new Rectangle(0, 0, Window.TILESIZE * 3, Window.TILESIZE * 1),
            new Rectangle(0, 0, Window.TILESIZE * 3, Window.TILESIZE * 1),
            new Rectangle(0, 0, Window.TILESIZE * 1, Window.TILESIZE * 3),
            new Rectangle(0, 0, Window.TILESIZE, Window.TILESIZE * 3), "Longsword",
            25, 35);

    public boolean isPlayer;

    public Player(Window window, KeyHandler keyH, String name, int level, int health,
            int maxHealth) {
        super(window, name, level, health, maxHealth);
        this.keyH = keyH;
        this.weapon = longSword;
        this.window = window;
        this.image = getImage();
        this.isFriendly = true;
        this.attackArea = this.weapon.hitBoxLeft;
        setDefaultValues();
    }

    public String toString() {
        return String.format("%s, HP: %.0f / %.0f\n", this.getName(), this.getHealth(), this.getMaxHealth());
    }

    public void setDefaultValues() {
        this.hitBoxDefeaultY = 16;
        this.hitBoxDefeaultX = 8;
        this.hitBox = new Rectangle(8, 16, 32, 32);
        this.setWorldX(Window.TILESIZE * 23);
        this.setWorldY(Window.TILESIZE * 21);
        this.setSpeed(4);
        attacking = false;
    }

    private void damageMonster(int index) {
        if (index != 999 && window.monster[index].invincible != true) {

            window.monster[index].setHealth(window.monster[index].getHealth() - calculateDamage(window.monster[index]));
            window.monster[index].invincible = true;
            System.out.println("Hit detected");
            System.out.println(window.monster[index].getHealth());

            if (window.monster[index].getHealth() == 0) { // makes the enemy disappear if it's dead
                window.monster[index] = null;
            }
            // knockback

        }
    }

    public void attacking() {
        spriteCounter++;
        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 80) {
            spriteNum = 2;
            // save hitbox before the attack
            int currentWorldX = this.getWorldX();
            int currentWorldY = this.getWorldY();
            int hitBoxWidth = this.hitBox.width;
            int hitBoxHeight = this.hitBox.height;
            // adjust hitbox for the attack area
            switch (direction) {
                case "up":
                    this.setWorldY(this.getWorldY() - attackArea.height);
                    break;
                case "down":
                    this.setWorldY(this.getWorldY() + attackArea.height);
                    break;
                case "left":
                    this.setWorldX(this.getWorldX() - attackArea.width);
                    break;
                case "right":
                    this.setWorldX(this.getWorldX() + attackArea.width);
                    break;
            }
            // check collision
            damageMonster(window.cDetection.checkAttackEntity(this, window.monster));
            this.setWorldX(currentWorldX);
            this.setWorldY(currentWorldY);
            hitBox.width = hitBoxWidth;
            hitBox.height = hitBoxHeight;
        }
        if (spriteCounter > 80) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public BufferedImage getImage() {
        tempScreenX = SCREEN_X;
        tempScreenY = SCREEN_Y;
        switch (direction) { // handles the direction that the sprite is facing.
            case "up":
                if (!attacking) {
                    if (spriteNum == 1)
                        image = up1;
                    if (spriteNum == 2)
                        image = up2;
                }
                if (attacking) {
                    tempScreenY = SCREEN_Y - Window.TILESIZE;
                    if (spriteNum == 1)
                        image = attackup1;
                    if (spriteNum == 2)
                        image = attackup2;
                }
                break;
            case "down":
                if (!attacking) {
                    if (spriteNum == 1)
                        image = down1;
                    if (spriteNum == 2)
                        image = down2;
                }
                if (attacking) {
                    if (spriteNum == 1)
                        image = attackdown1;
                    if (spriteNum == 2)
                        image = attackdown2;
                }

                break;
            case "left":
                if (!attacking) {
                    if (spriteNum == 1)

                        if (spriteNum == 2)
                            image = left2;
                }
                if (attacking) {
                    tempScreenX = SCREEN_X - Window.TILESIZE;
                    if (spriteNum == 1)
                        image = attackleft1;
                    if (spriteNum == 2)
                        image = attackleft2;
                }

                break;
            case "right":
                if (!attacking) {
                    if (spriteNum == 1)
                        image = right1;
                    if (spriteNum == 2)
                        image = right2;
                }
                if (attacking) {
                    if (spriteNum == 1)
                        image = attackright1;
                    if (spriteNum == 2)
                        image = attackright2;
                }
                break;
        }
        spriteCounter++; // switches between sprite 1 and 2 for the direction.
        if (spriteCounter > 90) { // serves as an idle animation. The player image
            // will change every 12 frames.
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        return image;

    }

    /**
     * Checks if if any bound keys have been pressed, and if so, changes the
     * property direction of the player object
     */
    private void updateKeys() {
        if (keyH.upPressed) {
            this.direction = "up";
        } else if (keyH.downPressed) {
            this.direction = "down";
        } else if (keyH.leftPressed) {
            this.direction = "left";
        } else if (keyH.rightPressed) {
            this.direction = "right";
        } else if (keyH.tabPressed) {
            if (Window.gameState == Window.PLAYSTATE) {
                Window.gameState = Window.MENUSTATE;
            } else if (Window.gameState == Window.MENUSTATE) {
                Window.gameState = Window.PLAYSTATE;
            }
        }
    }

    public void update() {
        if (Window.gameState == Window.PLAYSTATE) {
            this.updateKeys();
            if (this.invincible == true) {
                this.invincibleCounter++;
                if (this.invincibleCounter > 60) {
                    this.invincible = false;
                    this.invincibleCounter = 0;
                }
            }
            if (Window.keyH.spacePressed == true) {
                this.attacking = true;
            }
        }
        if (this.getHealth() == 0) {
            Window.gameState = Window.STARTSTATE;
        }
        if (this.attacking == true) {
            attacking();
        } else {
            switch (direction) {
                case "up":
                    this.attackArea = this.weapon.hitBoxUp;
                    break;
                case "down":
                    this.attackArea = this.weapon.hitBoxDown;
                    break;
                case "left":
                    this.attackArea = this.weapon.hitBoxLeft;
                    break;
                case "right":
                    this.attackArea = this.weapon.hitBoxRight;
                    break;
            }
            // Checking tile colllision
            collisionOn = false;
            window.cDetection.checkTile(this);

            // checking object collision
            pickUpObject(window.cDetection.checkObject(this, true));
            interactNPC(window.cDetection.checkEntity(this, window.npc));
            if (this.getName() != "player") // we don't want to check if the player is colliding with itself.
                window.cDetection.checkPlayer(this);
            contactMonster(window.cDetection.checkEntity(this, window.monster));

            // if collision is false, player can move
            // TODO: ensure that number of keys is limited to 2, and adjust speed
            // accordingly. should multiply speed by half for each key pressed
            if (collisionOn == false && keyH.spacePressed == false) {
                switch (direction) {
                    case "up":
                        this.setWorldY(this.getWorldY() - this.getSpeed());
                        break;
                    case "down":
                        this.setWorldY(this.getWorldY() + this.getSpeed());
                        break;
                    case "left":
                        this.setWorldX(this.getWorldX() - this.getSpeed());
                        break;
                    case "right":
                        this.setWorldX(this.getWorldX() + this.getSpeed());
                        break;
                    default:
                        break;
                }

            }
        }
    }

    public void draw(Graphics2D g2) {
        // DEBUG
        // AttackArea
        tempScreenX = getSCREEN_X() + attackArea.x;
        tempScreenY = getSCREEN_Y() + attackArea.y;
        switch (direction) {
            case "up":
                tempScreenY = getSCREEN_Y() - attackArea.height;
                break;
            case "down":
                tempScreenY = getSCREEN_Y() + Window.TILESIZE;
                break;
            case "left":
                tempScreenX = SCREEN_X - attackArea.width;
                break;
            case "right":
                tempScreenX = SCREEN_X + Window.TILESIZE;
                break;

        }
        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(tempScreenX, tempScreenY, attackArea.width, attackArea.height);
        // Draw player
        g2.drawImage(getImage(), tempScreenX, tempScreenY, null);

    }

    private void interactNPC(int i) {
        if (i != 999) {
            if (keyH.ePressed == true && Window.gameState != Window.BATTLESTATE) {
                Window.gameState = Window.DIALOGUESTATE;
                currentInteraction = window.npc[i];
                keyH.ePressed = false;
            }
        }
    }

    /**
     * @param i the index of the object in {@link window#items}
     */
    private void pickUpObject(int i) {
        if (i != 999) { // Must exclude the obejcts that we don't want picked
            String objectName = Window.items[i].getName();
            switch (objectName) {
                case "chest":
                    break;
                case "key":

                    inventory.put(Window.items[i], 1);
                    Window.items[i] = null;
                    System.out.println("Key obtained.");
                    hasKey++;
                    break;
                case "lockeddoor":
                    if (hasKey > 0) {
                        Window.items[i] = null;
                        hasKey--;
                    }
                    break;

            }
        }
    }

    private void contactMonster(int index) {
        if (index != 999 && invincible == false) {
            invincible = true;
            setHealth(getHealth() - 10);
        }
    }

    /**
     * Upon attacking a monster, calculate the damage that the player will deal to
     * the monster.
     * 
     * @param monster - the monster that the player is hitting
     * @return damage - the damage to the monster
     */
    private int calculateDamage(Entity monster) {
        int damage = 0;
        if (checkHit(monster)) {
            int weaponRoll = ThreadLocalRandom.current().nextInt(weapon.damageLowerBound, weapon.damageUpperBound + 1);
            damage = (int) ((strength * (.05 * weaponRoll)) / (monster.defense));
            System.out.println(damage);
        }
        return damage;
    }

    /**
     * @param monster - the entity that the player is attacking
     * @return hit - if the attack hit or not.
     */
    private boolean checkHit(Entity monster) {
        boolean hit = true;
        return hit;
    }
}
