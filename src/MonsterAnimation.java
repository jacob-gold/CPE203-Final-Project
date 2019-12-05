import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class MonsterAnimation implements Monster {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int health; //resource count
    private int actionPeriod;
    private int animationPeriod;


    public MonsterAnimation(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int health, int actionPeriod, int animationPeriod){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
        this.resourceLimit = resourceLimit;
        this.health = health;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public String getID(){return this.id;}
    public void setID(String s){this.id = s;}
    public Point getPosition(){return this.position;}
    public List<PImage> getImages(){return this.images;}
    public void setPosition(Point p){
        this.position = p;
    }
    public void setImages(List<PImage> l) {
        this.images = l;
    }
    protected int getImageIndex(){return this.imageIndex;}
    protected int getResourceLimit(){return this.resourceLimit;}
    protected int getHealth(){return this.health;}
    protected int getActionPeriod(){return this.actionPeriod;}
    protected int getAnimationPeriod(){return this.animationPeriod;}

    protected void setHealth(int i){
        this.health += i;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0),
                getAnimationPeriod());
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Monster> fullTarget;
        if(this.getClass() == Fireball.class){
            fullTarget = world.findNearest(this.getPosition(), Mugen.class);
        }
        else {
             fullTarget = world.findNearest(this.getPosition(),
                    Player.class);
        }
        if (fullTarget.isPresent() &&
                moveTo(world, fullTarget.get(), scheduler, imageStore))
        {
            //at atlantis trigger animation
            //((MonsterAnimation)fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
//            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent( this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    protected void nextImage() {
        this.imageIndex = (this.getImageIndex() + 1) % this.getImages().size();
    }

    public abstract boolean moveTo(WorldModel world,
                                   Monster target, EventScheduler scheduler, ImageStore imageStore);

    public abstract Point nextPosition(WorldModel world,
                                       Point destPos);

    public boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
                (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }

    protected static boolean withinBounds(Point p, GridValues[][] grid)
    {
        return p.y >= 0 && p.y < grid.length &&
                p.x >= 0 && p.x < grid[0].length;
    }

    protected static boolean neighbors(Point p1, Point p2)
    {
        return p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;
    }

}