
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Segment {

    final Direction direction;
    final Point location;  // this road segment is between intersection x and intersection y
    private ArrayList<Integer> intersections = new ArrayList<>(); //intersections connected by the segment
    private ArrayList<Vehicle> onSegment = new ArrayList<>(); //vehicles on the segment
    private Lane segmentLanes;
    private Random num = new Random();


    Segment(Point intersections, Direction d) {
        this.direction = d;
        this.location = intersections;
        addLane();
        addIntersections(intersections);
    }

    public int laneCount() {
        return segmentLanes.getLaneCount();
    }


    public int segmentLength() {
        return segmentLanes.getSegmentLength();
    }


    public void addVehicle(Map m, Vehicle v, int lane) {
        segmentLanes.addVehicle(m, v, lane);


    }


    public void moveVehicle(Point p) {
        segmentLanes.moveVehicle(p);
    }


    public void removeVehicle(Vehicle v) {
        segmentLanes.removeVehicle(v);
    }


    /**
     * Returns true when vehicle v is at the end of a segment
     *
     * @param v
     * @return
     */
    public boolean atEnd(Vehicle v) {
        if (segmentLanes.atEnd(v)) {
            return true;
        } else {
            return false;
        }

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


    public Lane getLane() {
        return segmentLanes;
    }


    /**
     * Inner class that handles lane funtionality
     */
    public class Lane {
        private Vehicle[][] lanes;
        private int laneCount;
        private int segmentLength = 12;

        Lane(int i) {
            this.laneCount = i;
            lanes = new Vehicle[segmentLength][i];
            for (int k = 0; k < segmentLength; k++) {
                for (int l = 0; l < i; l++) {
                    lanes[k][l] = null;
                }
            }
        }

        /**
         * Adds a vehicle to a segment but adding it to a lane. Note that vehicles are only added at the beginning of segments
         *
         * @param v    the vehicle we want to add
         * @param lane
         */
        public void addVehicle(Map m, Vehicle v, int lane) {

            if (v instanceof Car) {
                if (isEmpty(new Point(0, lane))) {
                    v.setVehicleLocation(new Point(0, lane));
                    onSegment.add(v);
                    lanes[v.getVehicleLocation().x][v.getVehicleLocation().y] = v;


                } else {
                    System.out.println("Lane is occupied");
                }


            } else if (v instanceof Bus) {
                if (isEmpty(new Point(0, lane)) && isEmpty(new Point(1, lane))) {
                    v.setVehicleLocation(new Point(1, lane));
                    onSegment.add(v);

                    for (int i = v.getLength() - 1; i >= 0; i--) {
                        lanes[i][v.getVehicleLocation().y] = v;
                    }

                } else {
                    System.out.println("Lane is occupied");
                }


            } else if (v instanceof Truck) {
                if (isEmpty(new Point(0, lane)) && isEmpty(new Point(1, lane)) && isEmpty(new Point(2, lane))) {
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
         * @param v the vehicle to be removed
         */
        public void removeVehicle(Vehicle v) {
            Point location = v.getVehicleLocation();
            v.setVehicleLocation(null);
            onSegment.remove(v);
            v.removeSegment();


            for (int i = location.x; i >= (location.x - (v.getLength() - 1)); i--) {
                lanes[location.x][location.y] = null;
            }


        }


        /**
         * Moves the vehicle one index up
         * @param p the segment position of the vehicle
         */
        public void moveVehicle(Point p) {
            Vehicle toMove = lanes[p.x][p.y];
            toMove.setVehicleLocation(new Point(toMove.getVehicleLocation().x + 1, toMove.getVehicleLocation().y));

            lanes[toMove.getVehicleLocation().x][toMove.vehicleLocation.y] = toMove;
            lanes[toMove.getVehicleLocation().x - (toMove.getLength())][toMove.vehicleLocation.y] = null;

            System.out.println( toMove + "moved 1mile forward");

        }


//        /**
//         * gets the location of a vehicle in the lanes array
//         */
//        public Point getIndex(Vehicle v) {
//
//            if (onSegment.contains(v)) {
//                for (int i = 0; i < segmentLength; i++) {
//                    for (int j = 0; j < laneCount; j++) {
//                        if (lanes[i][j] == v) {
//                            return new Point(i, j);
//
//                        }
//                    }
//
//                }
//            } else {
//
//                System.out.println("Vehicle is not on segment!");
//
//            }
//            return null;
//
//        }


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


        /**
         * Checks if an index is empty
         * @param p the point to be checked
         * @return
         */
        public boolean isEmpty(Point p) {

            if (lanes[p.x][p.y] == null) {
                return true;
            }

            return false;
        }


        /**
         * Returns the indexes of vehicle v
         * @param v the vehicle with an unknown index
         * @return
         */
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
         *
         * @param v Vehicle that want's to change lane
         * @return
         */
        private boolean canSwitchLeft(Vehicle v) {
            Point location = v.getVehicleLocation();

            if (v instanceof Car) {
                if (location.y > 0) {
                    if (lanes[location.x+1][location.y - 1] == null && location.x+1 <= segmentLength-1 ) {
                        return true;
                    }
                }
            } else if (v instanceof Bus) {
                if (location.y > 0) {
                    if (lanes[location.x+1][location.y - 1] == null && lanes[location.x][location.y - 1] == null && location.x+1 <= segmentLength-1) {
                        return true;
                    }
                }
            } else if (v instanceof Truck) {
                if (location.y > 0) {
                    if (lanes[location.x + 1][location.y - 1] == null && lanes[location.x ][location.y - 1] == null && lanes[location.x - 1][location.y - 1] == null && location.x+1 <= segmentLength-1) {
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

            if (v instanceof Car && location.x+1 <= segmentLength-1) {
                if (location.y < laneCount - 1) {
                    if (lanes[location.x+1][location.y + 1] == null) {
                        return true;
                    }

                }
            } else if (v instanceof Bus) {
                if (location.y < laneCount - 1) {
                    if (lanes[location.x+1][location.y + 1] == null && lanes[location.x][location.y + 1] == null && location.x+1 <= segmentLength-1) {
                        return true;
                    }

                }
            } else if (v instanceof Truck) {
                if (location.y < laneCount - 1) {
                    if (lanes[location.x+1][location.y + 1] == null && lanes[location.x ][location.y + 1] == null && lanes[location.x - 1][location.y + 1] == null && location.x+1 <= segmentLength-1) {
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
        public void switchLeft(Vehicle v) {
            Point location = v.getVehicleLocation();

            if (canSwitchLeft(v)) {
                v.setVehicleLocation(new Point(location.x + 1, location.y - 1));


                for (int i = location.x; i >= (location.x - (v.getLength() - 1)); i--) {
                    lanes[i][location.y] = null;
                    if (!v.getDamageStatus().isDestroyed()) {
                        lanes[i][location.y - 1] = v;
                    }
                }

                moveVehicle(v.getVehicleLocation());


            } else {
                if (location.x + 1 <= segmentLength - 1){
                    Vehicle hit = lanes[location.x][location.y - 1];
                v.getDamageStatus().calculatedSuffered(v, hit);
                v.getDamageStatus().calculateGenerated(v, hit);
                hit.getDamageStatus().calculatedSuffered(hit, v);
                hit.getDamageStatus().calculateGenerated(hit, v);




            }

            }

        }


        /**
         * Switch to lane on the right
         *
         * @param v
         */
        public void switchRight(Vehicle v) {
            Point location = v.getVehicleLocation();

            if (canSwitchRight(v)) {
                v.setVehicleLocation(new Point(location.x + 1, location.y + 1));


                for (int i = location.x; i >= (location.x - (v.getLength() - 1)); i--) {
                    lanes[i][location.y] = null;
                    if (!v.getDamageStatus().isDestroyed()) {
                        lanes[i][location.y + 1] = v;
                    }
                }

                moveVehicle(v.getVehicleLocation());


            } else {
                if(location.x+1 <= segmentLength-1) {
                    Vehicle hit = lanes[location.x][location.y - 1];
                    v.getDamageStatus().calculatedSuffered(v, hit);
                    v.getDamageStatus().calculateGenerated(v, hit);
                    hit.getDamageStatus().calculatedSuffered(hit, v);
                    hit.getDamageStatus().calculateGenerated(hit, v);

                }

            }
        }


        /**
         * Checks if Lane has the same or more amount of 'lanes'
         *
         * @param l
         */
        public boolean compatible(int l) {

            if (l >= laneCount) {
                return true;
            } else {
                return false;
            }

        }


        public int getLaneCount() {
            return laneCount;
        }

        public int getSegmentLength() {
            return segmentLength;
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
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns segment location
     * @return
     */
    public Point getSegmentLocation() {
        return location;
    }

    /**
     * Adds intersections of segment
     * @param p
     */
    private void addIntersections(Point p) {
        intersections.add(p.x);
        intersections.add(p.y);
    }


    /**
     * Returns true if segment object is oneway
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
     * @return
     */
    public ArrayList<Integer> getIntersections() {
        return intersections;
    }


}
