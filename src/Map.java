import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {
    private int interCount;

    private ArrayList<ArrayList<Segment>> map = new ArrayList<>();
    private ArrayList<Segment> onMap = new ArrayList<>();


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
        if (s.getSegmentLocation().x < interCount && s.getSegmentLocation().y < interCount) {
            int x = s.getSegmentLocation().x;
            map.get(x).add(s);
            onMap.add(s);
        }

    }


    public ArrayList<Segment> getSegments() {
        return onMap;
    }


    public void erase() {
        map.removeAll(map);

    }

    public ArrayList<ArrayList<Segment>> getMap() {
        return map;
    }


}
