import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * This class handles the drawing of the various items in this program.
 * To create a new item, call the constructor in the {@link AssetSetter} class.
 */
public class Item {

    protected final Rectangle HITBOX = new Rectangle(0, 0, 24, 24);
    protected final Integer HITBOXDEFAULTX = 0;
    protected final Integer HITBOXDEFAULTY = 0;
    protected BufferedImage image;

    protected String name;
    protected boolean collision = false;
    protected Integer worldX, worldY;
    protected final UtilityTools uTool = new UtilityTools();

    public Item(Window window, String name, boolean collision, int worldX, int worldY) {
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.collision = collision;
        this.image = getImage();
    }

    /** retrieves and scales the image to be used for this item.
     * @return the scaled image of this {@link Item}.
     */
    public BufferedImage getImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("assets/" + name +
                    ".png"));
            image = uTool.scaleImage(image, Window.tileSize, Window.tileSize);
            System.out.println("Loaded assets for item " + name);
        } catch (IOException e) {
            System.err.println("Error getting image for item: " + this.getName());
            e.printStackTrace();
        }
        return image;

    }

    /**
     * 
     * @return the name of the {@link Item}. 
     */
    public String getName() {
        return this.name;
    }

    public void draw(Graphics2D g2, Window window) {
        int screenX = worldX - Window.player.getWorldX() + Window.player.SCREEN_X;
        int screenY = worldY - Window.player.getWorldY() + Window.player.SCREEN_Y;
        if (worldX + Window.tileSize > Window.player.getWorldX() - Window.player.SCREEN_X &&
                worldX - Window.tileSize < Window.player.getWorldX() + Window.player.SCREEN_X &&
                worldY + Window.tileSize > Window.player.getWorldY() - Window.player.SCREEN_Y &&
                worldY - Window.tileSize < Window.player.getWorldY() + Window.player.SCREEN_Y) // only render tiles in the
                                                                                          // camera view
            g2.drawImage(image, screenX, screenY, Window.tileSize, Window.tileSize, null);
    }
}
