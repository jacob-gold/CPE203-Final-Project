import processing.core.PImage;

import java.util.List;

public interface Monster {
    Monster create();
    String getID();
    List<PImage> getImages();
    Point getPosition();
    void setPosition(Point p);
    void setImages(List<PImage> l);
}
