import processing.core.PImage;

import java.util.List;

public class Heart extends StaticMonster {
    public Heart(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

    @Override
    public Monster create() {
        return null;
    }
}
