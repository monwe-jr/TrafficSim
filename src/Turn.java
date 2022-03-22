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
                    && !roads.get(i).getSegmentLocation().equals(new Point(s.getSegmentLocation().y, s.getSegmentLocation().x)) ) {
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
        } else {
            System.out.println("Can't turn at upcoming intersection!");
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
    static protected Segment getStraight(Map m, Segment s) {
        ArrayList<Segment> options = m.getMap().get(s.getSegmentLocation().y);

        /*List<Segment> seg = options.stream()
                .filter(r -> r.getDirection() == Direction.straightDirection(r.getDirection()))
                .collect(Collectors.toList());
        if (seg.isEmpty()) return null;
        return seg.get(0);*/

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getDirection().equals(Direction.straightDirection(s.getDirection())) && !options.get(i).getSegmentLocation().equals(new Point(s.getSegmentLocation().y, s.getSegmentLocation().x)) ) {
                return options.get(i);
            }
        }

        return null;

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
            int oldLaneLocation = s.laneLocation(v);


            if (toGoStraightOn.laneCount() > 1) {

                if (s.compatible(toGoStraightOn.laneCount())) {
                    if (toGoStraightOn.canAdd(v,oldLaneLocation)) {
                        s.removeVehicle(v);
                        v.removeSegment();
                        toGoStraightOn.addVehicle(m, v, oldLaneLocation);
                    } else {

                        ArrayList<Vehicle> victims = toGoStraightOn.getFrontVictims(v,oldLaneLocation);

                        for (int i = 0; i < victims.size(); i++) {
                            Vehicle victim = victims.get(i);

                            v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                            v.getDamageStatus().calculateGenerated(v, victim);
                            victim.getDamageStatus().calculatedSuffered(victim,v);
                            victim.getDamageStatus().calculatedSuffered(victim,v);
                        }

                        System.out.println("Collision occurred");

                    }


                } else {


                    if (s.laneCount() == 3 && toGoStraightOn.laneCount() == 2) {
                        if (oldLaneLocation == 2) {
                            if (toGoStraightOn.canAdd(v,1)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toGoStraightOn.addVehicle(m, v, 1);
                                v.getReputation().correspondingLaneViolation();
                            } else {

                                ArrayList<Vehicle> victims = toGoStraightOn.getFrontVictims(v,1);

                                for (int i = 0; i < victims.size(); i++) {
                                    Vehicle victim = victims.get(i);

                                    v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                    v.getDamageStatus().calculateGenerated(v, victim);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                }

                                System.out.println("Collision occurred");
                            }

                        } else {
                            if (toGoStraightOn.canAdd(v, oldLaneLocation)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toGoStraightOn.addVehicle(m, v, oldLaneLocation);
                            } else {

                                ArrayList<Vehicle> victims = toGoStraightOn.getFrontVictims(v,oldLaneLocation);

                                for (int i = 0; i < victims.size(); i++) {
                                    Vehicle victim = victims.get(i);

                                    v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                    v.getDamageStatus().calculateGenerated(v, victim);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                }

                                System.out.println("Collision occurred");

                            }
                        }

                    }


                }


            } else {
                if (toGoStraightOn.canAdd(v,0)) {
                    s.removeVehicle(v);
                    v.removeSegment();
                    toGoStraightOn.addVehicle(m, v, 0);
                    if (oldLaneLocation != 0) {
                        v.getReputation().correspondingLaneViolation();
                    }
                } else {
                    if (oldLaneLocation != 0) {
                        v.getReputation().correspondingLaneViolation();
                    }


                    ArrayList<Vehicle> victims = toGoStraightOn.getFrontVictims(v,0);

                    for (int i = 0; i < victims.size(); i++) {
                        Vehicle victim = victims.get(i);

                        v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                        v.getDamageStatus().calculateGenerated(v, victim);
                        victim.getDamageStatus().calculatedSuffered(victim,v);
                        victim.getDamageStatus().calculatedSuffered(victim,v);
                    }


                    System.out.println("Collision occurred");
                }
            }

            v.setSegment(toGoStraightOn);
            if(v.getSegment().laneCount() ==1) {
                System.out.println( v + " is going straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". This segment has only one lane on segment " + toGoStraightOn.getSegmentLocation());
            }
            else if(v.getSegment().laneCount() ==2){
                if(v.getSegment().laneLocation(v) ==0){
                    System.out.println(v + " is going straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". Vehicle is on the left lane on segment " + toGoStraightOn.getSegmentLocation());
                } else {
                    System.out.println(v + " is going straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". Vehicle is on the right lane on segment " + toGoStraightOn.getSegmentLocation());
                }

            }
            else if(v.getSegment().laneCount() ==3){
                if(v.getSegment().laneLocation(v) ==0){
                    System.out.println(v + " is going straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". Vehicle is on the left lane on segment " + toGoStraightOn.getSegmentLocation());
                } else  if (v.getSegment().laneLocation(v) ==1){
                    System.out.println(v + " is going straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". Vehicle is on the middle lane on segment " + toGoStraightOn.getSegmentLocation());
                }
                else  if (v.getSegment().laneLocation(v) ==2){
                    System.out.println(v + " is gooing straight at intersection " + toGoStraightOn.getSegmentLocation().x + ". Vehicle is on the right lane on segment " + toGoStraightOn.getSegmentLocation());
                }

            }


        } else {
            System.out.println(v + " cannot go straight at upcoming intersection!");
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
            int oldLaneLocation = s.laneLocation(v);



            if (toTurnLeftOnto.laneCount() > 1) {
                if (s.compatible(toTurnLeftOnto.laneCount())) {

                    if (oldLaneLocation != 0) {
                        if (toTurnLeftOnto.canAdd(v, oldLaneLocation)) {
                            s.removeVehicle(v);
                            v.removeSegment();
                            toTurnLeftOnto.addVehicle(m, v, oldLaneLocation);
                            v.getReputation().correspondingLaneViolation();
                        }
                        else {

                            ArrayList<Vehicle> victims = toTurnLeftOnto.getFrontVictims(v,oldLaneLocation);

                            for (int i = 0; i < victims.size(); i++) {
                                Vehicle victim = victims.get(i);

                                v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                v.getDamageStatus().calculateGenerated(v, victim);
                                victim.getDamageStatus().calculatedSuffered(victim,v);
                                victim.getDamageStatus().calculatedSuffered(victim,v);
                            }

                            System.out.println("Collision occurred");
                        }

                    } else {
                        if (toTurnLeftOnto.canAdd(v, oldLaneLocation)) {
                            s.removeVehicle(v);
                            v.removeSegment();
                            toTurnLeftOnto.addVehicle(m, v, oldLaneLocation);

                        }
                        else {

                            ArrayList<Vehicle> victims = toTurnLeftOnto.getFrontVictims(v,oldLaneLocation);

                            for (int i = 0; i < victims.size(); i++) {
                                Vehicle victim = victims.get(i);

                                v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                v.getDamageStatus().calculateGenerated(v, victim);
                                victim.getDamageStatus().calculatedSuffered(victim,v);
                                victim.getDamageStatus().calculatedSuffered(victim,v);
                            }
                            System.out.println("Collision occurred");

                        }
                    }



                } else {

                    if (s.laneCount() == 3 && toTurnLeftOnto.laneCount() == 2) {
                        if (oldLaneLocation == 2 || oldLaneLocation == 3) {
                            if (toTurnLeftOnto.canAdd(v, 1)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toTurnLeftOnto.addVehicle(m, v, 1);
                                v.getReputation().correspondingLaneViolation();
                            }
                            else {
                                ArrayList<Vehicle> victims = toTurnLeftOnto.getFrontVictims(v,1);

                                for (int i = 0; i < victims.size(); i++) {
                                    Vehicle victim = victims.get(i);

                                    v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                    v.getDamageStatus().calculateGenerated(v, victim);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                }

                                System.out.println("Collision occurred");
                            }
                        } else {
                            if (toTurnLeftOnto.canAdd(v, oldLaneLocation)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toTurnLeftOnto.addVehicle(m, v, oldLaneLocation);

                            }
                            else {
                                ArrayList<Vehicle> victims = toTurnLeftOnto.getFrontVictims(v,oldLaneLocation);

                                for (int i = 0; i < victims.size(); i++) {
                                    Vehicle victim = victims.get(i);

                                    v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                    v.getDamageStatus().calculateGenerated(v, victim);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                }

                                System.out.println("Collision occurred");
                            }
                        }
                    }


                }

            } else {
                if (toTurnLeftOnto.canAdd(v, 0)) {
                    s.removeVehicle(v);
                    v.removeSegment();
                    toTurnLeftOnto.addVehicle(m, v, 0);
                    v.getReputation().correspondingLaneViolation();
                    if (oldLaneLocation != 0) {
                        v.getReputation().correspondingLaneViolation();

                    }
                }
                else {
                    if (oldLaneLocation != 0) {
                        v.getReputation().correspondingLaneViolation();

                    }
                    ArrayList<Vehicle> victims = toTurnLeftOnto.getFrontVictims(v,0);

                    for (int i = 0; i < victims.size(); i++) {
                        Vehicle victim = victims.get(i);

                        v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                        v.getDamageStatus().calculateGenerated(v, victim);
                        victim.getDamageStatus().calculatedSuffered(victim,v);
                        victim.getDamageStatus().calculatedSuffered(victim,v);
                    }

                    System.out.println("Collision occurred");
                }

            }


            v.setSegment(toTurnLeftOnto);

            //////////////////////////////////////////////////////////////////////////////////////////////////
            if(v.getSegment().laneCount() == 1) {
                System.out.println(v + " performed left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x  + ". This segment has only 1 lane on segment " + toTurnLeftOnto.getSegmentLocation());
            }
            else if (v.getSegment().laneCount() == 2){
                if(v.getSegment().laneLocation(v) == 0) {
                    System.out.println(v + " performed left turn at intersection "  + toTurnLeftOnto.getSegmentLocation().x  +  ". Vehicle is now on the left lane on segment " + toTurnLeftOnto.getSegmentLocation());
                } else {
                    System.out.println(v+ " performed left turn at intersection "  + toTurnLeftOnto.getSegmentLocation().x  +     ". Vehicle is now on the right lane on segment " + toTurnLeftOnto.getSegmentLocation());
                }
            }
            else if (v.getSegment().laneCount() == 3){
                if(v.getSegment().laneLocation(v) == 0) {
                    System.out.println(v + " performed left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x  + ". Vehicle is now on the left lane on segment " + toTurnLeftOnto.getSegmentLocation());
                } else if(v.getSegment().laneLocation(v) == 1){
                    System.out.println(v + " performed left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x  + ". Vehicle is now on the middle lane on segment " + toTurnLeftOnto.getSegmentLocation());
                } else if(v.getSegment().laneLocation(v) == 2){
                    System.out.println(v + " performed left turn at intersection " + toTurnLeftOnto.getSegmentLocation().x  + ". Vehicle is now on the right lane on segment " + toTurnLeftOnto.getSegmentLocation());

                }

            }
            /////////////////////////////////////////////////////////////////////////////////////////////

        } else {
            System.out.println(v + "Cannot turn Left at this intersection!");
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
            Segment toTurnRightOnto = getRightTurns(m, s);
            int oldLaneLocation = s.laneLocation(v);


            if (toTurnRightOnto.laneCount() > 1) {
                if (s.compatible(toTurnRightOnto.laneCount())) {

                    if (oldLaneLocation != s.laneCount() - 1) {
                        if(toTurnRightOnto.canAdd(v, oldLaneLocation)) {
                            s.removeVehicle(v);
                            v.removeSegment();
                            toTurnRightOnto.addVehicle(m, v, oldLaneLocation);
                            v.getReputation().correspondingLaneViolation();
                        } else{

                            ArrayList<Vehicle> victims = toTurnRightOnto.getFrontVictims(v,oldLaneLocation);

                            for (int i = 0; i < victims.size(); i++) {
                                Vehicle victim = victims.get(i);

                                v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                v.getDamageStatus().calculateGenerated(v, victim);
                                victim.getDamageStatus().calculatedSuffered(victim,v);
                                victim.getDamageStatus().calculatedSuffered(victim,v);

                            }

                            System.out.println("Collision occurred");
                        }
                    } else {
                        if(toTurnRightOnto.canAdd(v, toTurnRightOnto.laneCount()-1)) {
                            s.removeVehicle(v);
                            v.removeSegment();
                            toTurnRightOnto.addVehicle(m, v, toTurnRightOnto.laneCount()-1);
                        } else{

                            ArrayList<Vehicle> victims = toTurnRightOnto.getFrontVictims(v,toTurnRightOnto.laneCount()-1);

                            for (int i = 0; i < victims.size(); i++) {
                                Vehicle victim = victims.get(i);

                                v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                v.getDamageStatus().calculateGenerated(v, victim);
                                victim.getDamageStatus().calculatedSuffered(victim,v);
                                victim.getDamageStatus().calculatedSuffered(victim,v);

                            }

                            System.out.println("Collision occurred");
                        }
                    }


                } else {

                    if (s.laneCount() == 3 && toTurnRightOnto.laneCount() == 2) {

                        if (oldLaneLocation == 0 || oldLaneLocation == 1) {
                            if(toTurnRightOnto.canAdd(v, 0)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toTurnRightOnto.addVehicle(m, v, 0);
                                v.getReputation().correspondingLaneViolation();
                            } else{
                                ArrayList<Vehicle> victims = toTurnRightOnto.getFrontVictims(v,0);

                                for (int i = 0; i < victims.size(); i++) {
                                    Vehicle victim = victims.get(i);

                                    v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                    v.getDamageStatus().calculateGenerated(v, victim);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);

                                }

                                System.out.println("Collision occurred");
                            }

                        } else {
                            if(toTurnRightOnto.canAdd(v, toTurnRightOnto.laneCount()-1)) {
                                s.removeVehicle(v);
                                v.removeSegment();
                                toTurnRightOnto.addVehicle(m, v, toTurnRightOnto.laneCount()-1);
                            } else{
                                ArrayList<Vehicle> victims = toTurnRightOnto.getFrontVictims(v,toTurnRightOnto.laneCount()-1);

                                for (int i = 0; i < victims.size(); i++) {
                                    Vehicle victim = victims.get(i);

                                    v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                                    v.getDamageStatus().calculateGenerated(v, victim);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);
                                    victim.getDamageStatus().calculatedSuffered(victim,v);

                                }

                                System.out.println("Collision occurred");

                            }
                        }
                    }


                }

            } else {
                if (toTurnRightOnto.canAdd(v, 0)) {
                    s.removeVehicle(v);
                    v.removeSegment();
                    toTurnRightOnto.addVehicle(m, v, 0);
                    if (oldLaneLocation != 0) {
                        v.getReputation().correspondingLaneViolation();
                    }
                } else {
                    if (oldLaneLocation != 0) {
                        v.getReputation().correspondingLaneViolation();

                    }

                    ArrayList<Vehicle> victims = toTurnRightOnto.getFrontVictims(v,0);

                    for (int i = 0; i < victims.size(); i++) {
                        Vehicle victim = victims.get(i);

                        v.getDamageStatus().calculatedSuffered(v, victim);  //// fix
                        v.getDamageStatus().calculateGenerated(v, victim);
                        victim.getDamageStatus().calculatedSuffered(victim,v);
                        victim.getDamageStatus().calculatedSuffered(victim,v);

                    }
                    System.out.println("Collision occurred");

                }
            }

            v.setSegment(toTurnRightOnto);

            //////////////////////////////////////////////////////////////////////////////////////////////////
            if(v.getSegment().laneCount() == 1) {
                System.out.println(v + " performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x  + ". This segment has only 1 lane on segment " + toTurnRightOnto.getSegmentLocation());
            }
            else if (v.getSegment().laneCount() == 2){
                if(v.getSegment().laneLocation(v) == 0) {
                    System.out.println(v + " performed right turn at intersection "  + toTurnRightOnto.getSegmentLocation().x  +  ". Vehicle is now on the left lane on segment " + toTurnRightOnto.getSegmentLocation());
                } else {
                    System.out.println(v + " performed right turn at intersection "  + toTurnRightOnto.getSegmentLocation().x  +     ". Vehicle is now on the right lane on segment " + toTurnRightOnto.getSegmentLocation());
                }
            }
            else if (v.getSegment().laneCount() == 3){
                if(v.getSegment().laneLocation(v) == 0) {
                    System.out.println(v + " performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x  + ". Vehicle is now on the left lane on segment " + toTurnRightOnto.getSegmentLocation());
                } else if(v.getSegment().laneLocation(v) == 1){
                    System.out.println(v + " performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x  + ". Vehicle is now on the middle lane on segment " + toTurnRightOnto.getSegmentLocation());
                } else if(v.getSegment().laneLocation(v) == 2){
                    System.out.println(v + " performed right turn at intersection " + toTurnRightOnto.getSegmentLocation().x  + ". Vehicle is now on the right lane on segment " + toTurnRightOnto.getSegmentLocation());

                }

            }
            /////////////////////////////////////////////////////////////////////////////////////////////

        } else {
            System.out.println(v + "Cannot turn right at this intersection!");
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
            int oldLaneLocation = s.laneLocation(v);
            s.removeVehicle(v);
            v.removeSegment();

            if (toUTurnOnto.laneCount() > 1) {

                if (s.compatible(toUTurnOnto.laneCount()) && toUTurnOnto.laneCount() != 3) {
                    toUTurnOnto.addVehicle(m, v, oldLaneLocation);
                } else {

                    if (s.laneCount() == 1 && toUTurnOnto.laneCount() == 3) {
                        toUTurnOnto.addVehicle(m, v, 1);
                    } else if (s.laneCount() == 3 && toUTurnOnto.laneCount() == 2) {
                        if (s.laneLocation(v) == 1 || s.laneLocation(v) == 1) {
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

   //////////////////////////////////////////////////////////////////////////////////////////////////
            if(v.getSegment().laneCount() == 1) {
                System.out.println(v + " Performed U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x  + ". This segment has only 1 lane on segment " + toUTurnOnto.getSegmentLocation());
            }
            else if (v.getSegment().laneCount() == 2){
                if(v.getSegment().laneLocation(v) == 0) {
                    System.out.println(v + " Performed U-Turn at intersection "  + toUTurnOnto.getSegmentLocation().x  +  ". Vehicle is now on the left lane on segment " + toUTurnOnto.getSegmentLocation());
                } else {
                    System.out.println(v + " Performed U-Turn at intersection "  + toUTurnOnto.getSegmentLocation().x  +     ". Vehicle is now on the right lane on segment " + toUTurnOnto.getSegmentLocation());
                }
            }
            else if (v.getSegment().laneCount() == 3){
                if(v.getSegment().laneLocation(v) == 0) {
                    System.out.println(v + " Performed U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x  + ". Vehicle is now on the left lane on segment " + toUTurnOnto.getSegmentLocation());
                } else if(v.getSegment().laneLocation(v) == 1){
                    System.out.println(v + " Performed U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x  + ". Vehicle is now on the middle lane on segment " + toUTurnOnto.getSegmentLocation());
                } else if(v.getSegment().laneLocation(v) == 2){
                    System.out.println(v + " Performed U-Turn at intersection " + toUTurnOnto.getSegmentLocation().x  + ". Vehicle is now on the right lane on segment " + toUTurnOnto.getSegmentLocation());

                }

            }
    /////////////////////////////////////////////////////////////////////////////////////////////

        }

    }

    //TODO Change sout to only happen when driveable is true


}
