import java.awt.*;

public class Game {




    Game(){

        Map test = new Map(8);
        test.addSegment(new Segment(new Point(0,2)));
        test.addSegment(new Segment(new Point(0,4)));
        test.addSegment(new Segment(new Point(0,6)));
        test.addSegment(new Segment(new Point(0,7)));
        test.addSegment(new Segment(new Point(1,4)));
        test.addSegment(new Segment(new Point(1,2)));
        test.addSegment(new Segment(new Point(2,1)));
        test.addSegment(new Segment(new Point(2,0)));
        test.addSegment(new Segment(new Point(3,2)));
        test.addSegment(new Segment(new Point(4,0)));
        test.addSegment(new Segment(new Point(4,1)));
        test.addSegment(new Segment(new Point(5,7)));
        test.addSegment(new Segment(new Point(6,0)));
        test.addSegment(new Segment(new Point(7,3)));
        test.addSegment(new Segment(new Point(7,5)));


        Segment in6 = new Segment(new Point(7,3));

        if(in6.canTurn(test,3)){
            System.out.println("yes");
        }


    }



    public static void main(String[] args) {
        new Game();
    }
}
