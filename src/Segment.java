import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Segment {

    Point Location;  // this road segment is between intersection x and intersection y
    private ArrayList<Integer> intersections = new ArrayList<>();
    ArrayList<Lane> lanes;

    Segment(Point intersections) {
        this.Location = intersections;
        getIntersections(intersections);
    }


    public Point getLocation() {
        return Location;
    }


    private void getIntersections(Point p) {
        intersections.add(p.x);
        intersections.add(p.y);
    }


    public boolean oneWay(Map m, Point p) {
        Point toFind = new Point(p.y, p.x);

        for (int i = 0; i < m.getMap().get(p.y).size(); i++) {
            if (m.getMap().get(p.y).get(i).getLocation() == toFind) {
                return true;
            }
        }

        return false;
    }


    public ArrayList<Integer> getIntersections() {
        return intersections;
    }


}
