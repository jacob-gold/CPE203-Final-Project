import processing.core.PImage;

import java.io.IOException;
import java.util.List;

public class Mugen extends MonsterAnimation{

    public Mugen(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int health, int actionPeriod, int animationPeriod){
        super(id, position, images, imageIndex, resourceLimit, health, actionPeriod, animationPeriod);
    }

    public Mugen create(){
        return new Mugen(super.getID(), super.getPosition(), super.getImages(), super.getImageIndex(), super.getResourceLimit(), super.getHealth(), super.getActionPeriod(), super.getAnimationPeriod());
    }

public boolean moveTo(WorldModel world,
                      Monster target, EventScheduler scheduler, ImageStore imageStore)
{
    if (this.getPosition().getY() == target.getPosition().getY())
    {
        try {

            MugenAttack g = attack();
            g.setPosition(new Point(this.getPosition().getX()-1, this.getPosition().getY()));
            world.addEntity(g);
            g.scheduleActions( scheduler, world, imageStore);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    else
    {
        Point nextPos = this.nextPosition( world, target.getPosition());

        if (!this.getPosition().equals(nextPos) && !world.getOccupant(nextPos).isPresent())
        {

            //world.moveEntity(this, nextPos);
        }
        return false;
    }
}

    public Point nextPosition(WorldModel world, Point destPos){
        PathingStrategy strategy = new VerticalPathingStrategy();
        GridValues[][] grid = new GridValues[world.numRows][world.numCols];
        List<Point> points = strategy.computePath(this.getPosition(), destPos, p ->  withinBounds(p, grid) && grid[p.y][p.x] != GridValues.OBSTACLE,
                (p1, p2) -> neighbors(p1,p2),
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
        if (points.size() == 0) {
            return this.getPosition();
        }
        return points.get(0);
    }

    public MugenAttack attack() throws IOException {
        return new MugenAttack("ball", this.getPosition(), this.getImages(), 0, 0, getHealth(), getActionPeriod(), getAnimationPeriod());
    }
}
