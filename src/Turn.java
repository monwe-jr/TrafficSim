import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Turn {


  static private Segment getSegment(Map m, Point segment ){
      ArrayList<ArrayList<Segment>> segments = m.getMap();
      ArrayList<Segment> results = new ArrayList<>();
      segments.forEach(s -> results.addAll(s));
      results.stream()
              .filter(s -> s.getSegmentLocation().equals(segment))
              .collect(Collectors.toList());
      if (results.isEmpty()) return null;
      return results.get(0);

        /*for (int i = 0; i < m.getMap().size(); i++) {
            for (int j = 0; j < m.getMap().get(i).size(); j++) {
                if(m.getMap().get(i).get(j).getSegmentLocation().equals(segment)){
                    return m.getMap().get(i).get(j);
                }
            }
        }

        return null;*/
    }



  static   private Segment getStraight(Map m, Segment s){
        ArrayList<Segment> options = getTurns(m,s);

        options.stream()
                .filter(r -> r.getDirection() == Direction.straightDirection(r.getDirection()))
                .collect(Collectors.toList());
        if(options.isEmpty()) return null;
        return options.get(0);

        /*for (int i = 0; i < options.size(); i++) {
            if(options.get(i).getDirection() == Direction.straightDirection(s.getDirection())){
                    return options.get(i);
            }else{
                System.out.println("Can't go straight!");
            }
        }

            return null;*/

    }



  static   private Segment getLeftTurns(Map m, Segment s){
        ArrayList<Segment> options = getTurns(m,s);

        for (int i = 0; i < options.size(); i++) {
            if(options.get(i).getDirection() == Direction.leftDirection(s.getDirection())){
                return options.get(i);
            } else{
                System.out.println("Can't go left!");
            }
        }

        return null;

    }

   static private Segment getRightTurns(Map m, Segment s){
        ArrayList<Segment> options = getTurns(m,s);

        for (int i = 0; i < options.size(); i++) {
            if(options.get(i).getDirection() == Direction.rightDirection(s.getDirection())){
                return options.get(i);
            }else{
                System.out.println("Can't go right!");
            }
        }

        return null;

    }


    static public  ArrayList<Segment> getTurns(Map m, Segment s){
       ArrayList<Segment> availableTurns = new ArrayList<>();

       if(s.canTurn(m)) {
           for (int j = 0; j < m.getMap().size(); j++) {
               if (m.getMap().get(s.getSegmentLocation().y).get(j).getSegmentLocation().x == s.getSegmentLocation().y && !m.getMap().get(s.getSegmentLocation().y).get(j).getSegmentLocation().equals(new Point(s.getSegmentLocation().y,s.getSegmentLocation().x)) ) {
                   availableTurns.add(m.getMap().get(s.getSegmentLocation().y).get(j));
               }
           }
       } else{
           System.out.println("Can't turn at upcoming intersection");
           return null;
       }

        return availableTurns;
    }


    /**
     * Vehicles goes straight when method is call. Traffic violation is applied if player is not on the appropriate lane
     * @param m game map
     * @param s the segment player is turning from
     * @param v vehicle that is turning
     */
    static public void goStraight(Map m, Segment s,Vehicle v){

        if(getStraight(m,s) != null){
            Segment toGoStraightOn = getStraight(m,s);
            int oldLaneLocation = s.getLane().laneLocation(v);
            s.removeVehicle(v);
            v.removeSegment(s);

            if(toGoStraightOn.getLane().getLaneCount() >1){
                    if(s.getLane().compatible(toGoStraightOn.getLane().getLaneCount())){
                        toGoStraightOn.addVehicle(v, oldLaneLocation);
                    }else {


                        if (s.getLane().getLaneCount() == 3 && toGoStraightOn.getLane().getLaneCount() == 2) {
                            if (oldLaneLocation == 2) {
                                toGoStraightOn.addVehicle(v, 1);
                                //add traffic violation
                            } else {
                                toGoStraightOn.addVehicle(v, oldLaneLocation);
                            }
                        }


                    }

            }else{
                if(oldLaneLocation !=0) {
                    toGoStraightOn.addVehicle(v, 0);
                    //add traffic violation
                } else {
                    toGoStraightOn.addVehicle(v, 0);
                }
            }

        } else{
            System.out.println("Cannot go straight at this intersection!");
        }

    }


    /**
     * Vehicles turn left when t his method is call. Traffic violation is applied if player is not on the appropriate lane
     * @param m game map
     * @param s the segment player is turning from
     * @param v vehicle that is turning
     */
    static public void leftTurn(Map m, Segment s,Vehicle v){
      if(getLeftTurns(m,s) != null){
          Segment toTurnLeftOnto = getLeftTurns(m,s);
          int oldLaneLocation = s.getLane().laneLocation(v);
          s.removeVehicle(v);
          v.removeSegment(s);

          if(toTurnLeftOnto.getLane().getLaneCount() >1){
              if(s.getLane().compatible(toTurnLeftOnto.getLane().getLaneCount())){

                  if(oldLaneLocation != 0){
                      toTurnLeftOnto.addVehicle(v, oldLaneLocation);
                      // add traffic violation
                  } else {
                      toTurnLeftOnto.addVehicle(v, oldLaneLocation);
                  }


              }else {

                  if (s.getLane().getLaneCount() == 3 && toTurnLeftOnto.getLane().getLaneCount() == 2) {
                      if (oldLaneLocation == 2 || oldLaneLocation == 3) {
                          toTurnLeftOnto.addVehicle(v, 1);
                          //add traffic violation
                      } else {
                          toTurnLeftOnto.addVehicle(v, oldLaneLocation);
                      }
                  }


              }

          }else{
              if(oldLaneLocation !=0) {
                  toTurnLeftOnto.addVehicle(v, 0);
                  //add traffic violation

              } else {
                  toTurnLeftOnto.addVehicle(v, 0);
              }
          }



      } else {
          System.out.println("Cannot turn Left at this intersection!");
      }
    }


    /**
     * Vehicles turn right when t his method is call. Traffic violation is applied if player is not on the appropriate lane
     * @param m game map
     * @param s the segment player is turning from
     * @param v vehicle that is turning
     */
    static public void rightTurn(Map m, Segment s,Vehicle v){
        if(getRightTurns(m,s) != null){
            Segment toTurnRightOnto = getLeftTurns(m,s);
            int oldLaneLocation = s.getLane().laneLocation(v);
            s.removeVehicle(v);
            v.removeSegment(s);

            if(toTurnRightOnto.getLane().getLaneCount() >1){
                if(s.getLane().compatible(toTurnRightOnto.getLane().getLaneCount())){

                    if(oldLaneLocation != s.getLane().getLaneCount() - 1){
                        toTurnRightOnto.addVehicle(v, oldLaneLocation);
                        // add traffic violation
                    } else {
                        toTurnRightOnto.addVehicle(v, oldLaneLocation);
                    }


                }else {

                    if (s.getLane().getLaneCount() == 3 && toTurnRightOnto.getLane().getLaneCount() == 2) {
                        if (oldLaneLocation == 0 || oldLaneLocation == 1) {
                            toTurnRightOnto.addVehicle(v, 0);
                            //add traffic violation
                        } else {
                            toTurnRightOnto.addVehicle(v, oldLaneLocation);
                        }
                    }


                }

            }else{
                if(oldLaneLocation != s.getLane().getLaneCount() - 1) {
                    toTurnRightOnto.addVehicle(v, 0);
                    //add traffic violation

                } else {
                    toTurnRightOnto.addVehicle(v, 0);
                }
            }



        } else {
            System.out.println("Cannot turn right at this intersection!");
        }
    }






    /**
     * performs uTurn if intersection is a dead end. If the lanes are compatible, you turn to the corresponding lane. If not, you turn to the most logical choice without receiving a violation
     * @param m game map
     * @param s the segment player is turning from
     * @param v the vehicle that will be turning
     */
     public void uTurn(Map m, Segment s,Vehicle v){
        if(Intersection.isDeadEnd(m,s.getSegmentLocation().y)){
            Segment toUTurnOnto = getSegment(m,new Point(s.getSegmentLocation().y,s.getSegmentLocation().x));
            int oldLaneLocation = s.getLane().laneLocation(v);
            s.removeVehicle(v);
            v.removeSegment(s);

            if(toUTurnOnto.getLane().getLaneCount() > 1) {

                if (s.getLane().compatible(toUTurnOnto.getLane().getLaneCount()) && toUTurnOnto.getLane().getLaneCount() !=3) {
                    toUTurnOnto.addVehicle(v, oldLaneLocation);
                } else{

                     if (s.getLane().getLaneCount() == 1 && toUTurnOnto.getLane().getLaneCount() == 3){
                        toUTurnOnto.addVehicle(v, 1);
                    }

                     else if(s.getLane().getLaneCount() == 3 && toUTurnOnto.getLane().getLaneCount() == 2){
                         if(s.getLane().laneLocation(v) == 1 || s.getLane().laneLocation(v) == 1){
                             toUTurnOnto.addVehicle(v, 1);
                         } else {
                             toUTurnOnto.addVehicle(v, 0);
                         }

                     }

                }

            }else {
                toUTurnOnto.addVehicle(v, 0);
            }

            v.addSegment(toUTurnOnto);
        }
    }






}
