public class Node {

    Point point;
    Node parent;
    double g = 0;
    double h = 0;
    double f = 0;
    public Node(Point point, Node parent, double g, double h) {
        this.point = point;
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = this.g + this.h;
    }

    public double getF(){
        return f;
    }

    public boolean equals(Object other)
    {
        if ((other == null)){
            return false;
        }
        if (!(this.getClass() == other.getClass())){
            return false;
        }
        Node n = (Node)other;
        return this.point == n.point && this.g == n.g && this.h == n.h && this.f == n.f;
    }


//    @Override
//    public int compareTo(Object o) {
//        if (this.f < ((Node)o).f){
//            return -1;
//        }
//        if (this.f > ((Node)o).f){
//            return 1;
//        }
//        return 0;
//    }
}
