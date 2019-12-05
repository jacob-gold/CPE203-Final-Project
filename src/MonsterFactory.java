import java.awt.*;

public class MonsterFactory {

    String type;
    String[] properties;
    WorldModel world;
    ImageStore imageStore;

    public static final String PLAYER_KEY = "player";
    public static final int PLAYER_NUM_PROPERTIES = 4;
    public static final int PLAYER_ID = 1;
    public static final int PLAYER_COL = 2;
    public static final int PLAYER_ROW = 3;
    public static final int PLAYER_LIMIT = 4;
    public static final int PLAYER_ACTION_PERIOD = 5;
    public static final int PLAYER_ANIMATION_PERIOD = 6;

    public static final String GAOGA_KEY = "gaoga";
    public static final int GAOGA_NUM_PROPERTIES = 7;
    public static final int GAOGA_ID = 1;
    public static final int GAOGA_COL = 2;
    public static final int GAOGA_ROW = 3;
    public static final int GAOGA_LIMIT = 4;
    public static final int GAOGA_ACTION_PERIOD = 5;
    public static final int GAOGA_ANIMATION_PERIOD = 6;

    public static final String MUGEN_KEY = "mugen";
    public static final int MUGEN_NUM_PROPERTIES = 7;
    public static final int MUGEN_ID = 1;
    public static final int MUGEN_COL = 2;
    public static final int MUGEN_ROW = 3;
    public static final int MUGEN_LIMIT = 4;
    public static final int MUGEN_ACTION_PERIOD = 5;
    public static final int MUGEN_ANIMATION_PERIOD = 6;

    public static final String WARGREY_KEY = "wargrey";
    public static final int WARGREY_NUM_PROPERTIES = 7;
    public static final int WARGREY_ID = 1;
    public static final int WARGREY_COL = 2;
    public static final int WARGREY_ROW = 3;
    public static final int WARGREY_LIMIT = 4;
    public static final int WARGREY_ACTION_PERIOD = 5;
    public static final int WARGREY_ANIMATION_PERIOD = 6;

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 4;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;

    public static final String HEART_KEY = "heart";
    public static final int HEART_NUM_PROPERTIES = 4;
    public static final int HEART_ID = 1;
    public static final int HEART_COL = 2;
    public static final int HEART_ROW = 3;

    public static final int FISH_REACH = 1;

    public static final String BGND_KEY = "background";
    public static final int BGND_NUM_PROPERTIES = 4;
    public static final int BGND_ID = 1;
    public static final int BGND_COL = 2;
    public static final int BGND_ROW = 3;

    public MonsterFactory(String type, String[] properties, WorldModel world, ImageStore imageStore){
        this.type = type;
        this.properties = properties;
        this.world = world;
        this.imageStore = imageStore;
    }

    public boolean parse(){
        switch(type){
            case BGND_KEY:
                return parseBackground(properties, world, imageStore);
            case PLAYER_KEY:
                return parsePlayer(properties, world, imageStore);
            case OBSTACLE_KEY:
                return parseObstacle(properties, world, imageStore);
            case HEART_KEY:
                return parseHeart(properties, world, imageStore);
            case GAOGA_KEY:
                return parseGaoga(properties, world, imageStore);
            case MUGEN_KEY:
                return parseMugen(properties, world, imageStore);
            case WARGREY_KEY:
                return parseWarGrey(properties, world, imageStore);
        }
        return false;
    }

    public static boolean parseBackground(String [] properties,
                                          WorldModel world, ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground( pt,
                    new Background(id, imageStore.getImageList( id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public static boolean parsePlayer(String [] properties, WorldModel world, ImageStore imageStore){

        if (properties.length == PLAYER_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[PLAYER_COL]),
                    Integer.parseInt(properties[PLAYER_ROW]));
            Player p = Player.getInstance();
            p.setPosition(pt);
            p.setImages(imageStore.getImageList(PLAYER_KEY));
            world.tryAddEntity(p);
        }

        return properties.length == PLAYER_NUM_PROPERTIES;
    }

    public static boolean parseObstacle(String [] properties, WorldModel world,
                                        ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Obstacle entity = new Obstacle(properties[OBSTACLE_ID],
                    pt, imageStore.getImageList( OBSTACLE_KEY));
            world.tryAddEntity( entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public static boolean parseHeart(String [] properties, WorldModel world,
                                        ImageStore imageStore)
    {
        if (properties.length == HEART_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[HEART_COL]),
                    Integer.parseInt(properties[HEART_ROW]));
            Heart entity = new Heart(properties[HEART_ID],
                    pt, imageStore.getImageList( HEART_KEY));
            world.tryAddEntity( entity);
        }

        return properties.length == HEART_NUM_PROPERTIES;
    }

    public static boolean parseGaoga(String [] properties, WorldModel world,
            ImageStore imageStore)
        {
            if (properties.length == GAOGA_NUM_PROPERTIES)
            {
                Point pt = new Point(Integer.parseInt(properties[GAOGA_COL]),
                        Integer.parseInt(properties[GAOGA_ROW]));
                Gaoga entity = new Gaoga(properties[GAOGA_ID],
                        pt,
                        imageStore.getImageList(GAOGA_KEY),
                        Integer.parseInt(properties[GAOGA_LIMIT]),
                        0,
                        1,
                        Integer.parseInt(properties[GAOGA_ACTION_PERIOD]),
                        Integer.parseInt(properties[GAOGA_ANIMATION_PERIOD]));

                world.tryAddEntity( entity);
            }

        return properties.length == GAOGA_NUM_PROPERTIES;
    }

    public static boolean parseWarGrey(String [] properties, WorldModel world,
                                     ImageStore imageStore)
    {
        if (properties.length == WARGREY_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[WARGREY_COL]),
                    Integer.parseInt(properties[WARGREY_ROW]));
            WarGrey entity = new WarGrey(properties[WARGREY_ID],
                    pt,
                    imageStore.getImageList(WARGREY_KEY),
                    Integer.parseInt(properties[WARGREY_LIMIT]),
                    0,
                    1,
                    Integer.parseInt(properties[WARGREY_ACTION_PERIOD]),
                    Integer.parseInt(properties[WARGREY_ANIMATION_PERIOD]));

            world.tryAddEntity(entity);
        }

        return properties.length == GAOGA_NUM_PROPERTIES;
    }

    public static boolean parseMugen(String [] properties, WorldModel world,
                                     ImageStore imageStore)
    {
        if (properties.length == MUGEN_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[MUGEN_COL]),
                    Integer.parseInt(properties[MUGEN_ROW]));
            Mugen entity = new Mugen(properties[MUGEN_ID],
                    pt,
                    imageStore.getImageList(MUGEN_KEY),
                    Integer.parseInt(properties[MUGEN_LIMIT]),
                    0,
                    1,
                    Integer.parseInt(properties[MUGEN_ACTION_PERIOD]),
                    Integer.parseInt(properties[MUGEN_ANIMATION_PERIOD]));

            world.tryAddEntity( entity);
        }
        return properties.length == MUGEN_NUM_PROPERTIES;
    }
}
