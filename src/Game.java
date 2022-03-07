import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    ArrayList<Vehicle> vehicles;
    Map m;


    Game() {

        Map test = new Map(8);
        test.addSegment(new Segment(new Point(0, 2), Direction.East));
        test.addSegment(new Segment(new Point(0, 4), Direction.North));
        test.addSegment(new Segment(new Point(0, 6), Direction.West));
        test.addSegment(new Segment(new Point(0, 7), Direction.South));
        test.addSegment(new Segment(new Point(1, 4), Direction.West));
        test.addSegment(new Segment(new Point(1, 2), Direction.South));
        test.addSegment(new Segment(new Point(2, 1), Direction.North));
        test.addSegment(new Segment(new Point(2, 0), Direction.West));
        test.addSegment(new Segment(new Point(3, 2), Direction.North));
        test.addSegment(new Segment(new Point(4, 0), Direction.South));
        test.addSegment(new Segment(new Point(4, 1), Direction.East));
        test.addSegment(new Segment(new Point(5, 7), Direction.East));
        test.addSegment(new Segment(new Point(6, 0), Direction.East));
        test.addSegment(new Segment(new Point(7, 3), Direction.East));
        test.addSegment(new Segment(new Point(7, 5), Direction.West));




        Segment in6 = new Segment(new Point(5, 7), Direction.East);

        Car l = new Car(Color.blue, true,in6);
        in6.addVehicle(test,l, in6.laneCount()-1);

        l.move();




    }

    /**
     * Moves all AI vehicles every time it is called
     */
    private void moveAI() {
        for (Vehicle v: vehicles) {
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

    public static void main(String[] args) {
        new Game();
    }
}
