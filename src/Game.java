import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    Map m = new Map(8);


    Game() {


        m.addSegment(new Segment(new Point(0, 2), Direction.East, 2, 12));
        m.addSegment(new Segment(new Point(0, 4), Direction.North, 3, 12));
        m.addSegment(new Segment(new Point(0, 6), Direction.West, 1, 11));
        m.addSegment(new Segment(new Point(0, 7), Direction.South, 2, 10));
        m.addSegment(new Segment(new Point(1, 4), Direction.West, 3, 8));
        m.addSegment(new Segment(new Point(1, 2), Direction.South, 1, 12));
        m.addSegment(new Segment(new Point(2, 1), Direction.North, 3, 6));
        m.addSegment(new Segment(new Point(2, 0), Direction.West, 2, 112));
        m.addSegment(new Segment(new Point(3, 2), Direction.North, 3, 10));
        m.addSegment(new Segment(new Point(4, 0), Direction.South, 2, 10));
        m.addSegment(new Segment(new Point(4, 1), Direction.East, 3, 10));
        m.addSegment(new Segment(new Point(5, 7), Direction.East, 2, 11));
        m.addSegment(new Segment(new Point(6, 0), Direction.East, 1, 10));
        m.addSegment(new Segment(new Point(7, 3), Direction.East, 3, 12));
        m.addSegment(new Segment(new Point(7, 5), Direction.West, 3, 10));


        Segment in6 = new Segment(new Point(5, 7), Direction.East, 3, 10);


        vehicles.add(new Car(Color.blue, false, in6));
//        vehicles.add(new Car(Color.blue, false, in6));
//        vehicles.add(new Car(Color.blue, false, in6));
        int i = 0;
        for (Vehicle v : vehicles) {
            in6.addVehicle(m, v, i++ % in6.laneCount());
        }
        for (int j = 0; j < 20; j++) {
            moveAI();
        }

    }

    /**
     * Moves all AI vehicles every time it is called
     */
    private void moveAI() {
        for (Vehicle v : vehicles) {
            if (!v.isDrivable()) {
                Segment goal = v.target;
                ArrayList<Segment> possible;
                if (goal == null) {
                    possible = Turn.getTurns(m, v.getSegment());
                    possible.add(Turn.getStraight(m, v.getSegment()));
                    if (possible != null) {
                        Collections.shuffle(possible);
                        goal = possible.get(0);
                        v.target = goal;
                    }
                }
                Direction target = goal.getDirection();
                Direction current = v.getSegment().getDirection();
                if (!v.getSegment().atEnd(v)) {
                    if (Direction.equals(Direction.rightDirection(current), target)) {
                        if (v.getSegment().laneLocation(v) < v.getSegment().laneCount()-1) {
                            if (v.getSegment().canSwitchRight(v)) {
                                v.getSegment().switchRight(v);
                            } else {
                                v.move();
                            }
                        } else {
                            v.move();
                        }
                    } else if (Direction.equals(Direction.leftDirection(current), target)) {
                        if (v.getSegment().laneLocation(v) > 0) {
                            if (v.getSegment().canSwitchLeft(v)) {
                                v.getSegment().switchLeft(v);
                            } else {
                                v.move();
                            }
                        } else {
                            v.move();
                        }
                    } else {
                        v.move();
                    }
                } else {
                    if (Direction.equals(Direction.rightDirection(current), target)) {
                        Turn.rightTurn(m, v.getSegment(), v);
                        v.target = null;
                    } else if (Direction.equals(Direction.leftDirection(current), target)) {
                        Turn.leftTurn(m, v.getSegment(), v);
                        v.target = null;
                    } else {
                        Turn.goStraight(m, v.getSegment(), v);
                        v.target = null;
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        Game game = new Game();
    }
}
