import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VerticalPathingStrategy implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();

        Node current = new Node(start, null, 0, 0);

        List<Point> neighbors = potentialNeighbors.apply(current.point).
                filter(canPassThrough).
                filter(pt -> !(pt.equals(start) &&
                        !(pt.equals(end)))
                ).collect(Collectors.toList());
        for (Point neighbor: neighbors){
            if(end.y > neighbor.y){
                path.clear();
                path.add(new Point(neighbor.x, neighbor.y+1));
            }
            else if (end.y < neighbor.y){
                path.clear();
                path.add(new Point(neighbor.x, neighbor.y-1));
            }
            else{
                path.clear();
            }
        }
        return path;
    }
}
