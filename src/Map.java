import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable{
     private int interCount;

    private ArrayList<ArrayList<Segment>> map = new ArrayList<>();


    Map(int intersections) {
        this.interCount = intersections;
        generate(intersections);

    }


     public void generate(int intersections) {
        
        for (int i = 0; i < intersections; i++) {
            map.add(new ArrayList<Segment>());
        }

    }

    public String toString() {
        //get all intersections in an ArrayList
        //traverse through ArrayList find one with the highest Segments connected
        //Dictionary to map Points to Intersections, set target as (0,0)
        //if North Segment (previousx, previousy+1) etc
        //find lowest x and lowest y, subtract that from every point
        // 0 is empty, 6 is Intersection, 8 is Segment
        /* example
         * 00686
         * 00808
         * 68686
         * 00808
         * 68686
         */
        // for each possible coordinate check is it an intersection segment or empty
        return "";
    }

     public void addSegment(Segment s) {
        if (s.getSegmentLocation().x < interCount && s.getSegmentLocation().y < interCount) {
            int x = s.getSegmentLocation().x;
            map.get(x).add(s);
        }

    }





     public ArrayList<ArrayList<Segment>> getMap() {
        return map;
    }


}
