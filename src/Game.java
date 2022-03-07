import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    ArrayList<Vehicle> vehicles;
    Map m;


    Game() {

        m = new Map(8);
        m.addSegment(new Segment(new Point(0, 2), Direction.East));
        m.addSegment(new Segment(new Point(0, 4), Direction.North));
        m.addSegment(new Segment(new Point(0, 6), Direction.West));
        m.addSegment(new Segment(new Point(0, 7), Direction.South));
        m.addSegment(new Segment(new Point(1, 4), Direction.West));
        m.addSegment(new Segment(new Point(1, 2), Direction.South));
        m.addSegment(new Segment(new Point(2, 1), Direction.North));
        m.addSegment(new Segment(new Point(2, 0), Direction.West));
        m.addSegment(new Segment(new Point(3, 2), Direction.North));
        m.addSegment(new Segment(new Point(4, 0), Direction.South));
        m.addSegment(new Segment(new Point(4, 1), Direction.East));
        m.addSegment(new Segment(new Point(5, 7), Direction.East));
        m.addSegment(new Segment(new Point(6, 0), Direction.East));
        m.addSegment(new Segment(new Point(7, 3), Direction.East));
        m.addSegment(new Segment(new Point(7, 5), Direction.West));


        Segment in6 = new Segment(new Point(5, 7), Direction.East);

        Car l = new Car(Color.blue, true, in6);
        in6.addVehicle(m, l, in6.laneCount() - 1);

        l.move();
        vehicles.add(new Car(Color.blue, false, in6));
        vehicles.add(new Car(Color.blue, false, in6));
        vehicles.add(new Car(Color.blue, false, in6));
        int i = 0;
        for (Vehicle v : vehicles) {
            in6.addVehicle(m, v, i++);
        }
        for (int j = 0; j < 100; j++) {
            moveAI();
        }

    }

    /**
     * Moves all AI vehicles every time it is called
     */
    private void moveAI() {
        for (Vehicle v : vehicles) {
            if (!v.isDrivable()) {
                if (!v.getSegment().atEnd(v)) {
                    v.move();
                    return;
                } else {
                    ArrayList<Segment> possible = Turn.getTurns(m, v.getSegment());
                    if (possible != null) {
                        Collections.shuffle(possible);
                        Direction target = possible.get(0).getDirection();
                        Direction current = v.getSegment().getDirection();

                        if (Direction.equals(Direction.rightDirection(current), target)) {
                            Turn.rightTurn(m, v.getSegment(), v);
                            return;
                        } else if (Direction.equals(Direction.leftDirection(current), target)) {
                            Turn.leftTurn(m, v.getSegment(), v);
                            return;
                        } else {
                            Turn.goStraight(m, v.getSegment(), v);
                            return;
                        }
                    } else {
                        Turn.uTurn(m, v.getSegment(), v);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
    }
}
