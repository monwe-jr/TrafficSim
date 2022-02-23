public class Map {
    static private int i;  //intersection
    static private int s;  //segment
    static private int[][] graph;  //used for generation and method implementation
    static private int[][] lanes; //keeps track to how many lanes each intersection or road segment has
    static public int[][] traffic; //keeps track of traffic lights to create realistic functionality
    static private int[][] intersection;  //intersection data
    static private int[][] segment;  //segment data



    Map(int intersections, int segments) {
        this.i = intersections;
        this.s = segments;

    }

    //generates a graph with i nodes and s edges
    public void generate(int i, int s) {

    }


    //keeps graph data protected
    static public int[][] getGraph() {
        return graph;
    }

    //keeps lane data protected
    static public int[][] getLanes(){return lanes; }

    //keeps segment data protected
    static public int[][] getSegments(){return segment; }

    //keeps intersection data protected
    static public int[][] getIntersections(){return intersection; }





}
