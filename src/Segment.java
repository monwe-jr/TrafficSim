import java.awt.*;
import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;


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

    public boolean inFrontOfPlayer(Vehicle v) {
        return segmentLanes.inFrontOfPlayer(v);
    }

    public boolean vehicleAhead(Vehicle v){
        return segmentLanes.vehicleAhead(v);
    }

    public Vehicle getVehicleAhead(Vehicle v){
        return segmentLanes.getVehicleAhead(v);
    }

    public void callListener(Map m, Vehicle v) {
        segmentLanes.getListener(m, v);
    }


    public void insertVehicle(Vehicle v) {
        segmentLanes.insertVehicle(v);
    }

    public boolean canInsertOnSegment(Vehicle v) {
        return segmentLanes.canInsertOnSegment(v);
    }

    public boolean canAdd(Vehicle v, int lane) {
        return segmentLanes.canAdd(v, lane);
    }


    public void addVehicle(Map m, Vehicle v, int lane) {
        segmentLanes.addVehicle(m, v, lane);


    }


    public Vehicle getFrontVictims(Vehicle atFault, int lane, boolean atIntersection) {
        return segmentLanes.getFrontOccupant(atFault, lane, atIntersection);
    }


    public void removeVehicle(Vehicle v) {
        segmentLanes.removeVehicle(v);
    }

    public boolean canMove(Vehicle v) {
        return segmentLanes.canMove(v);
    }

    public void moveVehicle(Vehicle v) {
        segmentLanes.moveVehicle(v);
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


        private void getListener(Map m, Vehicle v) {
            Point location = v.getVehicleLocation();
            Segment leftTurn = null;
            Segment rightTurn = null;
            Segment straight = null;
            boolean canSwitchLeft = canSwitchLeft(v);
            boolean canSwitchRight = canSwitchRight(v);
            boolean canMove = v.getSegment().canMove(v);
            boolean canTurnLeft = false;
            boolean canTurnRight = false;
            boolean isDeadEnd = false;
            boolean canGoStraight = false;

            if (Turn.canTurn(m, Segment.this)) {
                ArrayList<Segment> turns = Turn.getTurns(m, Segment.this);

                if (turns.size() > 1) {
                    for (int i = 0; i < turns.size(); i++) {
                        if (turns.get(i).getDirection() == Direction.leftDirection(Segment.this.getDirection())) {
                            canTurnLeft = true;
                            leftTurn = turns.get(i);
                        } else {
                            canTurnRight = true;
                            rightTurn = turns.get(i);

                        }

                    }

                } else {

                    if (turns.get(0).getDirection() == Direction.leftDirection(Segment.this.getDirection())) {
                        canTurnLeft = true;
                        leftTurn = turns.get(0);
                    } else {
                        canTurnRight = true;
                        rightTurn = turns.get(0);

                    }

                }


            } else {
                if (Turn.getStraight(m, Segment.this) == null && Intersection.isDeadEnd(m, Segment.this.getSegmentLocation().y)) {
                    isDeadEnd = true;
                }

            }


            if (Turn.getStraight(m, Segment.this) != null) {
                canGoStraight = true;
                straight = Turn.getStraight(m, Segment.this);
            }


            System.out.println();
            System.out.println("Listener Feedback:");
            System.out.println("Segment: currently on the segment that connects intersect " + Segment.this.getSegmentLocation().x + " to intersection " + Segment.this.getSegmentLocation().y + ". This segment has " + laneCount + " lanes and is " + segmentLength + " miles long.");

            if (laneCount == 1) {
                System.out.println("Lane Location: You are on the only lane on this segment.");
            } else if (laneCount == 2) {
                if (location.y == 0) {
                    System.out.println("Lane Location: left most lane");
                } else {
                    System.out.println("Lane Location: right most lane");
                }

            } else {
                if (location.y == 0) {
                    System.out.println("Lane Location: left most lane");
                } else if (location.y == 1) {
                    System.out.println("Lane Location: middle lane");
                } else {
                    System.out.println("Lane Location: right most lane");
                }

            }


            if (!atEnd(v)) {
                if(!vehicleAhead(v)){
                    System.out.println("You are " + (segmentLength - location.x - 1) + " miles away from intersection " + Segment.this.getSegmentLocation().y + ". There is no vehicle ahead of you.");
                }else{
                    if(canMove) {
                        if (vehicleAhead(v)) {
                            Vehicle ahead = getVehicleAhead(v);
                            if (ahead instanceof Car) {
                                System.out.println("You are " + (segmentLength - location.x - 1) + " miles away from intersection " + Segment.this.getSegmentLocation().y + ". There is a car " + (ahead.getVehicleLocation().x - (location.x + 1)) + " miles ahead of you!");
                            } else if (ahead instanceof Bus) {
                                System.out.println("You are " + (segmentLength - location.x - 1) + " miles away from intersection " + Segment.this.getSegmentLocation().y + ". There is a bus " + (ahead.getVehicleLocation().x - (location.x + 1)) + " miles ahead of you!");
                            } else {
                                System.out.println("You are " + (segmentLength - location.x - 1) + " miles away from intersection " + Segment.this.getSegmentLocation().y + ". There is a truck " + (ahead.getVehicleLocation().x - (location.x + 1)) + " miles ahead of you!");
                            }
                        }
                    }
                }
            } else {
                System.out.println("You have arrived at intersection " + Segment.this.getSegmentLocation().y);
            }



                if (canMove) {
                    System.out.println("There is currently no vehicle in front of you.");
                } else {

                    if(!atEnd(v)) {
                        Vehicle o = getVehicle(new Point(location.x + 1, location.y));

                        if (o instanceof Car) {
                            System.out.println("There is a car in front of you.");
                        } else if (o instanceof Bus) {
                            System.out.println("There is a bus in front of you.");
                        } else {
                            System.out.println("There is a truck in front of you.");
                        }
                    }
                }

                if (canSwitchLeft) {

                    if (laneCount == 2) {
                        System.out.println("You can switch to the left most lane.");
                    } else {
                        if (location.y == laneCount - 1) {
                            System.out.println("You can switch to the middle lane on your left");
                        }
                    }


                } else {
                    ArrayList<Vehicle> occupants = getSideOccupants(v, true, false);

                    ///////////////////////////////////////////////////////////////////////////////
                    if (!occupants.isEmpty()) {
                        boolean car = false;
                        boolean bus = false;
                        boolean truck = false;


                        for (int i = 0; i < occupants.size(); i++) {
                            if (occupants.get(i) instanceof Car) {
                                car = true;
                            } else if (occupants.get(i) instanceof Bus) {
                                bus = true;
                            } else {
                                truck = true;
                            }

                        }


                        if (occupants.size() == 1) {
                            if (car) {
                                System.out.println("Your left lane is occupied by a  car.");
                            } else if (bus) {
                                System.out.println("Your left lane is occupied by a bus.");
                            } else {
                                System.out.println("Your left lane is occupied by a truck.");
                            }
                        } else if (occupants.size() == 2) {
                            if (car && !bus && !truck) {
                                System.out.println("Your left lane is occupied by 2 cars.");
                            } else if (!car && bus && !truck) {
                                System.out.println("Your left lane is occupied by 2 buses.");
                            } else if (!car && !bus && truck) {
                                System.out.println("Your left lane is occupied by 2 trucks.");
                            } else if (car && bus && !truck) {
                                System.out.println("Your left lane is occupied by a car and a bus.");
                            } else if (!car && bus && truck) {
                                System.out.println("Your left lane is occupied by a bus and a truck.");
                            } else {
                                System.out.println("Your left lane is occupied by a car and a truck.");
                            }

                        } else {
                            System.out.println("Your left lane is occupied by 3 cars.");

                        }


                    }

                    ///////////////////////////////////////////////////////////////////////////////

                }


                if (canSwitchRight) {


                    if (laneCount == 2) {
                        System.out.println("You can switch to the right most lane");
                    } else {
                        if (location.y == 0)
                            System.out.println("You can switch to the middle lane on your right");
                    }


                } else {
                    ArrayList<Vehicle> occupants = getSideOccupants(v, false, true);


                    ///////////////////////////////////////////////////////////////////////////////
                    if (!occupants.isEmpty()) {
                        boolean car = false;
                        boolean bus = false;
                        boolean truck = false;


                        for (int i = 0; i < occupants.size(); i++) {
                            if (occupants.get(i) instanceof Car) {
                                car = true;
                            } else if (occupants.get(i) instanceof Bus) {
                                bus = true;
                            } else {
                                truck = true;
                            }

                        }


                        if (occupants.size() == 1) {
                            if (car) {
                                System.out.println("Your right lane is occupied by a car.");
                            } else if (bus) {
                                System.out.println("Your right lane is occupied by a bus.");
                            } else {
                                System.out.println("Your right lane is occupied by a truck.");
                            }
                        } else if (occupants.size() == 2) {
                            if (car && !bus && !truck) {
                                System.out.println("Your right lane is occupied by 2 cars.");
                            } else if (!car && bus && !truck) {
                                System.out.println("Your right lane is occupied by 2 buses.");
                            } else if (!car && !bus && truck) {
                                System.out.println("Your right lane is occupied by 2 trucks.");
                            } else if (car && bus && !truck) {
                                System.out.println("Your right lane is occupied by a car and a bus.");
                            } else if (!car && bus && truck) {
                                System.out.println("Your right lane is occupied by a bus and a truck.");
                            } else {
                                System.out.println("Your right lane is occupied by a car and a truck.");
                            }

                        } else {
                            System.out.println("Your left lane is occupied by 3 cars.");

                        }
                    }

                    ///////////////////////////////////////////////////////////////////////////////
                }

                if (laneCount == 1) {
                    System.out.println("This segment only has 1 lane. You cannot switch lanes in either direction.");
                }


                if (canTurnLeft) {
                    if (canGoStraight || canTurnRight) {
                        System.out.println("You can turn left onto the segment that connects intersection " + leftTurn.getSegmentLocation().x + " to intersection " + leftTurn.getSegmentLocation().y + " when you arive at intersection " + location.y + ".");
                    } else {
                        System.out.println("You can only turn left onto the segment that connects intersection " + leftTurn.getSegmentLocation().x + " to intersection " + leftTurn.getSegmentLocation().y + " when you arive at intersection " + location.y + ".");
                    }


                    if (leftTurn.laneCount() > 1) {
                        if (compatible(leftTurn.laneCount())) {
                            if (leftTurn.canAdd(v, location.y)) {
                                System.out.println("You can turn left without expereincing any collisons.");
                            } else {
                                System.out.println("The lane you want to turn onto is not clear!");
                            }
                        } else {
                            if (laneCount == 3 && leftTurn.laneCount() == 2) {
                                if (location.y == 1 || location.y == 2) {
                                    if (leftTurn.canAdd(v, 1)) {
                                        System.out.println("You can turn left without experiencing any collisons.");
                                    } else {
                                        System.out.println("The lane you want to turn onto is not clear!");
                                    }

                                } else {
                                    if (leftTurn.canAdd(v, 0)) {
                                        System.out.println("You can turn left without experiencing any collisons.");
                                    } else {
                                        System.out.println("The lane you want to turn onto is not clear!");
                                    }

                                }
                            }
                        }
                    } else {
                        if (leftTurn.canAdd(v, 0)) {
                            System.out.println("You can turn left without experiencing any collisons.");
                        } else {
                            System.out.println("The lane you want to turn onto is not clear!");
                        }

                    }


                }

                if (canTurnRight) {
                    if (canGoStraight || canTurnLeft) {
                        System.out.println("You can turn right onto the segment that connects intersection " + rightTurn.getSegmentLocation().x + " to intersection " + rightTurn.getSegmentLocation().y + " when you arive at intersection " + location.y + ".");
                    } else {
                        System.out.println("You can only turn right onto the segment that connects intersection " + rightTurn.getSegmentLocation().x + " to intersection " + rightTurn.getSegmentLocation().y + " when you arive at intersection " + location.y + ".");

                    }


                    if (rightTurn.laneCount() > 1) {
                        if (compatible(rightTurn.laneCount())) {
                            if (rightTurn.canAdd(v, location.y)) {
                                System.out.println("You can turn right without experiencing any collisons.");
                            } else {
                                System.out.println("The lane you want to turn onto is not clear!");
                            }
                        } else {
                            if (laneCount == 3 && rightTurn.laneCount() == 2) {
                                if (location.y == 0 || location.y == 1) {
                                    if (rightTurn.canAdd(v, 0)) {
                                        System.out.println("You can turn right without experiencing any collisons.");
                                    } else {
                                        System.out.println("The lane you want to turn onto is not clear!");
                                    }

                                } else {
                                    if (rightTurn.canAdd(v, rightTurn.laneCount() - 1)) {
                                        System.out.println("You can turn rigth without experiencing any collisons.");
                                    } else {
                                        System.out.println("The lane you want to turn onto is not clear!");
                                    }

                                }
                            }
                        }
                    } else {
                        if (rightTurn.canAdd(v, 0)) {
                            System.out.println("You can turn rigth without experiencing any collisons.");
                        } else {
                            System.out.println("The lane you want to turn onto is not clear!");
                        }

                    }


                }

                if (canGoStraight) {
                    if (canTurnLeft || canTurnRight) {
                        System.out.println("You can go straight onto the segment that connects intersection " + straight.getSegmentLocation().x + " to intersection " + straight.getSegmentLocation().y + " when you arive at intersection " + location.y + ".");
                    } else {
                        System.out.println("You can only go straight onto the segment that connects intersection " + straight.getSegmentLocation().x + " to intersection " + straight.getSegmentLocation().y + " when you arive at intersection " + location.y + ".");

                    }


                    if (straight.laneCount() > 1) {
                        if (compatible(straight.laneCount())) {
                            if (straight.canAdd(v, location.y)) {
                                System.out.println("You can go straigth without experiencing any collisons.");
                            } else {
                                System.out.println("The lane you want to turn onto is not clear!");
                            }
                        } else {
                            if (laneCount == 3 && straight.laneCount() == 2) {
                                if (location.y == 1 || location.y == 2) {
                                    if (straight.canAdd(v, 1)) {
                                        System.out.println("You can go straigth without experiencing any collisons.");
                                    } else {
                                        System.out.println("The lane you want to turn onto is not clear!");
                                    }

                                } else {
                                    if (straight.canAdd(v, 0)) {
                                        System.out.println("You can go straigth without experiencing any collisons.");
                                    } else {
                                        System.out.println("The lane you want to turn onto is not clear!");
                                    }

                                }
                            }
                        }
                    } else {
                        if (straight.canAdd(v, 0)) {
                            System.out.println("You can go straigth without experiencing any collisons.");
                        } else {
                            System.out.println("The lane you want to turn onto is not clear!");
                        }

                    }


                }

                if (isDeadEnd) {
                    System.out.println("Intersection " + Segment.this.getSegmentLocation().y + " is a deadEnd. You can only UTurn!");
                }

        }

        private boolean inFrontOfPlayer(Vehicle v) {
            if((v.getVehicleLocation().x - 1) >= 0) {
                Point location = new Point(v.getVehicleLocation().x - 1, v.getVehicleLocation().y);

                if (lanes[location.x][location.y] != null) {
                    if (getVehicle(location).isDrivable()) {
                        return true;
                    }
                }
            }

            return false;
        }


        private boolean vehicleAhead(Vehicle v) {
            Point location = new Point(v.getVehicleLocation().x+1,v.getVehicleLocation().y);

            for (int i = location.x; i < segmentLength; i++) {
                if (lanes[i][location.y] != null) {
                    return true;
                }
            }

            return false;
        }

        private Vehicle getVehicleAhead(Vehicle v) {
            Point location = new Point(v.getVehicleLocation().x +1,v.getVehicleLocation().y);

                for (int i = location.x; i < segmentLength; i++) {
                    if (lanes[i][location.y] !=null){
                        return lanes[i][location.y];

                    }
                }

            return null;
        }


        private Point getPoint(Vehicle v) {
            ArrayList<Point> candidates = new ArrayList<>();

            for (int i = 0; i < lanes.length; i++) {
                for (int j = 0; j < lanes[i].length; j++) {
                    if (canInsertAtPoint(v, new Point(i, j))) {
                        candidates.add(new Point(i, j));
                    }

                }
            }

            Collections.shuffle(candidates);
            return candidates.get((int) (Math.random() * candidates.size()));

        }


        private void insertVehicle(Vehicle v) {
            Point p = getPoint(v);

            if (v instanceof Car) {
                v.setVehicleLocation(p);
                onSegment.add(v);
                v.setSegment(Segment.this);
                lanes[p.x][p.y] = v;

                if (v.isDrivable()) {
                    if (!atEnd(v)) {
                        System.out.println("Your car has been added to the segment connecting intersection " + location.x + " to intersection " + location.y + ". You are " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + location.y + ".");
                    } else {
                        System.out.println("Your car has been added to the segment connecting intersection " + location.x + " to intersection " + location.y + ". You are at the end of the segment and have arrived at intersection " + location.y + ".");
                    }
                }

                System.out.println(v + " Has been inserted into segment " + location + ". ");

            } else if (v instanceof Bus) {
                v.setVehicleLocation(p);
                onSegment.add(v);
                v.setSegment(Segment.this);

                for (int i = p.x; i > p.x - v.getSize(); i--) {
                    lanes[i][p.y] = v;
                }

                if (v.isDrivable()) {
                    if (!atEnd(v)) {
                        System.out.println("Your bus has been added to the segment connecting intersection " + location.x + " to intersection " + location.y + ". You are " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + location.y + ".");
                    } else {
                        System.out.println("Your bus has been added to the segment connecting intersection " + location.x + " to intersection " + location.y + ". You are at the end of the segment and have arrived at intersection " + location.y + ".");
                    }
                }

                System.out.println(v + " Has been inserted into segment " + location + ". ");

            } else if (v instanceof Truck) {
                v.setVehicleLocation(p);
                onSegment.add(v);
                v.setSegment(Segment.this);

                for (int i = p.x; i > p.x - v.getSize(); i--) {
                    lanes[i][p.y] = v;
                }


                if (v.isDrivable()) {
                    if (!atEnd(v)) {
                        System.out.println("Your truck has been added to the segment connecting intersection " + location.x + " to intersection " + location.y + ". You are " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + location.y + ".");
                    } else {
                        System.out.println("Your truck has been added to the segment connecting intersection " + location.x + " to intersection " + location.y + ". You are at the end of the segment and have arrived at intersection " + location.y + ".");
                    }
                }

                System.out.println(v + " Has been inserted into segment " + location + ". ");

            }


        }


        private boolean canInsertOnSegment(Vehicle v) {
            for (int i = 0; i < lanes.length; i++) {
                for (int j = 0; j < lanes[i].length; j++) {
                    if (canInsertAtPoint(v, new Point(i, j))) {
                        return true;
                    }

                }
            }


            return false;
        }


        private boolean canInsertAtPoint(Vehicle v, Point p) {
            if (v instanceof Car) {
                if (lanes[p.x][p.y] == null) {
                    return true;
                }

            } else if (v instanceof Bus) {
                if (p.x - 1 >= 0) {
                    if (lanes[p.x][p.y] == null && lanes[p.x - 1][p.y] == null) {
                        return true;
                    }
                }

            } else if (v instanceof Truck) {
                if (p.x - 2 >= 0) {
                    if (lanes[p.x][p.y] == null && lanes[p.x - 1][p.y] == null && lanes[p.x - 2][p.y] == null) {
                        return true;
                    }
                }
            }

            return false;

        }


        /**
         * Adds a vehicle to a segment but adding it to a lane. Note that vehicles are only added at the beginning of segments
         *
         * @param v    the vehicle we want to add
         * @param lane
         */
        private void addVehicle(Map m, Vehicle v, int lane) {

            if (v instanceof Car) {
                if (canAdd(v, lane)) {
                    v.setVehicleLocation(new Point(0, lane));
                    onSegment.add(v);
                    v.setSegment(Segment.this);
                    lanes[v.getVehicleLocation().x][v.getVehicleLocation().y] = v;


                }


            } else if (v instanceof Bus) {
                if (canAdd(v, lane)) {
                    v.setVehicleLocation(new Point(1, lane));
                    onSegment.add(v);
                    v.setSegment(Segment.this);

                    for (int i = v.getSize() - 1; i >= 0; i--) {
                        lanes[i][v.getVehicleLocation().y] = v;
                    }

                }

            } else if (v instanceof Truck) {
                if (canAdd(v, lane)) {
                    v.setVehicleLocation(new Point(2, lane));
                    onSegment.add(v);
                    v.setSegment(Segment.this);

                    for (int i = v.getSize() - 1; i >= 0; i--) {
                        lanes[i][v.getVehicleLocation().y] = v;
                    }
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
            onSegment.remove(v);
            v.removeSegment();
            v.setVehicleLocation(null);


            for (int i = location.x; i >= (location.x - (v.getSize() - 1)); i--) {
                lanes[i][location.y] = null;
            }


        }


        private boolean canMove(Vehicle v) {
            if (!atEnd(v)) {
                if (lanes[v.getVehicleLocation().x + 1][v.getVehicleLocation().y] == null) {
                    return true;
                }
            }

            return false;
        }


        /**
         * Moves the vehicle one index up
         *
         * @param v the vehicle to be moved
         */
        private void moveVehicle(Vehicle v) {
            Point location = v.getVehicleLocation();

            if (canMove(v)) {
                if (!atEnd(v)) {
                    Point target = new Point(location.x + 1, location.y);
                    v.setVehicleLocation(target);
                    lanes[v.getVehicleLocation().x][v.vehicleLocation.y] = v;
                    lanes[v.getVehicleLocation().x - (v.getSize())][v.vehicleLocation.y] = null;

                    if (v.isDrivable()) {
                        if (!atEnd(v)) {
                            System.out.println("Your vehicle moved one mile. You are now " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + Segment.this.location.y + ".");
                        } else {
                            System.out.println("Your vehicle has arrived at intersection " + Segment.this.location.y + ".");
                        }
                    }

                }
            } else {
                v.getDamageStatus().frontCollision(getFrontVictims(v, location.y, false));

            }

        }


//        /**
//         * gets the location of a vehicle in the lanes array
//         */
//        public Point getIndex(Vehicle v) {
//
//            if (onSegment.contains(v)) {
//                for (int i = segmentLength-1; i >=0 ; i--) {
//                    for (int j = 0; j < laneCount; j++) {
//                        if (lanes[i][j] == v) {
//                            return new Point(i, j);
//
//                        }
//                        break;
//                    }
//
//                }
//            } else {
//
//                System.out.println(v + " is not on segment!");
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
        private boolean atEnd(Vehicle v) {

            if (v.getVehicleLocation().x == segmentLength - 1) {
                return true;
            }

            return false;
        }


//        /**
//         * Checks if an index is empty
//         *
//         * @param p the point to be checked
//         * @return
//         */
//        private boolean isEmpty(Point p) {
//
//            if (lanes[p.x][p.y] == null) {
//                return true;
//            }
//
//            return false;
//        }


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

            if (v.getSize() == 1 && location.x + 1 <= segmentLength - 1) {
                if (location.y > 0) {
                    if (lanes[location.x + 1][location.y - 1] == null && location.x + 1 <= segmentLength - 1) {
                        return true;
                    }
                }
            } else if (v.getSize() == 2 && location.x + 1 <= segmentLength - 1) {
                if (location.y > 0) {
                    if (lanes[location.x + 1][location.y - 1] == null && lanes[location.x][location.y - 1] == null && location.x + 1 <= segmentLength - 1) {
                        return true;
                    }
                }
            } else if (v.getSize() == 3 && location.x + 1 <= segmentLength - 1) {
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


            if (v.getSize() == 1 && location.x + 1 <= segmentLength - 1) {
                if (location.y < laneCount - 1) {
                    if (lanes[location.x + 1][location.y + 1] == null) {
                        return true;
                    }

                }
            } else if (v.getSize() == 2 && location.x + 1 <= segmentLength - 1) {
                if (location.y < laneCount - 1) {
                    if (lanes[location.x + 1][location.y + 1] == null && lanes[location.x][location.y + 1] == null && location.x + 1 <= segmentLength - 1) {
                        return true;
                    }

                }
            } else if (v.getSize() == 3 && location.x + 1 <= segmentLength - 1) {
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
            boolean switchedLeft = false;
            Point location = v.getVehicleLocation();

            if (canSwitchLeft(v)) {
                Point target = new Point(location.x + 1, location.y - 1);
                v.setVehicleLocation(target);

                for (int i = location.x; i >= (location.x - (v.getSize() - 1)); i--) {
                    lanes[i][location.y] = null;
                    lanes[i + 1][location.y - 1] = v;

                }


                switchedLeft = true;

            } else {


                if (location.x + 1 <= segmentLength - 1 && location.y - 1 >= 0) {
                    v.getDamageStatus().sideCollision(getSideOccupants(v, true, false));
                }


            }


            if (switchedLeft) {
                if (v.isDrivable()) {
                    if (laneCount == 2) {
                        if (laneLocation(v) == 0) {
                            if ((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                                System.out.println("You moved from the right lane to the left lane. You are " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + Segment.this.location.y + ".");
                            } else {
                                System.out.println("You moved from the right lane to the left lane. You have arrived at intersection " + Segment.this.location.y + ".");
                            }
                        }
                    } else if (laneCount == 3) {
                        if (laneLocation(v) == 1) {
                            if ((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                                System.out.println("You moved from the right lane to the middle lane. You are " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + Segment.this.location.y + ".");
                            } else {
                                System.out.println("You moved from the right lane to the middle lane. You have arrived at intersection " + Segment.this.location.y + ".");
                            }
                        } else {
                            if ((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                                System.out.println("You moved from the middle lane to the left lane. You are" + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + Segment.this.location.y + ".");
                            } else {
                                System.out.println("You moved from the middle lane to the left lane. You have arrived at intersection " + Segment.this.location.y + ".");
                            }
                        }
                    }
                }

            }


            if (inFrontOfPlayer(v)) {
                if (v instanceof Car) {
                    System.out.println("There is a car in front of you.");

                } else if (v instanceof Bus) {
                    System.out.println("There is bus in front of you.");

                } else {
                    System.out.println("There is a truck in front of you.");

                }
            }


        }


        /**
         * Switch to lane on the right
         *
         * @param v
         */
        private void switchRight(Vehicle v) {
            boolean switchedRight = false;
            Point location = v.getVehicleLocation();

            if (canSwitchRight(v)) {
                Point target = new Point(location.x + 1, location.y + 1);
                v.setVehicleLocation(target);

                for (int i = location.x; i >= (location.x - (v.getSize() - 1)); i--) {
                    lanes[i][location.y] = null;
                    lanes[i + 1][location.y + 1] = v;

                }


                switchedRight = true;


            } else {


                if (location.x + 1 <= segmentLength - 1 && location.y + 1 <= laneCount) {
                    v.getDamageStatus().sideCollision(getSideOccupants(v, false, true));
                }


            }

            if (switchedRight) {
                if (v.isDrivable()) {
                    if (laneCount == 2) {
                        if (laneLocation(v) == 1) {
                            if ((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                                System.out.println("You moved from the left lane to the right lane. You are " + (segmentLength - v.getVehicleLocation().x - 1) + " away from intersection " + Segment.this.location.y + ".");
                            } else {
                                System.out.println("You moved from the left lane to the right lane. You have arrived at intersection " + Segment.this.location.y + ".");
                            }
                        }
                    } else if (laneCount == 3) {
                        if (laneLocation(v) == 1) {
                            if ((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                                System.out.println("You moved from the left lane to the middle lane. You are " + (segmentLength - v.getVehicleLocation().x - 1) + " away from intersection " + Segment.this.location.y + ".");
                            } else {
                                System.out.println("You moved from the left lane to the middle lane. You have arrived at intersection " + Segment.this.location.y + ".");
                            }
                        } else {
                            if ((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                                System.out.println("You moved from the middle lane to the right lane. You is " + (segmentLength - v.getVehicleLocation().x - 1) + " away from intersection " + Segment.this.location.y + ".");
                            } else {
                                System.out.println("You moved from the middle lane to the right lane. You have arrived at intersection " + Segment.this.location.y + ".");
                            }
                        }

                    }

                }
            }


            if (inFrontOfPlayer(v)) {
                if (v instanceof Car) {
                    System.out.println("There is a car in front of you.");

                } else if (v instanceof Bus) {
                    System.out.println("There is bus in front of you.");

                } else {
                    System.out.println("There is a truck in front of you.");

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


        private Vehicle getFrontOccupant(Vehicle atFault, int lane, boolean atIntersect) {
            if (atFault instanceof Truck) {
                if (atIntersect) {
                    if (lanes[0][lane] != null) {
                        return lanes[0][lane];
                    } else if (lanes[1][lane] != null) {
                        return lanes[1][lane];
                    } else if (lanes[2][lane] != null) {
                        return lanes[2][lane];
                    }
                } else {
                    return lanes[atFault.getVehicleLocation().x + 1][atFault.getVehicleLocation().y];
                }
            } else if (atFault instanceof Bus) {
                if (atIntersect) {
                    if (lanes[0][lane] != null) {
                        return lanes[0][lane];
                    } else if (lanes[1][lane] != null) {
                        return lanes[1][lane];
                    }
                } else {
                    return lanes[atFault.getVehicleLocation().x + 1][atFault.getVehicleLocation().y];
                }
            } else if (atFault instanceof Car) {
                if (atIntersect) {
                    return lanes[0][lane];
                } else {
                    return lanes[atFault.getVehicleLocation().x + 1][atFault.getVehicleLocation().y];
                }
            }

            return null;
        }

        private ArrayList<Vehicle> getSideOccupants(Vehicle v, boolean left, boolean right) {
            ArrayList<Vehicle> occupants = new ArrayList<>();
            Point location = v.getVehicleLocation();


            if (left) {
                if (location.x + 1 <= segmentLength - 1 && location.y > 0) {

                    Point leftV = new Point(location.x + 1, location.y - 1);

                    for (int i = leftV.x; i > leftV.x - v.getSize(); i--) {
                        if (lanes[i][leftV.y] != null && !occupants.contains(lanes[i][leftV.y])) {
                            occupants.add(getVehicle(new Point(i, leftV.y)));
                        }
                    }


                }
            } else {
                if (location.x + 1 <= segmentLength - 1 && location.y < laneCount - 1) {

                    Point rightV = new Point(location.x + 1, location.y + 1);

                    for (int i = rightV.x; i > rightV.x - v.getSize(); i--) {
                        if (lanes[i][rightV.y] != null && !occupants.contains(lanes[i][rightV.y])) {
                            occupants.add(getVehicle(new Point(i, rightV.y)));
                        }
                    }


                }

            }

            return occupants;

        }


        private int getLaneCount() {
            return laneCount;
        }

//
//        private int getSegmentLength() {
//            return segmentLength;
//        }


    }


}