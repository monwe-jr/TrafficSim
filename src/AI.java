import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AI extends Vehicle {
    private ArrayList<Vehicle> AIVehicles = new ArrayList<>();


    public AI(Map m) {
        super(m);

    }


    /**
     * Adds AI at random locations of the map
     * @param amount the amount of AI to be added
     */
    public void addAI(int amount) {
        ArrayList<Segment> mapSegments = map.getSegments();
        VehicleFactory vF = new VehicleFactory();
        Random random = new Random();

        if (amount <= map.AILimit()) {

            for (int i = 0; i < amount; i++) {
                float r = random.nextFloat();
                float g = random.nextFloat();
                float b = random.nextFloat();
                int choice = random.nextInt(3);
                Collections.shuffle(mapSegments);
                Segment s = mapSegments.get((int) (Math.random() * mapSegments.size()));
                Vehicle v;

                if (choice == 0) {
                    v = vF.getVehicle(VehicleFactory.vehicleType.Car, new Color(r, g, b), false);
                } else if (choice == 1) {
                    v = vF.getVehicle(VehicleFactory.vehicleType.Bus, new Color(r, g, b), false);
                } else {
                    v = vF.getVehicle(VehicleFactory.vehicleType.Truck, new Color(r, g, b), false);
                }


                AIVehicles.add(v);


                if (s.canInsertOnSegment(v)) {
                    s.insertVehicle(v);
                } else {
                    for (int j = 0; j < mapSegments.size(); j++) {
                        if (mapSegments.get(j).canInsertOnSegment(v)) {
                            mapSegments.get(j).insertVehicle(v);
                            break;
                        }
                    }
                }

            }


        } else {
            System.out.println("Not enough space for " + amount + " bots. The limit is " + map.AILimit() + "!");
        }
    }


    /**
     * Moves all AI vehicles on the map
     */
    public void moveAI(boolean firstMove, Vehicle player) {
        for (Vehicle v : AIVehicles) {
            if (!player.getDamageStatus().isDestroyed()) {
                if (!v.isDrivable() && !v.getDamageStatus().isDestroyed()) {
                    if (!Turn.canTurn(map, v.getSegment()) && Turn.getStraight(map, v.getSegment()) == null) {
                        if (!v.getSegment().atEnd(v)) {

                            if (v.getSegment().canMove(v)) {
                                v.move();
                            } else {
                                if (v.getSegment().canSwitchLeft(v)) {
                                    if (v.target.getDirection().equals(Direction.leftDirection(v.getSegment().getDirection()))) {
                                        v.getSegment().switchLeft(v);
                                    }

                                } else {
                                    if (v.getSegment().canSwitchRight(v)) {
                                        if (v.target.getDirection().equals(Direction.rightDirection(v.getSegment().getDirection()))) {
                                            v.getSegment().switchRight(v);
                                        }
                                    }
                                }
                            }

                        } else {
                            Turn.uTurn(map, v.getSegment(), v);
                        }
                    } else {
                        Segment goal = v.target;
                        ArrayList<Segment> possible = null;
                        if (goal == null) {
                            possible = (Turn.getTurns(map, v.getSegment()));
                            if (possible == null) possible = new ArrayList<>();
                            possible.add(Turn.getStraight(map, v.getSegment()));
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

                                        if (player.getSegment().vehicleAhead(player)) {
                                            if (player.getSegment().getVehicleAhead(player) == v) {
                                                if (v instanceof Car) {
                                                    System.out.println("There is a car " + (v.getVehicleLocation().x - (player.getVehicleLocation().x + 1)) + " miles ahead of you.");
                                                } else if (v instanceof Bus) {
                                                    System.out.println("There is a bus " + (v.getVehicleLocation().x - (player.getVehicleLocation().x + 1)) + " miles ahead of you.");
                                                } else {
                                                    System.out.println("There is a truck " + (v.getVehicleLocation().x - (player.getVehicleLocation().x + 1)) + " miles ahead of you.");
                                                }
                                            }
                                        }


                                    } else {

                                        if (v.getSegment().canMove(v)) {
                                            v.move();
                                        } else {
                                            if (v.getSegment().canSwitchLeft(v)) {
                                                if (v.target.getDirection().equals(Direction.leftDirection(v.getSegment().getDirection()))) {
                                                    v.getSegment().switchLeft(v);
                                                }

                                            } else {
                                                if (v.getSegment().canSwitchRight(v)) {
                                                    if (v.target.getDirection().equals(Direction.rightDirection(v.getSegment().getDirection()))) {
                                                        v.getSegment().switchRight(v);
                                                    }
                                                }
                                            }


                                        }

                                        if (v.getSegment().inFrontOfPlayer(v)) {
                                            if (v instanceof Car) {
                                                System.out.println("There is a car in front of you.");

                                            } else if (v instanceof Bus) {
                                                System.out.println("There is bus in front of you.");

                                            } else {
                                                System.out.println("There is a truck in front of you.");

                                            }

                                        }
                                    }
                                } else {

                                    if (v.getSegment().canMove(v)) {
                                        v.move();
                                    } else {
                                        if (v.getSegment().canSwitchLeft(v)) {
                                            if (v.target.getDirection().equals(Direction.leftDirection(v.getSegment().getDirection()))) {
                                                v.getSegment().switchLeft(v);
                                            }

                                        } else {
                                            if (v.getSegment().canSwitchRight(v)) {
                                                if (v.target.getDirection().equals(Direction.rightDirection(v.getSegment().getDirection()))) {
                                                    v.getSegment().switchRight(v);
                                                }
                                            }
                                        }
                                    }

                                    if (v.getSegment().inFrontOfPlayer(v)) {
                                        if (v instanceof Car) {
                                            System.out.println("There is a car in front of you.");

                                        } else if (v instanceof Bus) {
                                            System.out.println("There is bus in front of you.");

                                        } else {
                                            System.out.println("There is a truck in front of you.");

                                        }
                                    }

                                }

                            } else if (Direction.equals(Direction.leftDirection(current), target)) {
                                if (v.getSegment().laneLocation(v) > 0) {
                                    if (v.getSegment().canSwitchLeft(v)) {
                                        v.getSegment().switchLeft(v);

                                        if (player.getSegment().vehicleAhead(player)) {
                                            if (player.getSegment().getVehicleAhead(player) == v) {
                                                if (v instanceof Car) {
                                                    System.out.println("There is a car " + (v.getVehicleLocation().x - (player.getVehicleLocation().x + 1)) + " miles ahead of you.");
                                                } else if (v instanceof Bus) {
                                                    System.out.println("There is a bus " + (v.getVehicleLocation().x - (player.getVehicleLocation().x + 1)) + " miles ahead of you.");
                                                } else {
                                                    System.out.println("There is a truck " + (v.getVehicleLocation().x - (player.getVehicleLocation().x + 1)) + " miles ahead of you.");
                                                }
                                            }
                                        }

                                    } else {

                                        if (v.getSegment().canMove(v)) {
                                            v.move();
                                        } else {
                                            if (v.getSegment().canSwitchLeft(v)) {
                                                if (v.target.getDirection().equals(Direction.leftDirection(v.getSegment().getDirection()))) {
                                                    v.getSegment().switchLeft(v);
                                                }

                                            } else {
                                                if (v.getSegment().canSwitchRight(v)) {
                                                    if (v.target.getDirection().equals(Direction.rightDirection(v.getSegment().getDirection()))) {
                                                        v.getSegment().switchRight(v);
                                                    }
                                                }
                                            }
                                        }

                                        if (v.getSegment().inFrontOfPlayer(v)) {
                                            if (v instanceof Car) {
                                                System.out.println("There is a car in front of you.");

                                            } else if (v instanceof Bus) {
                                                System.out.println("There is bus in front of you.");

                                            } else {
                                                System.out.println("There is a truck in front of you.");

                                            }
                                        }

                                    }
                                } else {

                                    if (v.getSegment().canMove(v)) {
                                        v.move();
                                    } else {
                                        if (v.getSegment().canSwitchLeft(v)) {
                                            if (v.target.getDirection().equals(Direction.leftDirection(v.getSegment().getDirection()))) {
                                                v.getSegment().switchLeft(v);
                                            }

                                        } else {
                                            if (v.getSegment().canSwitchRight(v)) {
                                                if (v.target.getDirection().equals(Direction.rightDirection(v.getSegment().getDirection()))) {
                                                    v.getSegment().switchRight(v);
                                                }
                                            }
                                        }
                                    }

                                    if (v.getSegment().inFrontOfPlayer(v)) {
                                        if (v instanceof Car) {
                                            System.out.println("There is a car in front of you.");

                                        } else if (v instanceof Bus) {
                                            System.out.println("There is bus in front of you.");

                                        } else {
                                            System.out.println("There is a truck in front of you.");

                                        }
                                    }

                                }

                            } else {

                                if (v.getSegment().canMove(v)) {
                                    v.move();
                                } else {
                                    if (v.getSegment().canSwitchLeft(v)) {
                                        if (v.target.getDirection().equals(Direction.leftDirection(v.getSegment().getDirection()))) {
                                            v.getSegment().switchLeft(v);
                                        }

                                    } else {
                                        if (v.getSegment().canSwitchRight(v)) {
                                            if (v.target.getDirection().equals(Direction.rightDirection(v.getSegment().getDirection()))) {
                                                v.getSegment().switchRight(v);
                                            }
                                        }
                                    }
                                }

                                if (v.getSegment().inFrontOfPlayer(v)) {
                                    if (v instanceof Car) {
                                        System.out.println("There is a car in front of you.");

                                    } else if (v instanceof Bus) {
                                        System.out.println("There is bus in front of you.");

                                    } else {
                                        System.out.println("There is a truck in front of you.");

                                    }
                                }

                            }
                        } else {

                            if (Direction.equals(Direction.rightDirection(current), target)) {

                                Turn.rightTurn(map, v.getSegment(), v, firstMove);
                                v.target = null;
                            } else if (Direction.equals(Direction.leftDirection(current), target)) {

                                Turn.leftTurn(map, v.getSegment(), v, firstMove);
                                v.target = null;
                            } else {
                                Turn.goStraight(map, v.getSegment(), v, firstMove);
                                v.target = null;
                            }

                        }
                    }
                }

            }
        }
    }


//TODO remove AI sout

}
