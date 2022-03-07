import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Turn {

    /**
     * Checks segment s leads to an intersection that vehicles on can turn on
     *
     * @param m Map of segments
     * @param s Segment to be checked
     * @return
     */
    public static boolean canTurn(Map m, Segment s) {
        ArrayList<Segment> roads = m.getMap().get(s.getSegmentLocation().y);

        for (int i = 0; i < roads.size(); i++) {
            if (roads.get(i).getSegmentLocation().x == s.getSegmentLocation().y
                    && !s.getDirection().equals(s.getDirection(), roads.get(i).getDirection())
                    && !roads.get(i).getSegmentLocation().equals(new Point(s.getSegmentLocation().y, s.getSegmentLocation().x))) {
                return true;
            }
        }

        return false;
    }


    /**
     * Gets turns of the upcoming intersection of segment s
     *
     * @param m Map of segments
     * @param s segment with upcoming intersection
     * @return
     */
    static public ArrayList<Segment> getTurns(Map m, Segment s) {
        ArrayList<Segment> availableTurns = new ArrayList<>();

        if (canTurn(m, s)) {
            for (int j = 0; j < m.getMap().size(); j++) {
                if (m.getMap().get(s.getSegmentLocation().y).get(j).getSegmentLocation().x == s.getSegmentLocation().y && !m.getMap().get(s.getSegmentLocation().y).get(j).getSegmentLocation().equals(new Point(s.getSegmentLocation().y, s.getSegmentLocation().x))) {
                    availableTurns.add(m.getMap().get(s.getSegmentLocation().y).get(j));
                }
            }
        } else {
            System.out.println("Can't turn at upcoming intersection");
            return null;
        }

        return availableTurns;
    }


    /**
     * Returns the segment with location segLocation
     *
     * @param m           Map of segments
     * @param segLocation Point that represents segment location
     * @return
     */
    static public Segment getSegment(Map m, Point segLocation) {
        ArrayList<ArrayList<Segment>> segments = m.getMap();
        ArrayList<Segment> results = new ArrayList<>();
        segments.forEach(s -> results.addAll(s));
        List<Segment> seg = results.stream()
                .filter(s -> s.getSegmentLocation().equals(segLocation))
                .collect(Collectors.toList());
        if (seg.isEmpty()) return null;
        return seg.get(0);

        /*for (int i = 0; i < m.getMap().size(); i++) {
            for (int j = 0; j < m.getMap().get(i).size(); j++) {
                if(m.getMap().get(i).get(j).getSegmentLocation().equals(segment)){
                    return m.getMap().get(i).get(j);
                }
            }
        }

        return null;*/
    }


    /**
     * Returns segment ahead after upcoming intersection of segment s
     *
     * @param m Map of segments
     * @param s the segment with a straight direction
     * @return
     */
    static private Segment getStraight(Map m, Segment s) {
        ArrayList<Segment> options = getTurns(m, s);

        List<Segment> seg = options.stream()
                .filter(r -> r.getDirection() == Direction.straightDirection(r.getDirection()))
                .collect(Collectors.toList());
        if (seg.isEmpty()) return null;
        return seg.get(0);

        /*for (int i = 0; i < options.size(); i++) {
            if(options.get(i).getDirection() == Direction.straightDirection(s.getDirection())){
                    return options.get(i);
            }else{
                System.out.println("Can't go straight!");
            }
        }

            return null;*/

    }


    /**
     * Returns left turn segment adjacent to segment s
     *
     * @param m Map of segments
     * @param s Segment to turn left at
     * @return
     */
    static private Segment getLeftTurns(Map m, Segment s) {
        ArrayList<Segment> options = getTurns(m, s);

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getDirection() == Direction.leftDirection(s.getDirection())) {
                return options.get(i);
            } else {
                System.out.println("Can't go left!");
            }
        }

        return null;

    }


    /**
     * Returns right turn segment adjacent to segment s
     *
     * @param m Map of segments
     * @param s Segment to turn right at
     * @return
     */
    static private Segment getRightTurns(Map m, Segment s) {
        ArrayList<Segment> options = getTurns(m, s);

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getDirection() == Direction.rightDirection(s.getDirection())) {
                return options.get(i);
            } else {
                System.out.println("Can't go right!");
            }
        }

        return null;

    }


    /**
     * Vehicles goes straight when method is call. Traffic violation is applied if player is not on the appropriate lane
     *
     * @param m Map of segments
     * @param s The segment vehicle is going straight from
     * @param v Vehicle going straight
     */
    static public void goStraight(Map m, Segment s, Vehicle v) {

        if (getStraight(m, s) != null) {
            Segment toGoStraightOn = getStraight(m, s);
            int oldLaneLocation = s.getLane().laneLocation(v);
            s.removeVehicle(v);
            v.removeSegment();

            if (toGoStraightOn.laneCount() > 1) {
                if (s.getLane().compatible(toGoStraightOn.laneCount())) {
                    toGoStraightOn.addVehicle(m, v, oldLaneLocation);
                    v.setSegment(toGoStraightOn);
                } else {


                    if (s.laneCount() == 3 && toGoStraightOn.laneCount() == 2) {
                        if (oldLaneLocation == 2) {
                            toGoStraightOn.addVehicle(m, v, 1);
                            v.setSegment(toGoStraightOn);
                            v.getReputation().correspondingLaneViolation();
                        } else {
                            toGoStraightOn.addVehicle(m, v, oldLaneLocation);
                            v.setSegment(toGoStraightOn);
                        }
                    }


                }

            } else {
                toGoStraightOn.addVehicle(m, v, 0);
                v.setSegment(toGoStraightOn);
                if (oldLaneLocation != 0) {
                    v.getReputation().correspondingLaneViolation();
                }
            }

        } else {
            System.out.println("Cannot go straight at this intersection!");
        }

    }


    /**
     * Vehicles turn left when t his method is call. Traffic violation is applied if player is not on the appropriate lane
     *
     * @param m game map
     * @param s the segment player is turning from
     * @param v vehicle that is turning
     */
    static public void leftTurn(Map m, Segment s, Vehicle v) {
        if (getLeftTurns(m, s) != null) {
            Segment toTurnLeftOnto = getLeftTurns(m, s);
            int oldLaneLocation = s.getLane().laneLocation(v);
            s.removeVehicle(v);
            v.removeSegment();

            if (toTurnLeftOnto.laneCount() > 1) {
                if (s.getLane().compatible(toTurnLeftOnto.laneCount())) {

                    if (oldLaneLocation != 0) {
                        toTurnLeftOnto.addVehicle(m, v, oldLaneLocation);
                        v.setSegment(toTurnLeftOnto);
                        v.getReputation().correspondingLaneViolation();
                    } else {
                        toTurnLeftOnto.addVehicle(m, v, oldLaneLocation);
                        v.setSegment(toTurnLeftOnto);
                    }


                } else {

                    if (s.laneCount() == 3 && toTurnLeftOnto.laneCount() == 2) {
                        if (oldLaneLocation == 2 || oldLaneLocation == 3) {
                            toTurnLeftOnto.addVehicle(m, v, 1);
                            v.setSegment(toTurnLeftOnto);
                            v.getReputation().correspondingLaneViolation();
                        } else {
                            toTurnLeftOnto.addVehicle(m, v, oldLaneLocation);
                            v.setSegment(toTurnLeftOnto);
                        }
                    }


                }

            } else {
                toTurnLeftOnto.addVehicle(m, v, 0);
                v.setSegment(toTurnLeftOnto);
                if (oldLaneLocation != 0) {
                    v.getReputation().correspondingLaneViolation();

                }
            }


        } else {
            System.out.println("Cannot turn Left at this intersection!");
        }
    }


    /**
     * Vehicles turn right when t his method is call. Traffic violation is applied if player is not on the appropriate lane
     *
     * @param m game map
     * @param s the segment player is turning from
     * @param v vehicle that is turning
     */
    static public void rightTurn(Map m, Segment s, Vehicle v) {
        if (getRightTurns(m, s) != null) {
            Segment toTurnRightOnto = getLeftTurns(m, s);
            int oldLaneLocation = s.getLane().laneLocation(v);
            s.removeVehicle(v);
            v.removeSegment();

            if (toTurnRightOnto.laneCount() > 1) {
                if (s.getLane().compatible(toTurnRightOnto.laneCount())) {

                    if (oldLaneLocation != s.laneCount() - 1) {
                        toTurnRightOnto.addVehicle(m, v, oldLaneLocation);
                        v.setSegment(toTurnRightOnto);
                        v.getReputation().correspondingLaneViolation();
                    } else {
                        toTurnRightOnto.addVehicle(m, v, oldLaneLocation);
                        v.setSegment(toTurnRightOnto);
                    }


                } else {

                    if (s.laneCount() == 3 && toTurnRightOnto.laneCount() == 2) {
                        if (oldLaneLocation == 0 || oldLaneLocation == 1) {
                            toTurnRightOnto.addVehicle(m, v, 0);
                            v.setSegment(toTurnRightOnto);
                            v.getReputation().correspondingLaneViolation();
                        } else {
                            toTurnRightOnto.addVehicle(m, v, oldLaneLocation);
                            v.setSegment(toTurnRightOnto);
                        }
                    }


                }

            } else {
                toTurnRightOnto.addVehicle(m, v, 0);
                v.setSegment(toTurnRightOnto);
                if (oldLaneLocation != 0) {
                    v.getReputation().correspondingLaneViolation();

                }
            }


        } else {
            System.out.println("Cannot turn right at this intersection!");
        }
    }


    /**
     * performs uTurn if intersection is a dead end. If the lanes are compatible, you turn to the corresponding lane. If not, you turn to the most logical choice without receiving a violation
     *
     * @param m game map
     * @param s the segment player is turning from
     * @param v the vehicle that will be turning
     */
    static public void uTurn(Map m, Segment s, Vehicle v) {
        if (Intersection.isDeadEnd(m, s.getSegmentLocation().y)) {
            Segment toUTurnOnto = getSegment(m, new Point(s.getSegmentLocation().y, s.getSegmentLocation().x));
            int oldLaneLocation = s.getLane().laneLocation(v);
            s.removeVehicle(v);
            v.removeSegment();

            if (toUTurnOnto.laneCount() > 1) {

                if (s.getLane().compatible(toUTurnOnto.laneCount()) && toUTurnOnto.laneCount() != 3) {
                    toUTurnOnto.addVehicle(m, v, oldLaneLocation);
                } else {

                    if (s.laneCount() == 1 && toUTurnOnto.laneCount() == 3) {
                        toUTurnOnto.addVehicle(m, v, 1);
                    } else if (s.laneCount() == 3 && toUTurnOnto.laneCount() == 2) {
                        if (s.getLane().laneLocation(v) == 1 || s.getLane().laneLocation(v) == 1) {
                            toUTurnOnto.addVehicle(m, v, 1);
                        } else {
                            toUTurnOnto.addVehicle(m, v, 0);
                        }

                    }

                }

            } else {
                toUTurnOnto.addVehicle(m, v, 0);
            }

            v.setSegment(toUTurnOnto);
        }
    }


    //TODO add collision for turns 
}
