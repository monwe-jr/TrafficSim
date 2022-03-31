import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Game {
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private Map m = new Map(8);
    private static Game instance = null;
    private Players player = new Players(m);
    private AI AI = new AI(m);


    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private Game() {


        m.addSegment(new Segment(new Point(0, 4), Direction.North, 3, 6));
        m.addSegment(new Segment(new Point(0, 2), Direction.East, 3, 6));
        m.addSegment(new Segment(new Point(0, 6), Direction.West, 3, 6));
        m.addSegment(new Segment(new Point(0, 7), Direction.South, 3, 6));
        m.addSegment(new Segment(new Point(1, 4), Direction.West, 2, 4));
        m.addSegment(new Segment(new Point(1, 2), Direction.South, 3, 6));
        m.addSegment(new Segment(new Point(2, 1), Direction.North, 3, 6));
        m.addSegment(new Segment(new Point(2, 0), Direction.West, 2, 5));
        m.addSegment(new Segment(new Point(3, 2), Direction.North, 1, 4));
        m.addSegment(new Segment(new Point(4, 1), Direction.East, 2, 5));
        m.addSegment(new Segment(new Point(4, 0), Direction.South, 3, 4));
        m.addSegment(new Segment(new Point(5, 7), Direction.East, 3, 6));
        m.addSegment(new Segment(new Point(6, 0), Direction.East, 2, 6));
        m.addSegment(new Segment(new Point(7, 3), Direction.East, 3, 6));
        m.addSegment(new Segment(new Point(7, 5), Direction.West, 3, 6));


        prompt();


    }


    private void initialize() {
        Scanner input = new Scanner(System.in);
        boolean firstMove = true;
        Vehicle v = player.getPlayers();

        if(!v.getSegment().atEnd(v)) {
            System.out.println("Enter 'w' to move forward.");
            System.out.println("Enter 's' to wait.");
            System.out.println("Enter 'a' to switch lanes to the left.");
            System.out.println("Enter 'd' to switch lanes to the right.");
            System.out.println("Enter 'l' to activate listener.");
            System.out.println("Enter 'end' to end the game.");
            System.out.println();
        }


        while (!v.getDamageStatus().isDestroyed()) {
            if (!v.getDamageStatus().isDestroyed()) {
                if (!v.getSegment().atEnd(v)) {
                    String c = input.nextLine();

                    while (!c.equals("w") && !c.equals("s") && !c.equals("a") && !c.equals("d") && !c.equals("l") && !c.equals("end")) {
                        System.out.println("Please pick a valid choice!");
                        c = input.nextLine();
                    }


                    if (c.equals("w")) {
                        System.out.println();
                        v.move();
                        System.out.println();
                        AI.moveAI(firstMove,v);
                        System.out.println();
                    } else if (c.equals("s")) {
                        ///////////////
                        continue;
                    } else if (c.equals("a")) {
                        System.out.println();
                        v.getSegment().switchLeft(v);
                        System.out.println();
                        AI.moveAI(firstMove,v);
                        System.out.println();
                    } else if (c.equals("d")) {
                        System.out.println();
                        v.getSegment().switchRight(v);
                        System.out.println();
                        AI.moveAI(firstMove,v);
                        System.out.println();
                    } else if (c.equals("l")) {
                        v.getSegment().callListener(m, v);
                        System.out.println();
                    } else {
                        return;
                    }


                } else {
                    if(Intersection.isDeadEnd(m,v.getSegment().getSegmentLocation().y)){
                        Turn.uTurn(m,v.getSegment(),v);
                    }


                    System.out.println("Enter 'w' to go straight.");
                    System.out.println("Enter 's' to wait.");
                    System.out.println("Enter 'a' to turn left.");
                    System.out.println("Enter 'd' to turn right.");
                    System.out.println("Enter 'l' to activate listener.");
                    System.out.println("Enter 'end' to end the game.");
                    System.out.println();
                    String c = input.nextLine();

                    while (!c.equals("w") && !c.equals("s") && !c.equals("a") && !c.equals("d") && !c.equals("l") && !c.equals("end")) {
                        System.out.println("Please pick a valid choice!");
                        c = input.nextLine();
                    }

                    if (c.equals("w")) {
                        if (Turn.canGoStraight(m, v.getSegment())) {
                            System.out.println();
                            Turn.goStraight(m, v.getSegment(), v, firstMove);
                            System.out.println();
                            AI.moveAI(firstMove,v);
                            System.out.println();
                        } else {
                            System.out.println();
                            System.out.println("You cannot go straight at this intersection!");
                            AI.moveAI(firstMove,v);
                            System.out.println();
                            continue;

                        }


                    } else if (c.equals("s")) {
                        ///////////////
                        continue;
                    } else if (c.equals("a")) {

                        if (Turn.canLeftTurn(m, v.getSegment())) {
                            Turn.leftTurn(m, v.getSegment(), v, firstMove);
                            System.out.println();
                            AI.moveAI(firstMove,v);
                            System.out.println();
                        } else {
                            System.out.println();
                            System.out.println("You cannot turn right at this intersection!");
                            System.out.println();
                            AI.moveAI(firstMove,v);
                            System.out.println();
                            continue;

                        }

                    } else if (c.equals("d")) {

                        if (Turn.canRightTurn(m, v.getSegment())) {
                            Turn.rightTurn(m, v.getSegment(), v, firstMove);
                            System.out.println();
                            AI.moveAI(firstMove,v);
                            System.out.println();

                        } else {
                            System.out.println();
                            System.out.println("You cannot turn right at this intersection!");
                            System.out.println();
                            AI.moveAI(firstMove,v);
                            System.out.println();
                            continue;
                        }

                    } else if (c.equals("l")) {
                        v.getSegment().callListener(m, v);
                        System.out.println();
                    } else {
                       return;
                    }




                    System.out.println("Enter 'w' to move forward.");
                    System.out.println("Enter 's' to wait.");
                    System.out.println("Enter 'a' to switch lanes to the left.");
                    System.out.println("Enter 'd' to switch lanes to the right.");
                    System.out.println("Enter 'l' to activate listener.");
                    System.out.println("Enter 'end' to end the game.");
                    System.out.println();

                }

                firstMove = false;

            }
        }

    }


    private void prompt() {
        Scanner input = new Scanner(System.in);

        System.out.println("You can have up to " + m.AILimit() + " AI vehicles on the map. How many AI vehicles do you want to add on the map?");
        int AICount = input.nextInt();

        while (AICount > m.AILimit()) {
            System.out.println("The maximum number of AI vehicles you can add to the map is " + m.AILimit() + ". Please make another selection.");
            AICount = input.nextInt();
        }

        AI.addAI(AICount);
        System.out.println();
        System.out.println(AICount + " AI added!");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        player.prompt();
        Vehicle v = player.getPlayers();
        initialize();
    }


    //example implementation
    public void saveMap() {
        try {
            FileOutputStream fos = new FileOutputStream("Map.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadMap() {
        try {
            FileInputStream fis = new FileInputStream("Map.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            m = (Map) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Game();
    }
}