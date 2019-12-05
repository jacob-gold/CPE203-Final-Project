import processing.core.PImage;

import java.util.List;

public class Obstacle extends StaticMonster {


    public Obstacle(String id, Point position,
                 List<PImage> images){
        super(id, position, images);
    }


    public Obstacle create()
    {
        return new Obstacle(super.getID(), super.getPosition(), super.getImages());
    }

}
