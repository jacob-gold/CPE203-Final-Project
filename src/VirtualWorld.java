import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 1000;
   public static final int VIEW_HEIGHT = 900;
   public static final int TILE_WIDTH = 32;
   public static final int TILE_HEIGHT = 32;
   public static final int WORLD_WIDTH_SCALE = 1;
   public static final int WORLD_HEIGHT_SCALE = 1;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE - 2;
   public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE - 2;

   public static final String IMAGE_LIST_FILE_NAME = "imagelist";
   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;

   public static final String LOAD_FILE_NAME = "world.sav";

   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public static double timeScale = 1.0;

   public ImageStore imageStore;
   public WorldModel world;
   public WorldView view;
   public EventScheduler scheduler;
   PImage background;
   public Player p;

   public long next_time;

   public boolean start = true;
   public boolean redraw = false;
   public boolean end = false;
   public boolean victory = false;


   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.p = Player.getInstance();

      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      background = loadImage("images\\farm2.jpg");
      background.resize(VIEW_WIDTH, VIEW_HEIGHT);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {

      if(start){
         textSize(50);
         background(background);
         rect(140, 270, 760, 355);
         fill(0);
         text("Click the mouse to start the game\nDefeat all of the Big Boys\n" +
                 "Move with the arrow keys and \n" +
                 "press space to shoot a fireball", 150, 320);
         start = false;
      }
      if(redraw){
         p.nextImage();
         int hp = p.getHealth();
         world.calcHP(hp);
         background(background);
         long time = System.currentTimeMillis();
         if (time >= next_time)
         {
            this.scheduler.updateOnTime( time);
            next_time = time + TIMER_ACTION_PERIOD;
         }
         this.view.drawViewport();
         if(p.getHealth() <= 0){
            end=true;
         }
         if(p.getResourceLimit()==0){
            victory=true;
         }
      }
      if(end){
         redraw = false;
         fill(255, 0, 0);
         rect(340, 360, 380, 170);
         fill(0);
         text("Game Over\nClick to Restart", 350, 420);
         end = false;
         setup();
         p.resetHealth();
         p.resetResourceLimit();
      }
      if(victory){
         redraw=false;
         fill(0,255,255);
         rect(250, 320, 380, 270);
         fill(0);
         text("You win!\nClick to Restart", 250, 420);
         victory = false;
         setup();
         p.resetHealth();
         p.resetResourceLimit();
      }

   }

   public void mousePressed(){
      redraw = true;
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;

         }
//         this.view.shiftView( dx, dy);
         Point point = p.getPosition();
         int x = point.getX() + dx;
         int y = point.getY() + dy;
         point = new Point(x, y);
         if(!world.isOccupied(point) && world.withinBounds(point)) {
            this.p.setPosition(point);
         }
      }
      else if (key == ' '){
         p.attack(world, scheduler, imageStore);
      }
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList( DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         Functions.loadImages(in, imageStore, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }


   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         Functions.load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Monster entity : world.entities)
      {
         //Only start actions for entities that include action (not those with just animations)
         if (entity.getClass() == Obstacle.class || entity.getClass() == Heart.class){

         }
         else if (entity.getClass() == Player.class){
         }
         else if (((MonsterAnimation)entity).getActionPeriod() > 0)
            ((MonsterAnimation)entity).scheduleActions( scheduler, world, imageStore);
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
