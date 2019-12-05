import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   public int numRows;
   public int numCols;
   public Background background[][];
   public Monster occupancy[][];
   public Set<Monster> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Monster[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getCurrentImage(getBackgroundCell(pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   public Background getBackgroundCell(Point pos)
   {
      return this.background[pos.y][pos.x];
   }

   public Monster getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.y][pos.x];
   }

   public  void setOccupancyCell(Point pos, Monster entity)
   {
      this.occupancy[pos.y][pos.x] = entity;
   }

   public void setBackgroundCell(Point pos,
                                        Background background)
   {
      this.background[pos.y][pos.x] = background;
   }

   public PImage getCurrentImage(Object entity)
   {
      if (entity instanceof Background)
      {
         return ((Background)entity).images
                 .get(((Background)entity).imageIndex);
      }
      else if (entity.getClass() == Obstacle.class || entity.getClass() == Heart.class){
         return ((Monster)entity).getImages().get(0);
      }
      else if (entity.getClass() == Player.class){
         return ((Monster)entity).getImages().get(0);
      }
      else if (entity instanceof Monster)
      {
         return ((Monster)entity).getImages().get(((MonsterAnimation)entity).getImageIndex());
      }
      else
      {
         throw new UnsupportedOperationException(
                 String.format("getCurrentImage not supported for %s",
                         entity));
      }
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -MonsterFactory.FISH_REACH; dy <= MonsterFactory.FISH_REACH; dy++)
      {
         for (int dx = -MonsterFactory.FISH_REACH; dx <= MonsterFactory.FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public void tryAddEntity(Monster entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }
      addEntity(entity);
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   public Optional<Monster> nearestEntity(List<Monster> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Monster nearest = entities.get(0);
         int nearestDistance = distanceSquared(nearest.getPosition(), pos);

         for (Monster other : entities)
         {
            int otherDistance = distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public Optional<Monster> findNearest(Point pos, Class kind)
   {
      List<Monster> ofType = new LinkedList<>();
      for (Monster entity : this.entities)
      {
         if (entity.getClass() == kind && entity.getClass() != Fireball.class)
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   public void addEntity(Monster entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public void moveEntity(Monster entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(Monster entity)
   {
      removeEntityAt(entity.getPosition());
   }


   public void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Monster entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   public void setBackground(Point pos,
                                    Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   public Optional<Monster> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public void calcHP(int hp){
      List<Heart> heartList = new ArrayList<Heart>();
      for (Monster m : entities){
         if (m.getClass() == Heart.class){
            heartList.add((Heart)m);
         }
         else{
            continue;
         }
      }
      while (heartList.size() > hp){
         Heart h = heartList.remove(hp);
         removeEntity(h);
         //hp--;
      }
   }
}
