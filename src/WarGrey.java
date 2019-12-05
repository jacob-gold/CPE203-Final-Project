import processing.core.PImage;

import java.io.IOException;
import java.util.List;

public class WarGrey extends MonsterAnimation {

    public WarGrey(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int health, int actionPeriod, int animationPeriod){
        super(id, position, images, imageIndex, resourceLimit, health, actionPeriod, animationPeriod);
    }

    public WarGrey create(){
        return new WarGrey(super.getID(), super.getPosition(), super.getImages(), super.getImageIndex(), super.getResourceLimit(), super.getHealth(), super.getActionPeriod(), super.getAnimationPeriod());
    }

    public boolean moveTo(WorldModel world,
                          Monster target, EventScheduler scheduler, ImageStore imageStore)
    {
        if (adjacent(this.getPosition(), target.getPosition()))
        {
            try {
                WarGreyAttack g = attack();
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                world.addEntity(g);
                g.scheduleActions( scheduler, world, imageStore);

                Player.getInstance().setHealth(-1);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }
        else
        {
            Point nextPos = this.nextPosition( world, target.getPosition());

            if (!this.getPosition().equals(nextPos) && !world.getOccupant(nextPos).isPresent())
            {
//                Optional<Monster> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent())
//                {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
                if(this.getHealth() > 0) {
                    world.moveEntity(this, nextPos);
                }
            }
            return false;
        }
    }

    public Point nextPosition(WorldModel world, Point destPos){
        PathingStrategy strategy = new AStarPathingStrategy();
        GridValues[][] grid = new GridValues[world.numRows][world.numCols];
        List<Point> points = strategy.computePath(this.getPosition(), destPos, p ->  withinBounds(p, grid) && grid[p.y][p.x] != GridValues.OBSTACLE,
                (p1, p2) -> neighbors(p1,p2),
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
        if (points.size() == 0) {
            return this.getPosition();
        }
        return points.get(0);
    }

    public WarGreyAttack attack() throws IOException {
        return new WarGreyAttack("wargreyattack", this.getPosition(), this.getImages(), 0, 0, getHealth(), getActionPeriod(), getAnimationPeriod());
    }
}
