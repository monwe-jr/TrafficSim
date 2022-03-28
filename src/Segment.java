import java.awt.*;
import java.io.Serializable;
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


    public int segmentLength() {
        return segmentLanes.getSegmentLength();
    }

    public void insertVehicle(Vehicle v) {
       segmentLanes.insertVehicle(v);
    }

    public boolean canInsertOnSegment(Vehicle v){
        return segmentLanes.canInsertOnSegment(v);
    }

    public boolean canAdd(Vehicle v, int lane) {
        return segmentLanes.canAdd(v, lane);
    }



    public void addVehicle(Map m, Vehicle v, int lane) {
        segmentLanes.addVehicle(m, v, lane);


    }


    public Vehicle getFrontVictims(Vehicle atFault, int lane, boolean atIntersection) {
        return segmentLanes.getFrontVictims(atFault, lane, atIntersection);
    }


    public void removeVehicle(Vehicle v) {
        segmentLanes.removeVehicle(v);
    }

    public boolean canMove(Vehicle v){
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


       private Point getPoint (Vehicle v){
            ArrayList<Point> candidates = new ArrayList<>();

           for (int i = 0; i < lanes.length; i++) {
               for (int j = 0; j < lanes[i].length; j++) {
                    if(canInsertAtPoint(v, new Point(i,j))){
                        candidates.add(new Point(i,j));
                    }

               }
           }

           Collections.shuffle(candidates);
           return candidates.get((int) (Math.random() * candidates.size()));

       }



        private void insertVehicle(Vehicle v) {
           Point p = getPoint(v);

               if(v instanceof Car) {
                   v.setVehicleLocation(p);
                   onSegment.add(v);
                   v.setSegment(Segment.this);
                   lanes[p.x][p.y] = v;

                   System.out.println(v + " Has been inserted into segment " + location + ". " );

               }
               else if (v instanceof Bus){
                   v.setVehicleLocation(p);
                   onSegment.add(v);
                   v.setSegment(Segment.this);

                   for (int i = p.x; i > p.x-v.getSize(); i--) {
                       lanes[i][p.y] = v;
                   }

                   System.out.println(v + "Has been inserted into segment " + location + ". " );

               }
               else if(v instanceof Truck){
                   v.setVehicleLocation(p);
                   onSegment.add(v);
                   v.setSegment(Segment.this);

                   for (int i = p.x; i > p.x-v.getSize(); i--) {
                       lanes[i][p.y] = v;
                   }

                   System.out.println(v + "Has been inserted into segment " + location + ". " );

           }


        }


      private boolean canInsertOnSegment(Vehicle v){
          for (int i = 0; i < lanes.length; i++) {
              for (int j = 0; j < lanes[i].length; j++) {
                  if(canInsertAtPoint(v, new Point(i,j))){
                      return true;
                  }

              }
          }


            return false;
      }


        private boolean canInsertAtPoint(Vehicle v, Point p) {
            if (v instanceof Car) {
               if(lanes[p.x][p.y] == null){
                   return true;
               }

            } else if (v instanceof Bus) {
                if(p.x-1 >= 0){
                    if(lanes[p.x][p.y] == null && lanes[p.x-1][p.y] == null){
                        return true;
                    }
                }

            } else if (v instanceof Truck) {
                if (p.x - 2 >= 0) {
                    if(lanes[p.x][p.y] == null && lanes[p.x-1][p.y] == null && lanes[p.x-2][p.y] == null){
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
                if (canAdd(v,lane)) {
                    v.setVehicleLocation(new Point(0, lane));
                    onSegment.add(v);
                    v.setSegment(Segment.this);
                    lanes[v.getVehicleLocation().x][v.getVehicleLocation().y] = v;


                } else {
                    System.out.println("Lane is occupied");
                }


            } else if (v instanceof Bus) {
                if (canAdd(v,lane)) {
                    v.setVehicleLocation(new Point(1, lane));
                    onSegment.add(v);
                    v.setSegment(Segment.this);

                    for (int i = v.getSize() - 1; i >= 0; i--) {
                        lanes[i][v.getVehicleLocation().y] = v;
                    }

                } else {
                    System.out.println("Lane is occupied");
                }


            } else if (v instanceof Truck) {
                if (canAdd(v,lane)) {
                    v.setVehicleLocation(new Point(2, lane));
                    onSegment.add(v);
                    v.setSegment(Segment.this);

                    for (int i = v.getSize() - 1; i >= 0; i--) {
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
            onSegment.remove(v);
            v.removeSegment();
            v.setVehicleLocation(null);


            for (int i = location.x; i >= (location.x - (v.getSize() - 1)); i--) {
                lanes[i][location.y] = null;
            }


        }


        private boolean canMove(Vehicle v){
            if(lanes[v.getVehicleLocation().x +1][v.getVehicleLocation().y] ==null){
                return true;
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


            if (!atEnd(v)) {
                Point target = new Point(location.x + 1, location.y);
                v.setVehicleLocation(target);
                lanes[v.getVehicleLocation().x][v.vehicleLocation.y] = v;
                lanes[v.getVehicleLocation().x - (v.getSize())][v.vehicleLocation.y] = null;


                if (!atEnd(v)) {
                    System.out.println(v + " moved on segment " + Segment.this.location + " and is "  + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + Segment.this.location.y + ".");
                } else {
                    System.out.println(v + " has arrived at intersection " + v.getSegment().getSegmentLocation().y);
                }
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
//                if (location.x + 1 <= segmentLength - 1 && location.y - 1 >= 0) {
//                    ArrayList<Vehicle> victims = getSideVictims(v, location.y - 1);
//                    v.getDamageStatus().sideCollision(victims);
//                }

            }


            if (switchedLeft) {

                if (laneCount == 2) {
                    if (laneLocation(v) == 0) {
                        if((segmentLength - v.getVehicleLocation().x - 1)>0) {
                            System.out.println(v + " moved from the right lane to the left lane on segment " + Segment.this.location + ". " + v + " is " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + Segment.this.location.y);
                        } else{
                            System.out.println(v + " moved from the right lane to the left lane on segment " + Segment.this.location + ". " + v + " has arrived at intersection " + Segment.this.location.y);
                        }
                    }
                } else if (laneCount == 3) {
                    if (laneLocation(v) == 1) {
                        if((segmentLength - v.getVehicleLocation().x - 1)>0) {
                            System.out.println(v + " moved from the right lane to the middle lane on segment " + Segment.this.location + ". " + v + " is " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + Segment.this.location.y);
                        } else{
                            System.out.println(v + " moved from the right lane to the middle lane on segment " + Segment.this.location + ". " + v + " has arrived at intersection " + Segment.this.location.y);
                        }
                    } else {
                        if((segmentLength - v.getVehicleLocation().x - 1)>0) {
                            System.out.println(v + " moved from the middle lane to the left lane on segment " + Segment.this.location + ". " + v + " is " + (segmentLength - v.getVehicleLocation().x - 1) + " miles away from intersection " + Segment.this.location.y);
                        }else{
                            System.out.println(v + " moved from the middle lane to the left lane on segment " + Segment.this.location + ". " + v + " has arrived at intersection " + Segment.this.location.y);
                        }
                    }
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
//                if (location.x + 1 <= segmentLength - 1 && location.y + 1 <= laneCount) {
//                    ArrayList<Vehicle> victims = getSideVictims(v, location.y + 1);
//                    v.getDamageStatus().sideCollision(victims);
//                }

            }

            if (switchedRight) {
                if (laneCount == 2) {
                    if (laneLocation(v) == 1) {
                        if((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                            System.out.println(v + " moved from the left lane to the right lane on segment " + Segment.this.location + ". " + v + " is " + (segmentLength - v.getVehicleLocation().x - 1) + " away from intersection " + Segment.this.location.y);
                        } else {
                            System.out.println(v + " moved from the left lane to the right lane on segment " + Segment.this.location + ". " + v + " has arrived at intersection " + Segment.this.location.y);
                        }
                    }
                } else if (laneCount == 3) {
                    if (laneLocation(v) == 1) {
                        if((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                            System.out.println(v + " moved from the left lane to the middle lane on segment " + Segment.this.location + ". " + v + " is " + (segmentLength - v.getVehicleLocation().x - 1) + " away from intersection " + Segment.this.location.y);
                        } else {
                            System.out.println(v + " moved from the left lane to the middle lane on segment " + Segment.this.location + ". " + v + " has arrived at intersection " + Segment.this.location.y);
                        }
                    } else {
                        if((segmentLength - v.getVehicleLocation().x - 1) > 0) {
                            System.out.println(v + " moved from the middle lane to the right lane on segment " + Segment.this.location + ". " + v + " is " + (segmentLength - v.getVehicleLocation().x - 1) + " away from intersection " + Segment.this.location.y);
                        } else {
                            System.out.println(v + " moved from the middle lane to the right lane on segment " + Segment.this.location + ". " + v + " has arrived at intersection " + Segment.this.location.y);
                        }
                    }

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


        private Vehicle getFrontVictims(Vehicle atFault, int lane, boolean atIntersect) {
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


        private ArrayList<Vehicle> getSideVictims(Vehicle atFault, int lane) {
            ArrayList<Vehicle> victims = new ArrayList<>();

            for (int i = atFault.getVehicleLocation().x + 1; i >= i - (atFault.getSize() - 1); i--) {
                if (lanes[i][lane] != null && !victims.contains(lanes[i][lane])) {
                    victims.add(getVehicle(new Point(i, lane)));
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


    //TODO Change sout to only happen when driveable is true for full game


}