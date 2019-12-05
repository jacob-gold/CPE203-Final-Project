public class Activity extends Action {



    public Activity(MonsterAnimation entity, WorldModel world, ImageStore imageStore, int repeatCount){
        super(entity, world, imageStore, repeatCount);
    }

    public static Activity createActivityAction(MonsterAnimation entity, WorldModel world,
                                                ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }

    public void executeAction(EventScheduler scheduler){
        this.entity.executeActivity(this.world, this.imageStore, scheduler);
    }
}
