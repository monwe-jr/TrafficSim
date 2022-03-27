import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    Random random = new Random();
    Map m = new Map(8);
    private Game instance = null;

    public Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private Game() {

//m.erase();
//saveMap();
//        m.addSegment(new Segment(new Point(0,4), Direction.North,3,4));
//        m.addSegment(new Segment(new Point(0,2), Direction.East,2,4));
//        m.addSegment(new Segment(new Point(0,6), Direction.West,2,4));
//        m.addSegment(new Segment(new Point(0,7), Direction.South,2,4));
//        m.addSegment(new Segment(new Point(1,4),Direction.West,2,4) );
//        m.addSegment(new Segment(new Point(1,2), Direction.South,3,4));
//        m.addSegment(new Segment(new Point(2,1), Direction.North,3,4));
//        m.addSegment(new Segment(new Point(2,0), Direction.West,2,4));
//        m.addSegment(new Segment(new Point(3,2), Direction.North,1,4));
//        m.addSegment(new Segment(new Point(4,1), Direction.East,2,4));
//        m.addSegment(new Segment(new Point(4,0), Direction.South,3,4));
//        m.addSegment(new Segment(new Point(5,7), Direction.East,2,4));
//        m.addSegment(new Segment(new Point(6,0), Direction.East,2,4));
//        m.addSegment(new Segment(new Point(7,3), Direction.East,3,4));
//        m.addSegment(new Segment(new Point(7,5), Direction.West,3,4));
//saveMap();
        loadMap();


//        Segment in6 = new Segment(new Point(2, 0), Direction.West, 2, 4);
//        int a = 1;
//
//        vehicles.add(new Car(Color.blue, false, in6));
////        vehicles.add(new Car(Color.blue, false, in6));
////        vehicles.add(new Car(Color.blue, false, in6));
//
//
//        int i = 0;
//        for (Vehicle v : vehicles) {
//            in6.addVehicle(m, v, i++ % in6.laneCount());
//        }
//

        addAI(1);
        for (int j = 0; j < 1000; j++) {
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
            m = (Map) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void addAI(int amount) {
        ArrayList<Segment> mapSegments = m.getSegments();

        for (int i = 0; i < amount; i++) {
            float r = random.nextFloat();
            float g = random.nextFloat();
            float b = random.nextFloat();
            int choice = random.nextInt(3);
            Segment s = mapSegments.get(random.nextInt(mapSegments.size()));
            int laneCount = random.nextInt(s.laneCount());

            if (choice == 0) {
                Vehicle v = new Car(new Color(r, g, b), false, s);
                vehicles.add(v);

                if (s.isEmpty(new Point(0, laneCount))) {
                    s.addVehicle(m, v, laneCount);
                } else {
                    forceAdd(v, s);
                }

            } else if (choice == 1) {
                Vehicle v = new Bus(new Color(r, g, b), false, s);
                vehicles.add(v);

                if (s.isEmpty(new Point(0, laneCount))) {
                    s.addVehicle(m, v, laneCount);
                } else {
                    forceAdd(v, s);
                }

            } else if (choice == 2) {
                Vehicle v = new Truck(new Color(r, g, b), false, s);
                vehicles.add(v);

                if (s.isEmpty(new Point(0, laneCount))) {
                    s.addVehicle(m, v, laneCount);
                } else {
                    forceAdd(v, s);
                }



            }
        }
    }



    private void forceAdd(Vehicle v, Segment s) {
        ArrayList<Segment> mapSegments = m.getSegments();


        if (s.canInsert(v)) {
            ArrayList<Integer> alternates = s.alternateInserts(v);
            s.addVehicle(m, v, alternates.get(random.nextInt(alternates.size())));

        } else {
            Segment choice = mapSegments.get(random.nextInt(mapSegments.size()));

            if (choice.canInsert(v)) {
                ArrayList<Integer> alternates = choice.alternateInserts(v);
                choice.addVehicle(m, v, alternates.get(random.nextInt(alternates.size())));
            } else {

                for (int i = 0; i < mapSegments.size(); i++) {
                    if (mapSegments.get(i).canInsert(v)) {
                        ArrayList<Integer> alternates = choice.alternateInserts(v);
                        choice.addVehicle(m, v, alternates.get(random.nextInt(alternates.size())));
                        break;
                    }
                }
            }


        }

    }


    /**
     * Moves all AI vehicles every time it is called
     */
    private void moveAI() {
        for (Vehicle v : vehicles) {
            if (!v.isDrivable()) {
                if (!Turn.canTurn(m, v.getSegment()) && Turn.getStraight(m, v.getSegment()) == null) {
                    if (!v.getSegment().atEnd(v)) {
                        v.move();
                    } else {
                        Turn.uTurn(m, v.getSegment(), v);
                    }
                } else {
                    Segment goal = v.target;
                    ArrayList<Segment> possible = null;
                    if (goal == null) {
                        possible = (Turn.getTurns(m, v.getSegment()));
                        if (possible == null) possible = new ArrayList<>();
                        possible.add(Turn.getStraight(m, v.getSegment()));
                        if (possible != null) {
                            possible.removeAll(Collections.singleton(null));
                            Collections.shuffle(possible);
                            goal = possible.get((int) (Math.random() * possible.size()));
                            v.target = goal;
                        }
                    }
                    Direction target = goal.getDirection();
                    Direction current = v.getSegment().getDirection();
                    if (!v.getSegment().atEnd(v)) {
                        if (Direction.equals(Direction.rightDirection(current), target)) {
                            if (v.getSegment().laneLocation(v) < v.getSegment().laneCount() - 1) {
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
    }


    public static void main(String[] args) {
        Game game = new Game();
    }
}