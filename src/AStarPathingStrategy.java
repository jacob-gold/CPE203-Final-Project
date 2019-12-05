import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();

        HashMap<Point, Node> openList = new HashMap<Point, Node>();
        HashMap<Point, Node> closedList = new HashMap<Point, Node>();
        PriorityQueue<Node> openQueue = new PriorityQueue<Node>(new Comparator<Node>() {
            public int compare(Node x, Node y){
                if(x.f < y.f){return -1;}
                if(x.f > y.f){return 1;}
                return 0;
            }
            public int thenComparing(Node x, Node y){
                if(x.g < y.g){return -1;}
                if(x.g > y.g){return 1;}
                return 0;
            }
        });

        Node startNode = new Node(start, null, 0, /*Math.abs(start.x - end.x) + Math.abs(start.y - end.y)*/
                distance(start, end));

        openList.put(start, startNode);
        openQueue.add(startNode);

        Node current;

        while (!openQueue.isEmpty()){

            current = openQueue.remove();
            openList.remove(current.point);

            if (withinReach.test(current.point, end) /*|| distance(current.point, end) == Math.sqrt(2)/2*/) {
                return createPath(current, path);
            }

            List<Point> neighbors = potentialNeighbors.apply(current.point).
                    filter(canPassThrough).
                    filter(pt -> !(pt.equals(start) &&
                            !(pt.equals(end)))
                    ).collect(Collectors.toList());

            for (Point point : neighbors){

                double g = current.g + 1;
                double h = distance(point, end);
                Node neighbor = new Node(point, current, g, h);

                Node testNode = closedList.get(point);
                Node testNode2 = openList.get(point);

                //checking that points haven't been calculated already
                if (testNode != null && neighbor.f >= testNode.f)
                    continue;
                if (testNode2 != null && neighbor.f >= testNode2.f)
                    continue;

                openList.put(neighbor.point, neighbor);
                openQueue.add(neighbor);

            }
            closedList.put(current.point, current);
        }

        return path;
    }
    public List<Point> createPath(Node node, List<Point> path){
        if (node == null){
            return path;
        }
        if (node.parent != null){
            path.add(0, node.point);
        }
        return createPath(node.parent, path);
    }
    public double distance(Point current, Point end){
        return Math.sqrt(Math.pow(Math.abs(current.x - end.x),2) + Math.pow(Math.abs(current.y - end.y),2));
    }
}
