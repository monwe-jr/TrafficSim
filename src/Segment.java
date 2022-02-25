import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Segment {

    private Point Location;  // this road segment is between intersection x and intersection y
    private ArrayList<Integer> intersections = new ArrayList<>();
    private ArrayList<Integer> lanes = new ArrayList<>();
    private Random num = new Random();

    Segment(Point intersections) {
        this.Location = intersections;
        getIntersections(intersections);
        addLane();
    }


    private void addLane() {
        int random = num.nextInt(3);

        for (int i = 0; i < random; i++) {
            lanes.add(0);
        }

    }


    //fix this
    public int laneLocation(){
        return lanes.indexOf(1);
    }


    private boolean canSwitchLeft(){
        if(laneLocation() !=0){
            return true;
        }
        return false;
    }


    private boolean canSwitchRight(){
        if(laneLocation() != lanes.size()-1){
            return true;
        }
        return false;
    }


    public void switchLeft(){
        if(canSwitchLeft()) {
            int location = laneLocation();
            lanes.set(location, 0);
            lanes.set(location - 1, 1);
        }
    }


    public void switchRight(){
        if(canSwitchRight()) {
            int location = laneLocation();
            lanes.set(location, 0);
            lanes.set(location + 1, 1);
        }
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
