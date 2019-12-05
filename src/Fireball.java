import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Fireball extends MonsterAnimation {

    public Fireball(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int health, int actionPeriod, int animationPeriod){
        super(id, position, images, imageIndex, resourceLimit, health, actionPeriod, animationPeriod);
    }

    public Fireball create(){
        return new Fireball(super.getID(), super.getPosition(), super.getImages(), super.getImageIndex(), super.getResourceLimit(), super.getHealth(), super.getActionPeriod(), super.getAnimationPeriod());
    }

    public boolean moveTo(WorldModel world, Monster target, EventScheduler scheduler, ImageStore imageStore) {

        Point nextPos = this.nextPosition(world, target.getPosition());


        if (world.getOccupant(nextPos).isPresent()){
            Optional<Monster> occupant = world.getOccupant(nextPos);
            Monster m = occupant.get();
            if(m instanceof MonsterAnimation){
                ((MonsterAnimation)m).setHealth(-1);
                scheduler.unscheduleAllEvents(m);
                world.removeEntityAt(m.getPosition());
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
                if(m.getClass() == Mugen.class){
                    Player.getInstance().setResourceLimit(-1);
                }
                return true;
            }
            else if (m instanceof Obstacle){
                scheduler.unscheduleAllEvents(this);
                world.removeEntity(this);
            }
        }
        else{
            if(!this.getPosition().equals(nextPos)){
                if(this.getPosition().getX()>=28){
                    scheduler.unscheduleAllEvents(this);
                    world.removeEntity(this);
                }
                world.moveEntity(this, nextPos);
                this.setPosition(nextPos);
            }
            return false;
        }

        return false;
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
        return new Point(p.getX()+1, p.getY());
    }
}
