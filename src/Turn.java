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
     * @return segments turns can be performed onto
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
            for (int j = 0; j < m.getMap().get(s.getSegmentLocation().y).size(); j++) {
                if (m.getMap().get(s.getSegmentLocation().y).get(j).getSegmentLocation().x == s.getSegmentLocation().y && !m.getMap().get(s.getSegmentLocation().y).get(j).getSegmentLocation().equals(new Point(s.getSegmentLocation().y, s.getSegmentLocation().x)) && !m.getMap().get(s.getSegmentLocation().y).get(j).getDirection().equals(Direction.straightDirection(s.getDirection()))) {
                    availableTurns.add(m.getMap().get(s.getSegmentLocation().y).get(j));

                }
            }
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
    static protected Segment getStraight(Map m, Segment s) {
        ArrayList<Segment> options = m.getMap().get(s.getSegmentLocation().y);

        /*List<Segment> seg = options.stream()
                .filter(r -> r.getDirection() == Direction.straightDirection(r.getDirection()))
                .collect(Collectors.toList());
        if (seg.isEmpty()) return null;
        return seg.get(0);*/

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getDirection().equals(Direction.straightDirection(s.getDirection())) && !options.get(i).getSegmentLocation().equals(new Point(s.getSegmentLocation().y, s.getSegmentLocation().x))) {
                return options.get(i);
            }
        }

        return null;

    }


    static public boolean canGoStraight(Map m, Segment s) {
        if (getStraight(m, s) != null) {
            return true;
        }
        return false;
    }


    static public boolean canLeftTurn(Map m, Segment s) {
        ArrayList<Segment> turns = getTurns(m, s);

        if (turns != null) {
            for (int i = 0; i < turns.size(); i++) {
                if (turns.get(i).getDirection() == Direction.leftDirection(s.getDirection())) {
                    return true;
                }
            }
        }

        return false;
    }


    static public boolean canRightTurn(Map m, Segment s) {
        ArrayList<Segment> turns = getTurns(m, s);

        if (turns != null) {
            for (int i = 0; i < turns.size(); i++) {
                if (turns.get(i).getDirection() == Direction.rightDirection(s.getDirection())) {
                    return true;
                }
            }
        }

        return false;

    }


    /**
     * Returns left turn segment adjacent to segment s
     *
     * @param m Map of segments
     * @param s Segment to turn left at
     * @return
     */
    static private Segment getLeftTurn(Map m, Segment s) {
        ArrayList<Segment> options = getTurns(m, s);

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getDirection() == Direction.leftDirection(s.getDirection())) {
                return options.get(i);
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
    static private Segment getRightTurn(Map m, Segment s) {
        ArrayList<Segment> options = getTurns(m, s);

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getDirection() == Direction.rightDirection(s.getDirection())) {
                return options.get(i);
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
    static public void goStraight(Map m, Segment s, Vehicle v, boolean firstMove) {
        boolean wentStraight = false;
        if (getStraight(m, s) != null) {

            Segment toGoStraightOn = getStraight(m, s);
            int oldLaneLocation = v.getVehicleLocation().y;
            ;


            if (toGoStraightOn.laneCount() > 1) {

                if (s.compatible(toGoStraightOn.laneCount())) {
                    if (toGoStraightOn.canAdd(v, oldLaneLocation)) {
                        s.removeVehicle(v);
                        v.removeSegment();
                        toGoStraightOn.addVehicle(m, v, oldLaneLocation);
                        wentStraight = true;

                        v.getReputation().successfulGamble();

                    } else {

                        v.getDamageStatus().frontCollision(toGoStraightOn.getFrontVictims(v, oldLaneLocation, true));


                    }


                } else {


                    if (s.laneCount() == 3 && toGoStraightOn.laneCount() == 2) {
                        if (oldLaneLocation == 1 || oldLaneLocation == 2) {
                            if (toGoStraightOn.canAdd(v, 1)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toGoStraightOn.addVehicle(m, v, 1);
                                wentStraight = true;

                                v.getReputation().successfulGamble();

                            } else {

                                v.getDamageStatus().frontCollision(toGoStraightOn.getFrontVictims(v, 1, true));


                            }

                            if (oldLaneLocation == 2) {
                                if (!firstMove) {
                                    v.getReputation().correspondingLaneViolation();
                                }
                            }

                        } else {
                            if (toGoStraightOn.canAdd(v, 0)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toGoStraightOn.addVehicle(m, v, 0);
                                wentStraight = true;

                                v.getReputation().successfulGamble();

                            } else {

                                v.getDamageStatus().frontCollision(toGoStraightOn.getFrontVictims(v, 0, true));


                            }
                        }

                    }

                }


            } else {
                if (toGoStraightOn.canAdd(v, 0)) {
                    s.removeVehicle(v);
                    v.removeSegment();
                    toGoStraightOn.addVehicle(m, v, 0);
                    wentStraight = true;

                    v.getReputation().successfulGamble();

                } else {

                    v.getDamageStatus().frontCollision(toGoStraightOn.getFrontVictims(v, 0, true));


                }

                if (oldLaneLocation != 0) {
                    if (!firstMove) {
                        v.getReputation().correspondingLaneViolation();
                    }
                }
            }

            if (wentStraight) {
                if(v.isDrivable()) {
                    if (v.getSegment().laneCount() == 1) {
                        System.out.println("You went straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". Your vehicle is now on the segment that connects intersection " + toGoStraightOn.getSegmentLocation().x + " to intersection " + toGoStraightOn.getSegmentLocation().y + ". This segment has only 1 lane.");
                    } else if (v.getSegment().laneCount() == 2) {
                        if (v.getSegment().laneLocation(v) == 0) {
                            System.out.println("You went straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". You are now on the segment that connects intersection " + toGoStraightOn.getSegmentLocation().x + " to intersection " + toGoStraightOn.getSegmentLocation().y + ". Your vehicle is on the left lane on segment.");
                        } else {
                            System.out.println("You went straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". You are now on the segment that connects intersection " + toGoStraightOn.getSegmentLocation().x + " to intersection " + toGoStraightOn.getSegmentLocation().y + ". Your vehicle is on the right lane on segment.");
                        }

                    } else if (v.getSegment().laneCount() == 3) {
                        if (v.getSegment().laneLocation(v) == 0) {
                            System.out.println("You went straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". You are now on the segment that connects intersection " + toGoStraightOn.getSegmentLocation().x + " to intersection " + toGoStraightOn.getSegmentLocation().y + ". Your vehicle is on the left lane on segment " + toGoStraightOn.getSegmentLocation());
                        } else if (v.getSegment().laneLocation(v) == 1) {
                            System.out.println("You went straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". You are now on the segment that connects intersection " + toGoStraightOn.getSegmentLocation().x + " to intersection " + toGoStraightOn.getSegmentLocation().y + ". Your vehicle is on the middle lane on segment " + toGoStraightOn.getSegmentLocation());
                        } else if (v.getSegment().laneLocation(v) == 2) {
                            System.out.println("You went straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". You are now on the segment that connects intersection " + toGoStraightOn.getSegmentLocation().x + " to intersection " + toGoStraightOn.getSegmentLocation().y + ". Your vehicle is on the right lane on segment " + toGoStraightOn.getSegmentLocation());
                        }

                    }
                }
            }


        } else {
            if (v.isDrivable()) {
                System.out.println("You cannot go straight at upcoming intersection!");
            }
        }

    }


    /**
     * Vehicles turn left when t his method is call. Traffic violation is applied if player is not on the appropriate lane
     *
     * @param m game map
     * @param s the segment player is turning from
     * @param v vehicle that is turning
     */
    static public void leftTurn(Map m, Segment s, Vehicle v, boolean firstMove) {
        boolean turned = false;
        if (getLeftTurn(m, s) != null) {
            Segment toTurnLeftOnto = getLeftTurn(m, s);
            int oldLaneLocation = v.getVehicleLocation().y;


            if (toTurnLeftOnto.laneCount() > 1) {
                if (s.compatible(toTurnLeftOnto.laneCount())) {

                    if (toTurnLeftOnto.canAdd(v, oldLaneLocation)) {
                        s.removeVehicle(v);
                        v.removeSegment();
                        toTurnLeftOnto.addVehicle(m, v, oldLaneLocation);
                        turned = true;

                        v.getReputation().successfulGamble();

                    } else {

                        v.getDamageStatus().frontCollision(toTurnLeftOnto.getFrontVictims(v, oldLaneLocation, true));


                    }

                    if (oldLaneLocation != 0) {
                        if (!firstMove) {
                            v.getReputation().correspondingLaneViolation();
                        }
                    }

                } else {

                    if (s.laneCount() == 3 && toTurnLeftOnto.laneCount() == 2) {
                        if (oldLaneLocation == 1 || oldLaneLocation == 2) {
                            if (toTurnLeftOnto.canAdd(v, 1)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toTurnLeftOnto.addVehicle(m, v, 1);
                                turned = true;

                                v.getReputation().successfulGamble();

                            } else {

                                v.getDamageStatus().frontCollision(toTurnLeftOnto.getFrontVictims(v, 1, true));


                            }

                            if (!firstMove) {
                                v.getReputation().correspondingLaneViolation();
                            }

                        } else {
                            if (toTurnLeftOnto.canAdd(v, 0)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toTurnLeftOnto.addVehicle(m, v, 0);
                                turned = true;

                                v.getReputation().successfulGamble();

                            } else {

                                v.getDamageStatus().frontCollision(toTurnLeftOnto.getFrontVictims(v, 0, true));


                            }
                        }
                    }


                }

            } else {
                if (toTurnLeftOnto.canAdd(v, 0)) {
                    s.removeVehicle(v);
                    v.removeSegment();
                    toTurnLeftOnto.addVehicle(m, v, 0);
                    turned = true;

                    v.getReputation().successfulGamble();

                } else {

                    v.getDamageStatus().frontCollision(toTurnLeftOnto.getFrontVictims(v, 0, true));


                }

                if (oldLaneLocation != 0) {
                    if (!firstMove) {
                        v.getReputation().correspondingLaneViolation();
                    }

                }

            }

            if (turned) {
                if (v.isDrivable()) {
                    if (v.getSegment().laneCount() == 1) {
                        System.out.println("You performed a left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x + ". Your vehicle is now on the segment that connects intersection " + toTurnLeftOnto.getSegmentLocation().x + " to intersection " + toTurnLeftOnto.getSegmentLocation().y + " This segment has only 1 lane.");
                    } else if (v.getSegment().laneCount() == 2) {
                        if (v.getSegment().laneLocation(v) == 0) {
                            System.out.println("You performed a left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x + ". Your vehicle is now on the left lane of the segment that connects intersection " + toTurnLeftOnto.getSegmentLocation().x + " to intersection " + toTurnLeftOnto.getSegmentLocation().y + ".");
                        } else {
                            System.out.println("You performed a left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x + ". Your vehicle is now on the right lane of the segment that connects intersection " + toTurnLeftOnto.getSegmentLocation().x + " to intersection " + toTurnLeftOnto.getSegmentLocation().y + ".");
                        }
                    } else if (v.getSegment().laneCount() == 3) {
                        if (v.getSegment().laneLocation(v) == 0) {
                            System.out.println("You performed a left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x + ". Your vehicle is now on the left lane of the segment that connects intersection " + toTurnLeftOnto.getSegmentLocation().x + " to intersection " + toTurnLeftOnto.getSegmentLocation().y + ".");
                        } else if (v.getSegment().laneLocation(v) == 1) {
                            System.out.println("You performed a left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x + ". Your vehicle is now on the middle lane of the segment that connects intersection " + toTurnLeftOnto.getSegmentLocation().x + " to intersection " + toTurnLeftOnto.getSegmentLocation().y + ".");
                        } else if (v.getSegment().laneLocation(v) == 2) {
                            System.out.println("You performed a left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x + ". Your vehicle is now on the right lane of the segment that connects intersection " + toTurnLeftOnto.getSegmentLocation().x + " to intersection " + toTurnLeftOnto.getSegmentLocation().y + ".");

                        }

                    }
                }
            }


        } else {
            if (v.isDrivable()) {
                System.out.println("You cannot turn Left at this intersection!");
            }
        }
    }


    /**
     * Vehicles turn right when t his method is call. Traffic violation is applied if player is not on the appropriate lane
     *
     * @param m game map
     * @param s the segment player is turning from
     * @param v vehicle that is turning
     */
    static public void rightTurn(Map m, Segment s, Vehicle v, boolean firstMove) {
        boolean turned = false;
        if (getRightTurn(m, s) != null) {
            Segment toTurnRightOnto = getRightTurn(m, s);
            int oldLaneLocation = v.getVehicleLocation().y;
            ;


            if (toTurnRightOnto.laneCount() > 1) {
                if (s.compatible(toTurnRightOnto.laneCount())) {

                    if (toTurnRightOnto.canAdd(v, oldLaneLocation)) {
                        s.removeVehicle(v);
                        v.removeSegment();
                        toTurnRightOnto.addVehicle(m, v, oldLaneLocation);
                        turned = true;

                        v.getReputation().successfulGamble();

                    } else {

                        v.getDamageStatus().frontCollision(toTurnRightOnto.getFrontVictims(v, oldLaneLocation, true));


                    }


                    if (oldLaneLocation != s.laneCount() - 1) {
                        if (!firstMove) {
                            v.getReputation().correspondingLaneViolation();
                        }
                    }


                } else {

                    if (s.laneCount() == 3 && toTurnRightOnto.laneCount() == 2) {
                        if (oldLaneLocation == 0 || oldLaneLocation == 1) {
                            if (toTurnRightOnto.canAdd(v, 0)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toTurnRightOnto.addVehicle(m, v, 0);
                                turned = true;

                                v.getReputation().successfulGamble();

                            } else {

                                v.getDamageStatus().frontCollision(toTurnRightOnto.getFrontVictims(v, 0, true));


                            }

                            if (!firstMove) {
                                v.getReputation().correspondingLaneViolation();
                            }

                        } else {
                            if (toTurnRightOnto.canAdd(v, toTurnRightOnto.laneCount() - 1)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toTurnRightOnto.addVehicle(m, v, toTurnRightOnto.laneCount() - 1);
                                turned = true;

                                v.getReputation().successfulGamble();

                            } else {

                                v.getDamageStatus().frontCollision(toTurnRightOnto.getFrontVictims(v, toTurnRightOnto.laneCount() - 1, true));


                            }
                        }
                    }


                }

            } else {
                if (toTurnRightOnto.canAdd(v, 0)) {
                    s.removeVehicle(v);
                    v.removeSegment();
                    toTurnRightOnto.addVehicle(m, v, 0);
                    turned = true;

                    v.getReputation().successfulGamble();

                } else {

                    v.getDamageStatus().frontCollision(toTurnRightOnto.getFrontVictims(v, 0, true));


                }

                if (oldLaneLocation != s.laneCount() - 1) {
                    if (!firstMove) {
                        v.getReputation().correspondingLaneViolation();
                    }
                }

            }


            if (turned) {
                if (v.isDrivable()) {
                    if (v.getSegment().laneCount() == 1) {
                        System.out.println("You performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x + ". Your vehicle is now on the segment that connects intersection " + toTurnRightOnto.getSegmentLocation().x + " to intersection " + toTurnRightOnto.getSegmentLocation().y + ". This segment has only 1 lane.");
                    } else if (v.getSegment().laneCount() == 2) {
                        if (v.getSegment().laneLocation(v) == 0) {
                            System.out.println("You performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x + ". Your vehicle is now on the left lane of the segment that connects intersection " + toTurnRightOnto.getSegmentLocation().x + " to intersection " + toTurnRightOnto.getSegmentLocation().y + ".");
                        } else {
                            System.out.println("You performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x + ". Your vehicle is now on the right lane of the segment that connects intersection " + toTurnRightOnto.getSegmentLocation().x + " to intersection " + toTurnRightOnto.getSegmentLocation().y + ".");
                        }
                    } else if (v.getSegment().laneCount() == 3) {
                        if (v.getSegment().laneLocation(v) == 0) {
                            System.out.println("You performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x + ". Your vehicle is now on the left lane of the segment that connects intersection " + toTurnRightOnto.getSegmentLocation().x + " to intersection " + toTurnRightOnto.getSegmentLocation().y + ".");
                        } else if (v.getSegment().laneLocation(v) == 1) {
                            System.out.println("You performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x + ". Your vehicle is now on the middle lane of the segment that connects intersection " + toTurnRightOnto.getSegmentLocation().x + " to intersection " + toTurnRightOnto.getSegmentLocation().y + ".");
                        } else if (v.getSegment().laneLocation(v) == 2) {
                            System.out.println("You performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x + ". Your vehicle is now on the right lane of the segment that connects intersection " + toTurnRightOnto.getSegmentLocation().x + " to intersection " + toTurnRightOnto.getSegmentLocation().y + ".");

                        }

                    }
                }

            }
        } else {
            if (v.isDrivable()) {
                System.out.println("You cannot turn right at this intersection!");
            }
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
        boolean turned = false;
        if (Intersection.isDeadEnd(m, s.getSegmentLocation().y)) {
            Segment toUTurnOnto = getSegment(m, new Point(s.getSegmentLocation().y, s.getSegmentLocation().x));
            int oldLaneLocation = v.getVehicleLocation().y;
            ;


            if (toUTurnOnto.laneCount() > 1) {

                if (s.compatible(toUTurnOnto.laneCount())) {
                    if (toUTurnOnto.canAdd(v, oldLaneLocation)) {
                        s.removeVehicle(v);
                        v.removeSegment();
                        toUTurnOnto.addVehicle(m, v, oldLaneLocation);
                        turned = true;

                        v.getReputation().successfulGamble();

                    } else {

                        v.getDamageStatus().frontCollision(toUTurnOnto.getFrontVictims(v, oldLaneLocation, true));

                    }

                } else {

                    if (s.laneCount() == 3 && toUTurnOnto.laneCount() == 2) {
                        if (s.laneLocation(v) == 1 || s.laneLocation(v) == 2) {
                            if (toUTurnOnto.canAdd(v, 1)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toUTurnOnto.addVehicle(m, v, 1);
                                turned = true;

                                v.getReputation().successfulGamble();

                            } else {

                                v.getDamageStatus().frontCollision(toUTurnOnto.getFrontVictims(v, 1, true));

                            }

                        } else {
                            if (toUTurnOnto.canAdd(v, 0)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toUTurnOnto.addVehicle(m, v, 0);
                                turned = true;

                                v.getReputation().successfulGamble();

                            } else {

                                v.getDamageStatus().frontCollision(toUTurnOnto.getFrontVictims(v, 0, true));

                            }
                        }

                    }

                }

            } else {

                if (toUTurnOnto.canAdd(v, 0)) {
                    s.removeVehicle(v);
                    v.removeSegment();
                    toUTurnOnto.addVehicle(m, v, 0);
                    turned = true;

                    v.getReputation().successfulGamble();

                } else {
                    v.getDamageStatus().frontCollision(toUTurnOnto.getFrontVictims(v, 0, true));
                }

            }


            if (turned) {
                if (v.isDrivable()) {
                    if (v.getSegment().laneCount() == 1) {
                        System.out.println("You Performed a U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x + "Your vehicle is now on the segment that connects intersection " + toUTurnOnto.getSegmentLocation().x + " to intersection " + toUTurnOnto.getSegmentLocation().y + ". This segment has only 1 lane.");
                    } else if (v.getSegment().laneCount() == 2) {
                        if (v.getSegment().laneLocation(v) == 0) {
                            System.out.println("You Performed a U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x + ". Your vehicle is now on the left lane of the segment that connects " + toUTurnOnto.getSegmentLocation().x + " to intersection " + toUTurnOnto.getSegmentLocation().y + ".");
                        } else {
                            System.out.println("You Performed a U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x + ". Your vehicle is now on the right lane of the segment that connects " + toUTurnOnto.getSegmentLocation().x + " to intersection " + toUTurnOnto.getSegmentLocation().y + ".");
                        }
                    } else if (v.getSegment().laneCount() == 3) {
                        if (v.getSegment().laneLocation(v) == 0) {
                            System.out.println("You Performed a U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x + ". Your vehicle is now on the left lane of the segment that connects " + toUTurnOnto.getSegmentLocation().x + " to intersection " + toUTurnOnto.getSegmentLocation().y + ".");
                        } else if (v.getSegment().laneLocation(v) == 1) {
                            System.out.println("You Performed a U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x + ". Your vehicle is now on the middle lane of the segment that connects " + toUTurnOnto.getSegmentLocation().x + " to intersection " + toUTurnOnto.getSegmentLocation().y + ".");
                        } else if (v.getSegment().laneLocation(v) == 2) {
                            System.out.println("You Performed a U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x + ". Your vehicle is now on the right lane of the segment that connects " + toUTurnOnto.getSegmentLocation().x + " to intersection " + toUTurnOnto.getSegmentLocation().y + ".");

                        }

                    }
                }
            }

        }

    }

    //TODO Change sout to only happen when driveable is true


}
