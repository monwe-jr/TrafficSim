import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    Map m = new Map(8);


    Game() {
        loadMap();

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
            m = (Map)ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves all AI vehicles every time it is called
     */
    private void moveAI() {
        for (Vehicle v : vehicles) {
            if (!v.isDrivable()) {
                Segment goal = v.target;
                ArrayList<Segment> possible = null;
                if (goal == null) {
                    possible = (Turn.getTurns(m, v.getSegment()));
                    if(possible == null) possible = new ArrayList<>();
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
