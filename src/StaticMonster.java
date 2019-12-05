import processing.core.PImage;

import java.util.List;

public abstract class StaticMonster implements Monster {
    String id;
    Point position;
    List<PImage> images;

    public StaticMonster(String id, Point position, List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public String getID(){return this.id;}
    public Point getPosition(){return this.position;}
    public List<PImage> getImages(){return this.images;}
    public void setPosition(Point p){
        this.position = p;
    }
    public void setImages(List<PImage> l) {
        this.images = l;
    }
}
