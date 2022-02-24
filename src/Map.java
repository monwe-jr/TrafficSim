import java.awt.*;
import java.util.ArrayList;

public class Map {
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


     public void addSegment(Segment s) {

        if (s.getLocation().x < interCount && s.getLocation().y < interCount) {
            int x = s.getLocation().x;
            int y = s.getLocation().y;
            map.get(x).add(new Segment(new Point(x, y)));
        }

    }


     public ArrayList<ArrayList<Segment>> getMap() {
        return map;
    }


}
