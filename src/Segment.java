
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Segment {

    final Direction direction;
    final Point location;  // this road segment is between intersection x and intersection y
    private ArrayList<Integer> intersections = new ArrayList<>();
    private ArrayList<Vehicle> onSegment = new ArrayList<>();
    private Lane segmentLanes;
    private Random num = new Random();


    Segment(Point intersections, Direction d) {
        this.direction = d;
        this.location = intersections;
        addIntersections(intersections);
        addLane();

    }


    public void addVehicle(Vehicle v, int lane) {
        segmentLanes.addVehicle(v, lane);

    }

    public void removeVehicle(Vehicle v) {
        segmentLanes.removeVehicle(v);
    }

    public boolean atEnd(Vehicle v) {
        if (segmentLanes.atEnd(v)) {
            return true;
        } else {
            return false;
        }

    }


    public Lane getLane(){
        return segmentLanes;
    }


    public class Lane {
        private Vehicle[][] lanes;
      private  int laneCount;
      private  int segmentLength = 5;

        Lane(int i) {
            this.laneCount = i;
            lanes = new Vehicle[segmentLength][i];
            for (int k = 0; k < segmentLength; k++) {
                for (int l = 0; l < i; l++) {
                    lanes[l] = null;
                }
            }
        }

        /**
         * Adds a vehicle to a segment but adding it to a lane. Note that vehicles are only added at the beginning of segments
         * @param v the vehicle we want to add
         * @param lane
         */
        public void addVehicle(Vehicle v, int lane) {
            if (laneIsEmpty(new Point(0,lane))) {
                lanes[0][lane] = v;
                onSegment.add(v);


            } else {
                System.out.println("Lane is occupied");
            }
        }


        public void removeVehicle(Vehicle v) {
            Point location =  getIndex(v);
            lanes[location.x][location.y] = null;
            onSegment.remove(v);
            v.removeSegment(v.getSegment());

        }

        /**
         * gets the location of a vehicle in the lanes array
         */
        public Point getIndex(Vehicle v) {

            if (onSegment.contains(v)) {
                for (int i = 0; i < segmentLength; i++) {
                    for (int j = 0; j < laneCount; j++) {
                        if (lanes[i][j] == v) {
                            return new Point(i, j);

                        }
                    }

                }
            } else {

                System.out.println("Vehicle is not on segment!");

            }
            return null;

        }


        /**
         * Checks if a vehicle is at the end of a segment
         *
         * @param v Vehicle to be checked
         * @return
         */
        public boolean atEnd(Vehicle v) {
            for (int i = 0; i < laneCount; i++) {
                if (v == lanes[4][i]) {
                    return true;
                }
            }
            return false;
        }


        private boolean laneIsEmpty(Point p) {

            if (lanes[p.x][p.y] == null) {
                return true;
            }

            return false;
        }


        public int laneLocation(Vehicle v) {

            for (int i = 0; i < segmentLength; i++) {
                for (int j = 0; j < lanes.length; j++) {
                    if (lanes[i][j] == v) {
                        return j;
                    }
                }
            }

            return -1;
        }


        /**
         * Checks if left lane is available
         * @param v Vehicle that want's to change lane
         * @return
         */
        private boolean canSwitchLeft(Vehicle v) {
            Point location = getIndex(v);

            if (location.y > 0) {
                if (lanes[location.x][location.y - 1] == null) {
                    return true;
                }
            }
            return false;
        }


        /**
         *Checks if right lane is available
         * @param v Vehicle that want's to change lane
         * @return
         */
        private boolean canSwitchRight(Vehicle v) {
            Point location = getIndex(v);

            if (location.y < laneCount -1) {
                if (lanes[location.x][location.y + 1] == null) {
                    return true;
                }

            }
            return false;
        }


        /**
         *
         * @param v
         */
        public void switchLeft(Vehicle v) {
            if (canSwitchLeft(v)) {
                Point location = getIndex(v);
                lanes[location.x][location.y] = null;
                lanes[location.x][location.y-1] = v;

            }
        }


        public void switchRight(Vehicle v) {

            if (canSwitchRight(v)) {
                Point location = getIndex(v);
                lanes[location.x][location.y] = null;
                lanes[location.x][location.y+1] = v;

            }
        }

        /**
         * Checks if Lane has the same or more amount of 'lanes'
         * @param l
         */
        public boolean compatible(int l){

            if(l >= laneCount){
                return true;
            }else{
                return false;
            }

        }


        public int getLaneCount(){
            return laneCount;
        }



    }


    /**
     * Assigns road segment a random number of lanes in the range 1-3
     */
    private void addLane() {
        int random = num.nextInt(3 - 1 + 1) + 1;
        segmentLanes = new Lane(random);

    }


    /**
     * Returns the direction of a segment
     *
     * @return
     */
    public Direction getDirection() {
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


    //from this segment, can we turn at intersection the upcoming intersection
    public boolean canTurn(Map m) {
        ArrayList<Segment> roads = m.getMap().get(location.y);

        for (int i = 0; i < roads.size(); i++) {
            if (roads.get(i).getSegmentLocation().x == location.y && !direction.equals(direction, roads.get(i).getDirection()) && !roads.get(i).getSegmentLocation().equals(new Point(location.y, location.x))) {
                return true;
            }
        }


        return false;
    }


    public ArrayList<Integer> getIntersections() {
        return intersections;
    }


}
