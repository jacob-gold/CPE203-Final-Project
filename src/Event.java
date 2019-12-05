final class Event
{
   public Action action;
   public long time;
   public Monster entity;

   public Event(Action action, long time, Monster entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }
}
