import processing.core.*;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GaogaAttack extends MonsterAnimation {

    public GaogaAttack(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int health, int actionPeriod, int animationPeriod) throws IOException {
        super(id, position, images, imageIndex, resourceLimit, health, actionPeriod, animationPeriod);

        List<PImage> i = new ArrayList<PImage>();
        try {
            BufferedImage b = ImageIO.read(new File("images/gaogaattack0.png"));
            PImage img = new PImage(b.getWidth(), b.getHeight(), PConstants.ARGB);
            b.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
            img.updatePixels();

            i.add(img);

            b = ImageIO.read(new File("images/gaogaattack1.png"));
            img = new PImage(b.getWidth(), b.getHeight(), PConstants.ARGB);
            b.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
            img.updatePixels();

            i.add(img);

            this.setImages(i);
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public boolean moveTo(WorldModel world, Monster target, EventScheduler scheduler, ImageStore imageStore) {
        return false;
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        return null;
    }

    @Override
    public Monster create() {
        return null;
    }
}
