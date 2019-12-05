import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BadButNewPathingStrategy
        implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        /* Does not check withinReach.  Since only a single step is taken
         * on each call, the caller will need to check if the destination
         * has been reached.
         */


        List<Point> pointList =
                potentialNeighbors.apply(start)
                .filter(canPassThrough)
                .filter(pt ->
                        !pt.equals(start)
                                && !pt.equals(end)
                                && Math.abs(end.x - pt.x) <= Math.abs(end.x - start.x)
                                && Math.abs(end.y - pt.y) <= Math.abs(end.y - start.y))
//                .limit(1)
                .collect(Collectors.toList());
        for(int i = 0; i < pointList.size(); i++){
            if (pointList.get(i).getY() != start.getY()){
                pointList.remove(i);
                i--;
            }
        }

        return pointList;
    }
}