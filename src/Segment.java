
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Segment {

    private Direction direction;
    private Point location;  // this road segment is between intersection x and intersection y
    private ArrayList<Integer> intersections = new ArrayList<>();
    private ArrayList<Vehicle> onSegment = new ArrayList<>();
    private Lane SegmentLanes;
    private Random num = new Random();


    Segment(Point intersections, Direction d) {
        this.direction = d;
        this.location = intersections;
        addIntersections(intersections);
        addLane();

    }


    public void addVehicle(Vehicle v, int lane) {
        SegmentLanes.addVehicle(v, lane);

    }

    public void removeVehicle(Vehicle v){
        SegmentLanes.removeVehicle(v);
    }



    public class Lane{
      private Vehicle[] lanes;

        Lane(int i){
            lanes = new Vehicle[i];
            for (int l = 0; l < i; l++) {
                lanes[l] = null;
            }
        }

        public void addVehicle(Vehicle v, int lane){
            if(laneIsEmpty(lane)){
                lanes[lane] = v;
                onSegment.add(v);

            }else{
                System.out.println("Lane is occupied");
            }
        }


        public void removeVehicle(Vehicle v){
            int location = laneLocation(v);
            lanes[location] = null;
            onSegment.remove(v);
            v.removeSegment(v.getSegment());

        }




        private boolean laneIsEmpty(int i){

            if (lanes[i] == null) {
                return true;
            }

            return false;
        }



        public int laneLocation(Vehicle v) {
            for (int i = 0; i < lanes.length; i++) {
                if(lanes[i] == v){
                    return i;
                }
            }

            return -1;
        }



        private boolean canSwitchLeft(Vehicle v) {
            int location = laneLocation(v);

            if (lanes[location-1] == null && location >0 ) {
                return true;
            }
            return false;
        }

        private boolean canSwitchRight(Vehicle v) {
            int location = laneLocation(v);

            if (lanes[location-1] == null && location < lanes.length -1) {
                return true;
            }
            return false;
        }


        public void switchLeft(Vehicle v) {

            if (canSwitchLeft(v)) {
                int location = laneLocation(v);
                lanes[location] = null;
                lanes[location-1] = v;

            }
        }


        public void switchRight(Vehicle v) {

            if (canSwitchRight(v)) {
                int location = laneLocation(v);
                lanes[location] = null;
                lanes[location+1] = v;

            }
        }

    }


    /**
     * Assigns road segment a random number of lanes in the range 1-3
     */
    private void addLane() {
        int random = num.nextInt(3-1 + 1) + 1;
        SegmentLanes = new Lane(random);

    }


    /**
     * Returns the direction of a segment
     * @return
     */
    public Direction getDirection(){
        return direction;
    }



    public Point getSegmentLocation() {
        return location;
    }

    private void addIntersections(Point p) {
        intersections.add(p.x);
        intersections.add(p.y);
    }


    //segment is one way
    public boolean oneWay(Map m) {
        Point toFind = new Point(location.y, location.x);

        for (int i = 0; i < m.getMap().get(location.y).size(); i++) {
            if (m.getMap().get(location.y).get(i).getSegmentLocation().equals(toFind)) {
                return false;
            }
        }

        return true;
    }


    //from this segment, can we turn at intersection 'ID'
    public boolean canTurn(Map m, int ID) {
        ArrayList<Segment> roads = m.getMap().get(ID);

        for (int i = 0; i < roads.size(); i++) {
            if(roads.get(i).getSegmentLocation().x == location.y && !direction.equals(direction,roads.get(i).getDirection()) && !roads.get(i).getSegmentLocation().equals(new Point(location.y,location.x) )){
                return true;
            }
        }


        return false;
    }


    public ArrayList<Integer> getIntersections() {
        return intersections;
    }


}
