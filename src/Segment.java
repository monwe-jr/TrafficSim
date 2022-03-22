import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Segment implements Serializable {

    final Direction direction;
    final Point location;  // this road segment is between intersection x and intersection y
    private ArrayList<Integer> intersections = new ArrayList<>(); //intersections connected by the segment
    private ArrayList<Vehicle> onSegment = new ArrayList<>(); //vehicles on the segment
    private Lane segmentLanes;


    Segment(Point intersections, Direction d, int laneCount, int segLength) {
        this.direction = d;
        this.location = intersections;
        segmentLanes = new Lane(laneCount, segLength);
        addIntersections(intersections);
    }

    public int laneCount() {
        return segmentLanes.getLaneCount();
    }


    public int segmentLength() {
        return segmentLanes.getSegmentLength();
    }

    public boolean canInsert(Vehicle v) {
        return segmentLanes.canInsert(v);
    }

    public boolean canAdd(Vehicle v, int lane) {
        return segmentLanes.canAdd(v, lane);
    }


    public ArrayList<Integer> alternateInserts(Vehicle v) {
        return segmentLanes.alternateInserts(v);
    }

    public void addVehicle(Map m, Vehicle v, int lane) {
        segmentLanes.addVehicle(m, v, lane);


    }

    public ArrayList<Vehicle> getVictims(Vehicle atFault, int lane){
        return segmentLanes.getVictims(atFault,lane);
    }


    public void removeVehicle(Vehicle v) {
        segmentLanes.removeVehicle(v);
    }

    public void moveVehicle(Point p) {
        segmentLanes.moveVehicle(p);
    }


    public int laneLocation(Vehicle v) {
        return segmentLanes.laneLocation(v);
    }


    public boolean compatible(int l) {
        return segmentLanes.compatible(l);
    }


    public boolean atEnd(Vehicle v) {
        return segmentLanes.atEnd(v);

    }

    public boolean isEmpty(Point p) {
        return segmentLanes.isEmpty(p);
    }


    public boolean canSwitchLeft(Vehicle v) {
        return segmentLanes.canSwitchLeft(v);
    }

    public boolean canSwitchRight(Vehicle v) {
        return segmentLanes.canSwitchRight(v);
    }

    public void switchRight(Vehicle v) {
        segmentLanes.switchRight(v);
    }

    public void switchLeft(Vehicle v) {
        segmentLanes.switchLeft(v);
    }

    public Vehicle getVehicle(Point p) {
        return segmentLanes.getVehicle(p);
    }


    /**
     * Returns the direction of a segment
     *
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns segment location
     *
     * @return
     */
    public Point getSegmentLocation() {
        return location;
    }

    /**
     * Adds intersections of segment
     *
     * @param p
     */
    private void addIntersections(Point p) {
        intersections.add(p.x);
        intersections.add(p.y);
    }


    /**
     * Returns true if segment object is oneway
     *
     * @param m the Map that contains the segment
     * @return
     */
    public boolean oneWay(Map m) {
        Point toFind = new Point(location.y, location.x);

        for (int i = 0; i < m.getMap().get(location.y).size(); i++) {
            if (m.getMap().get(location.y).get(i).getSegmentLocation().equals(toFind)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Returns an arraylist of intersections connected to segment object
     *
     * @return
     */
    public ArrayList<Integer> getIntersections() {
        return intersections;
    }


    /**
     * returns a list of vehicles at the end of a segment
     *
     * @return
     */
    public ArrayList<Vehicle> getVehiclesAtEnd() {
        ArrayList<Vehicle> temp = new ArrayList<>();

        for (int i = 0; i < onSegment.size(); i++) {
            if (atEnd(onSegment.get(i))) {
                temp.add(onSegment.get(i));
            }
        }

        return temp;
    }


    /**
     * Inner class that handles lane funtionality
     */
    private class Lane implements Serializable {
        private Vehicle[][] lanes;
        private int laneCount;
        private int segmentLength;


        Lane(int count, int segLength) {
            this.laneCount = count;
            this.segmentLength = segLength;

            lanes = new Vehicle[segmentLength][count];
            for (int k = 0; k < segmentLength; k++) {
                for (int l = 0; l < count; l++) {
                    lanes[k][l] = null;
                }
            }
        }


        private boolean canInsert(Vehicle v) {


            if (v instanceof Car) {
                int check = 0;

                for (int i = 0; i < laneCount; i++) {
                    if (lanes[0][i] != null) {
                        check += 1;
                    }
                }

                if (check != laneCount) {
                    return true;
                }

            } else if (v instanceof Bus) {
                int check = 0;

                for (int i = 0; i < laneCount; i++) {
                    if (lanes[1][i] != null && lanes[0][i] != null) {
                        check += 1;
                    }
                }

                if (check != laneCount) {
                    return true;
                }

            } else if (v instanceof Truck) {
                int check = 0;

                for (int i = 0; i < laneCount; i++) {
                    if (lanes[2][i] != null && lanes[1][i] != null && lanes[0][i] != null) {
                        check += 1;
                    }
                }

                if (check != laneCount) {
                    return true;
                }

            }

            return false;

        }


        private ArrayList<Integer> alternateInserts(Vehicle v) {
            ArrayList<Integer> alternates = new ArrayList<>();

            if (canInsert(v)) {
                if (v instanceof Car) {
                    for (int i = 0; i < laneCount; i++) {
                        if (lanes[0][i] == null) {
                            alternates.add(i);
                        }
                    }

                } else if (v instanceof Bus) {
                    for (int i = 0; i < laneCount; i++) {
                        if (lanes[0][i] == null && lanes[1][i] == null) {
                            alternates.add(i);
                        }
                    }

                } else if (v instanceof Truck) {
                    for (int i = 0; i < laneCount; i++) {
                        if (lanes[2][i] == null && lanes[1][i] == null && lanes[0][i] == null) {
                            alternates.add(i);
                        }
                    }
                }


            }

            return alternates;
        }


        /**
         * Adds a vehicle to a segment but adding it to a lane. Note that vehicles are only added at the beginning of segments
         *
         * @param v    the vehicle we want to add
         * @param lane
         */
        private void addVehicle(Map m, Vehicle v, int lane) {

            if (v instanceof Car) {
                if (canInsert(v)) {
                    v.setVehicleLocation(new Point(0, lane));
                    onSegment.add(v);
                    lanes[v.getVehicleLocation().x][v.getVehicleLocation().y] = v;


                } else {
                    System.out.println("Lane is occupied");
                }


            } else if (v instanceof Bus) {
                if (canInsert(v)) {
                    v.setVehicleLocation(new Point(1, lane));
                    onSegment.add(v);

                    for (int i = v.getLength() - 1; i >= 0; i--) {
                        lanes[i][v.getVehicleLocation().y] = v;
                    }

                } else {
                    System.out.println("Lane is occupied");
                }


            } else if (v instanceof Truck) {
                if (canInsert(v)) {
                    v.setVehicleLocation(new Point(2, lane));
                    onSegment.add(v);

                    for (int i = v.getLength() - 1; i >= 0; i--) {
                        lanes[i][v.getVehicleLocation().y] = v;
                    }

                } else {
                    System.out.println("Lane is occupied");
                }

            }
        }


        /**
         * Removes vehicle on segment
         *
         * @param v the vehicle to be removed
         */
        private void removeVehicle(Vehicle v) {
            Point location = v.getVehicleLocation();
            v.setVehicleLocation(null);
            onSegment.remove(v);
            v.removeSegment();


            for (int i = location.x; i >= (location.x - (v.getLength() - 1)); i--) {
                lanes[i][location.y] = null;
            }


        }


        /**
         * Moves the vehicle one index up
         *
         * @param p the segment position of the vehicle
         */
        private void moveVehicle(Point p) {
            Point location = p;
            Vehicle toMove = lanes[location.x][location.y];


            if (!atEnd(toMove)) {
                toMove.setVehicleLocation(new Point(location.x + 1, location.y));
                lanes[toMove.getVehicleLocation().x][toMove.vehicleLocation.y] = toMove;
                lanes[toMove.getVehicleLocation().x - (toMove.getLength())][toMove.vehicleLocation.y] = null;




                if (!atEnd(toMove)) {
                    System.out.println(toMove + " is " + (segmentLength - toMove.getVehicleLocation().x - 1) + " miles away from intersection " + toMove.getSegment().getSegmentLocation().y + ".");
                } else {
                    System.out.println(toMove + " has arrived at intersection " + toMove.getSegment().getSegmentLocation().y);
                }
            }


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

                System.out.println(v + " is not on segment!");

            }
            return null;

        }


        /**
         * Checks if a vehicle is at the end of a segment
         *
         * @param v Vehicle to be checked
         * @return
         */
        private boolean atEnd(Vehicle v) {

                if (v.getVehicleLocation().x == segmentLength-1) {
                    return true;
                }

            return false;
        }


        /**
         * Checks if an index is empty
         *
         * @param p the point to be checked
         * @return
         */
        private boolean isEmpty(Point p) {

            if (lanes[p.x][p.y] == null) {
                return true;
            }

            return false;
        }


        private boolean canAdd(Vehicle v, int lane) {

            if (v instanceof Car) {

                    if (lanes[0][lane] == null) {
                        return true;
                    }

            } else if (v instanceof Bus) {

                    if (lanes[1][lane] == null && lanes[0][lane] == null) {
                      return true;
                    }

            } else if (v instanceof Truck) {

                    if (lanes[2][lane] == null && lanes[1][lane] == null && lanes[0][lane] == null) {
                       return true;
                    }

            }

            return false;
        }


        /**
         * Returns the indexes of vehicle v
         *
         * @param v the vehicle with an unknown index
         * @return
         */
        public int laneLocation(Vehicle v) {

            for (int i = 0; i < segmentLength; i++) {
                for (int j = 0; j < lanes[0].length; j++) {
                    if (lanes[i][j] == v) {
                        return j;
                    }
                }
            }

            return -1;
        }


        /**
         * Checks if left lane is available
         *
         * @param v Vehicle that want's to change lane
         * @return
         */
        private boolean canSwitchLeft(Vehicle v) {
            Point location = v.getVehicleLocation();

            if (v.getLength() == 1 && location.x + 1 <= segmentLength - 1) {
                if (location.y > 0) {
                    if (lanes[location.x + 1][location.y - 1] == null && location.x + 1 <= segmentLength - 1) {
                        return true;
                    }
                }
            } else if (v.getLength() == 2 && location.x + 1 <= segmentLength - 1) {
                if (location.y > 0) {
                    if (lanes[location.x + 1][location.y - 1] == null && lanes[location.x][location.y - 1] == null && location.x + 1 <= segmentLength - 1) {
                        return true;
                    }
                }
            } else if (v.getLength() == 3 && location.x + 1 <= segmentLength - 1) {
                if (location.y > 0) {
                    if (lanes[location.x + 1][location.y - 1] == null && lanes[location.x][location.y - 1] == null && lanes[location.x - 1][location.y - 1] == null && location.x + 1 <= segmentLength - 1) {
                        return true;
                    }
                }
            }

            return false;
        }


        /**
         * Checks if right lane is available
         *
         * @param v Vehicle that want's to change lane
         * @return
         */
        private boolean canSwitchRight(Vehicle v) {
            Point location = v.getVehicleLocation();


                if (v.getLength() == 1 && location.x + 1 <= segmentLength - 1) {
                    if (location.y < laneCount - 1) {
                        if (lanes[location.x + 1][location.y + 1] == null) {
                            return true;
                        }

                    }
                } else if (v.getLength() == 2 && location.x + 1 <= segmentLength - 1) {
                    if (location.y < laneCount - 1) {
                        if (lanes[location.x + 1][location.y + 1] == null && lanes[location.x][location.y + 1] == null && location.x + 1 <= segmentLength - 1) {
                            return true;
                        }

                    }
                } else if (v.getLength() == 3 && location.x + 1 <= segmentLength - 1) {
                    if (location.y < laneCount - 1) {
                        if (lanes[location.x + 1][location.y + 1] == null && lanes[location.x][location.y + 1] == null && lanes[location.x - 1][location.y + 1] == null && location.x + 1 <= segmentLength - 1) {
                            return true;
                        }

                    }
                }


            return false;
        }


        /**
         * Switch to lane on the left
         *
         * @param v
         */
        private void switchLeft(Vehicle v) {
            Point location = v.getVehicleLocation();

            if (canSwitchLeft(v)) {
                Point target = new Point(location.x + 1, location.y - 1); 
                v.setVehicleLocation(target);

                for (int i = location.x; i >= (location.x - (v.getLength() - 1)); i--) {
                    lanes[i][location.y] = null;
                    if (!v.getDamageStatus().isDestroyed()) {
                        lanes[i+1][location.y-1] = v;
                    }
                }

             


                if (laneCount == 2) {
                    if (laneLocation(v) == 0) {
                        System.out.println(v + " moved from the right lane to the left lane.");
                    }
                } else if (laneCount == 3) {
                    if (laneLocation(v) == 1) {
                        System.out.println(v + " Vehicle moved from the right lane to the middle lane.");
                    } else {
                        System.out.println(v + " Vehicle moved from the middle lane to the left lane.");
                    }
                }


            } else {
                if (location.x + 1 <= segmentLength - 1) {
                    Vehicle hit = lanes[location.x][location.y - 1];
                    v.getDamageStatus().calculatedSuffered(v, hit);
                    v.getDamageStatus().calculateGenerated(v, hit);
                    hit.getDamageStatus().calculatedSuffered(hit, v);
                    hit.getDamageStatus().calculateGenerated(hit, v);

                    System.out.println("Collision occurred when switching lanes! Vehicle was returned to original lane.");


                }

            }

        }


        /**
         * Switch to lane on the right
         *
         * @param v
         */
        private void switchRight(Vehicle v) {
            Point location = v.getVehicleLocation();

            if (canSwitchRight(v)) {
                Point target = new Point(location.x + 1, location.y + 1);
                v.setVehicleLocation(target);

                for (int i = location.x; i >= (location.x - (v.getLength() - 1)); i--) {
                    lanes[i][location.y] = null;
                    if (!v.getDamageStatus().isDestroyed()) {
                        lanes[i+1][location.y+1] = v;
                    }
                }



                if (laneCount == 2) {
                    if (laneLocation(v) == 1) {
                        System.out.println(v + " Vehicle moved from the left lane to the right lane.");
                    }
                } else if (laneCount == 3) {
                    if (laneLocation(v) == 1) {
                        System.out.println(v + " Vehicle moved from the left lane to the middle lane.");
                    } else {
                        System.out.println(v + " Vehicle moved from the middle lane to the right lane.");
                    }

                }


            } else {
                if (location.x + 1 <= segmentLength - 1) {
                    Vehicle hit = lanes[location.x][location.y - 1];
                    v.getDamageStatus().calculatedSuffered(v, hit);
                    v.getDamageStatus().calculateGenerated(v, hit);
                    hit.getDamageStatus().calculatedSuffered(hit, v);
                    hit.getDamageStatus().calculateGenerated(hit, v);

                    System.out.println("Collision occurred when switching lanes! Vehicle was returned to original lane.");

                }

            }
        }


        /**
         * Checks if Lane has the same or more amount of 'lanes'
         *
         * @param l lane count
         */
        private boolean compatible(int l) {

            if (l >= laneCount) {
                return true;
            } else {
                return false;
            }

        }



        private Vehicle getVehicle(Point p) {
            return lanes[p.x][p.y];
        }



        private ArrayList<Vehicle> getVictims (Vehicle atFault, int lane){
            ArrayList<Vehicle> victims = new ArrayList<>();


                for (int i = 0; i < atFault.getLength(); i++) {
                    if(lanes[i][lane] !=null){
                        victims.add(getVehicle(new Point(i,lane)));
                    }
                }

                return victims;

        }





        private int getLaneCount() {
            return laneCount;
        }

        private int getSegmentLength() {
            return segmentLength;
        }


    }


    //TODO Change sout to only happen when driveable is true


}