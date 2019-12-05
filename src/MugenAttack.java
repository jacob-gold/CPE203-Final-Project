import processing.core.PConstants;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MugenAttack extends MonsterAnimation {

    public MugenAttack(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int health, int actionPeriod, int animationPeriod){
        super(id, position, images, imageIndex, resourceLimit, health, actionPeriod, animationPeriod);

        List<PImage> i = new ArrayList<PImage>();
        try {
            BufferedImage b = ImageIO.read(new File("images/mugenattack0.png"));
            PImage img = new PImage(b.getWidth(), b.getHeight(), PConstants.ARGB);
            b.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
            img.updatePixels();

            i.add(img);

            for(int index = 1; index <= 7; index++ ){
                b = ImageIO.read(new File("images/mugenattack" + index + ".png"));
                img = new PImage(b.getWidth(), b.getHeight(), PConstants.ARGB);
                b.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
                img.updatePixels();

                i.add(img);
            }

            this.setImages(i);
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    public MugenAttack create(){
        return new MugenAttack(super.getID(), super.getPosition(), super.getImages(), super.getImageIndex(), super.getResourceLimit(), super.getHealth(), super.getActionPeriod(), super.getAnimationPeriod());
    }

//    public boolean adjacent(Point p1, Point p2){
//        return p1.y == p2.y && Math.abs(p1.x - p2.x) == 1;
//    }

    public boolean moveTo(WorldModel world, Monster target, EventScheduler scheduler, ImageStore imageStore) {

        Point nextPos = this.nextPosition(world, target.getPosition());


//        if (world.getOccupant(nextPos).isPresent()){
//            Optional<Monster> occupant = world.getOccupant(nextPos);
//            Monster m = occupant.get();
            if(adjacent(Player.getInstance().getPosition(), this.getPosition())){
                Player.getInstance().setHealth(-1);
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
                return true;
            }
//            else if (m instanceof Obstacle){
//                scheduler.unscheduleAllEvents(this);
//                world.removeEntity(this);
//            }
//            else if (m instanceof MugenAttack){
//                scheduler.unscheduleAllEvents(this);
//                world.removeEntity(this);
//            }
//        }
//        else{
            if(!this.getPosition().equals(nextPos) && world.isOccupied(nextPos)== false){
                world.moveEntity(this, nextPos);
                this.setPosition(nextPos);
            }
            if(this.getPosition().getX()==0){
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);
            }
            return false;
 //       }
    }
    public Point nextPosition(WorldModel world, Point destPos){
//        PathingStrategy strategy = new SingleStepPathingStrategy();
//        GridValues[][] grid = new GridValues[world.numRows][world.numCols];
//        List<Point> points = strategy.computePath(this.getPosition(), destPos, p ->  withinBounds(p, grid) && grid[p.y][p.x] != GridValues.OBSTACLE,
//                (p1, p2) -> neighbors(p1,p2),
//                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
//        if (points.size() == 0) {
//            return this.getPosition();
//        }
//        return points.get(0);
        Point p = this.getPosition();
        return new Point(p.getX()-1, p.getY());
    }
}
