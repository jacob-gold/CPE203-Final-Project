import processing.core.PImage;

import java.util.List;

public class Player implements Monster{

    private String id = "player";
    private Point position;
    private List<PImage> images;
    private int imageIndex = 0;
    private int resourceLimit = 6;
    private int health = 5; //resource count
    private int actionPeriod = 10000;
    private int animationPeriod = 10000;
    public boolean swap = false;

    private Player(){}

    public Player create(){return thePlayer;}
    public static Player getInstance(){return thePlayer;}

    @Override
    public String getID() {
        return id;
    }

    @Override
    public List<PImage> getImages() {
        return images;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point p) {
        this.position = p;
    }

    @Override
    public void setImages(List<PImage> l) {
        this.images = l;
    }

    public int getImageIndex(){
        return this.imageIndex;
    }
    public void setImageIndex(int i){
        this.imageIndex = i;
    }

    public int getHealth(){return this.health;}
    public void setHealth(int i){ this.health += i;}

    public void resetHealth(){this.health=5;}

    public void nextImage() {
        PImage temp = images.get(0);
        images.remove(0);
        images.add(temp);
    }

    public int getResourceLimit(){return this.resourceLimit;}
    public void setResourceLimit(int i){this.resourceLimit += i;}
    public void resetResourceLimit(){this.resourceLimit = 6;}

    private static Player thePlayer = new Player();

    public void changePosition(int dx, int dy){
        int x = position.getX();
        int y = position.getY();
        this.position = new Point(x+dx, y+dy);
    }


    public void attack(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        Point p = new Point(this.position.getX()+1, this.position.getY());
        if(world.isOccupied(p)) {
            return;
        }
        else {
            Fireball f = new Fireball("fireball", p, imageStore.getImageList("fireball"), 0, 4, 1, 992, 100);
            world.addEntity(f);
            f.scheduleActions(scheduler, world, imageStore);
        }
    }

}
