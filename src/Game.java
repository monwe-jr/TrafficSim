import java.awt.*;
import java.util.ArrayList;

public class Game {
     ArrayList<Vehicle> vehicles;



    Game(){

        Map test = new Map(8);
        test.addSegment(new Segment(new Point(0,2),Direction.East));
        test.addSegment(new Segment(new Point(0,4),Direction.North));
        test.addSegment(new Segment(new Point(0,6),Direction.West));
        test.addSegment(new Segment(new Point(0,7),Direction.South));
        test.addSegment(new Segment(new Point(1,4),Direction.West));
        test.addSegment(new Segment(new Point(1,2),Direction.South));
        test.addSegment(new Segment(new Point(2,1),Direction.North));
        test.addSegment(new Segment(new Point(2,0),Direction.West));
        test.addSegment(new Segment(new Point(3,2),Direction.North));
        test.addSegment(new Segment(new Point(4,0),Direction.South));
        test.addSegment(new Segment(new Point(4,1),Direction.East));
        test.addSegment(new Segment(new Point(5,7),Direction.East));
        test.addSegment(new Segment(new Point(6,0),Direction.East));
        test.addSegment(new Segment(new Point(7,3),Direction.East));
        test.addSegment(new Segment(new Point(7,5),Direction.West));


        Intersection name = new Intersection(test,0);
        Segment in6 = new Segment(new Point(5,7), Direction.East);

        Car l = new Car(Color.blue,true, in6);

        System.out.println(l.getSegment().getSegmentLocation());

        Segment n = new Segment(new Point(4,5), Direction.North);



    }



    public static void main(String[] args) {
        new Game();
    }
}
