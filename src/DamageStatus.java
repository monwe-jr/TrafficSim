import java.util.ArrayList;

public class DamageStatus {

    private boolean destroyed;
    private double currentStatus = 100.0;
    private ArrayList<Double> sufferedDamageHistory = new ArrayList<>();
    private ArrayList<Double> generatedDamageHistory = new ArrayList<>();
    private Reputation currentReputation; //reputation of the vehicle
    private Vehicle type;  //vehicle at fault


    DamageStatus(Reputation r, Vehicle v) {
        this.currentReputation = r;
        this.type = v;
    }


    /**
     * Returns the generated damage history of a vehicle
     * @return arraylist
     */
    public ArrayList<Double> getGeneratedDamageHistory() {
        return generatedDamageHistory;
    }


    /**
     * Returns the suffered damage history of a vehicle
     * @return arraylist
     */
    public ArrayList<Double> getSufferedDamageHistory() {
        return sufferedDamageHistory;
    }


    /**
     * This method updates the currentStatus variable
     * @param d the difference
     */
    public void updateStatus(double d) {
        if (currentStatus - d > 0) {
            currentStatus -= d;
            sufferedDamageHistory.add(d);

            if (type.isDrivable()) {
                System.out.println("Your health is now " + currentStatus + "!");
            }

        } else {
            destroyed = true;
            currentReputation.destroyed();
            type.getSegment().removeVehicle(type);
            type.removeSegment();
            type.setVehicleLocation(null);

            if (type.isDrivable()) {
                System.out.println("Your vehicle has been destroyed!");
                System.out.println("Game over!");
            }

        }


    }


    /**
     * This method returns the current health of the vehicle
     * @return currentStatus variable
     */
    public double getHealth() {
        return currentStatus;
    }


    /**
     * This method returns the current value of the isDestroyed variable
     * @return true or false
     */
    public boolean isDestroyed() {
        if (destroyed) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * This method handles the game logic of front collisions
     *
     * @param victim
     */
    public void frontCollision(Vehicle victim) {

        if (victim.isDrivable()) {
            if (type instanceof Car) {
                System.out.println("A car rear ended your vehicle!");
            } else if (type instanceof Bus) {
                System.out.println("A bus rear ended your vehicle!");
            } else {
                System.out.println("A truck rear ended your vehicle!");
            }
        }

        calculatedSufferedFront(victim);
        calculateGeneratedFront(victim);
        calculatedSufferedBack(victim);
        calculateGeneratedBack(victim);

        currentReputation.calculateReputation(sufferedDamageHistory, generatedDamageHistory, true);
        victim.getReputation().calculateReputation(victim.getDamageStatus().getSufferedDamageHistory(), victim.getDamageStatus().getGeneratedDamageHistory(), false);
        currentReputation.atFaultViolation();


    }


    /**
     * This method handles the game logic of side collisions
     *
     * @param victims array list of victims if vehicle is bus or truck
     */
    public void sideCollision(ArrayList<Vehicle> victims) {
        boolean car = false;
        boolean bus = false;
        boolean truck = false;
        boolean victim = false;


        for (int i = 0; i < victims.size(); i++) {
            if (victims.get(i) instanceof Car) {
                car = true;
            } else if (victims.get(i) instanceof Bus) {
                bus = true;
            } else {
                truck = true;
            }

            if (victims.get(i).isDrivable()) {
                victim = true;
            }

        }

        if (victim) {
            if (type instanceof Car) {
                System.out.println("A car sideswiped your vehicle!");
            } else if (type instanceof Bus) {
                System.out.println("A bus sideswiped your vehicle!");
            } else {
                System.out.println("A truck sideswiped your vehicle!");
            }
        }


        for (int i = 0; i < victims.size(); i++) {
            calculateSufferedSide(victims.get(i), true);
            calculateGeneratedSide(victims.get(i), true);
            calculateSufferedSide(victims.get(i), false);
            calculateGeneratedSide(victims.get(i), false);


            currentReputation.calculateReputation(sufferedDamageHistory, generatedDamageHistory, true);
            victims.get(i).getReputation().calculateReputation(victims.get(i).getDamageStatus().getSufferedDamageHistory(), victims.get(i).getDamageStatus().getGeneratedDamageHistory(), false);
            currentReputation.atFaultViolation();
        }

        if (type.isDrivable()) {
            if (victims.size() == 1) {
                if (car) {
                    System.out.println("You collided with a car!");
                } else if (bus) {
                    System.out.println("You collided with a bus!");
                } else {
                    System.out.println("You collided with a truck!");
                }
            } else if (victims.size() == 2) {
                if (car && !bus && !truck) {
                    System.out.println("You collided with 2 cars!");
                } else if (!car && bus && !truck) {
                    System.out.println("You collided with 2 buses!");
                } else if (!car && !bus && truck) {
                    System.out.println("You collided with 2 trucks!");
                } else if (car && bus && !truck) {
                    System.out.println("You collided with a car and a bus!");
                } else if (!car && bus && truck) {
                    System.out.println("You collided with a bus and a truck!");
                } else {
                    System.out.println("You collided with a car and a truck!");
                }

            } else {
                System.out.println("You collided with 3 cars!");

            }
        }


        currentReputation.atFaultViolation();
    }


    /**
     * This method calculates the damage a vehicle will endure during a front collision
     *
     * @param victim the collision victim
     */
    private void calculatedSufferedFront(Vehicle victim) {
        double total = 0.0;

        if (type instanceof Truck) {
            if (victim instanceof Car) {
                total += 15;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a car!");
                }

            }


            if (victim instanceof Truck) {
                total += 25;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a truck!");
                }

            }


            if (victim instanceof Bus) {
                total += 20;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a bus!");
                }

            }


            if (type.getSize() > victim.size) {
                total += 15.5;
            } else {
                total += 20.5;
            }


            if (type.getWeight() > victim.getWeight()) {
                total += 8.5;
            } else {
                total += 10.5;
            }

        } else if (type instanceof Bus) {
            if (victim instanceof Car) {
                total += 5;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a car!");
                }

            }

            if (victim instanceof Truck) {
                total += 15;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a truck!");
                }

            }

            if (victim instanceof Bus) {
                total += 10;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a bus!");
                }

            }


            if (type.getSize() > victim.size) {
                total += 15.5;
            } else {
                total += 20.5;
            }


            if (type.getWeight() > victim.getWeight()) {
                total += 8.5;
            } else {
                total += 10.5;
            }

        } else if (type instanceof Car) {

            if (victim instanceof Car) {
                total += 15;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a car!");
                }

            }

            if (victim instanceof Truck) {
                total += 25;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a truck!");
                }


            }

            if (victim instanceof Bus) {
                total += 20;

                if (type.isDrivable()) {
                    System.out.println("You rear ended a bus!");
                }

            }


            if (type.getSize() > victim.size) {
                total += 15.5;
            } else {
                total += 20.5;
            }


            if (type.getWeight() > victim.getWeight()) {
                total += 8.5;
            } else {
                total += 10.5;
            }
        }


        updateStatus(total);

    }


    /**
     * This method calculates the damage generated by the vehicle at fault during a front collision
     * @param victim the collision victim
     */
    private void calculateGeneratedFront(Vehicle victim) {
        double total = 0.0;


        if (victim instanceof Truck) {

            if (type instanceof Car) {
                total += 5;
            }

            if (type instanceof Truck) {
                total += 10;
            }

            if (type instanceof Bus) {
                total += 8;
            }


            if (type.getSize() > victim.getSize()) {
                total += 15.5;
            } else {
                total += 10.5;
            }

            if (type.getWeight() > victim.getWeight()) {
                total += 25.5;
            } else {
                total += 20.5;
            }


        } else if (victim instanceof Bus) {

            if (type instanceof Car) {
                total += 5;
            }

            if (type instanceof Truck) {
                total += 10;
            }

            if (type instanceof Bus) {
                total += 8;
            }

            if (type.getSize() > victim.getSize()) {
                total += 20.5;
            } else {
                total += 15.5;
            }

            if (type.getWeight() > victim.getWeight()) {
                total += 30.5;
            } else {
                total += 20.5;
            }

            total += 20;

        } else if (victim instanceof Car) {

            if (type instanceof Car) {
                total += 5;
            }

            if (type instanceof Truck) {
                total += 10;
            }

            if (type instanceof Bus) {
                total += 8;
            }

            if (type.getSize() > victim.getSize()) {
                total += 20.5;
            } else {
                total += 15.5;
            }

            if (type.getWeight() > victim.getWeight()) {
                total += 25.5;
            } else {
                total += 20.5;
            }

            total += 10;
        }

        generatedDamageHistory.add(total);


    }


    /**
     * This method calculates the amount of damage the collision victim will occur during a front collision
     * @param victim the collision victim
     */
    private void calculatedSufferedBack(Vehicle victim) {
        double total = 0.0;

        if (type instanceof Truck) {
            if (victim instanceof Car) {
                total += 20;
            }

            if (victim instanceof Truck) {
                total += 10;
            }

            if (victim instanceof Bus) {
                total += 15;
            }
        } else if (type instanceof Bus) {
            if (victim instanceof Car) {
                total += 20;
            }

            if (victim instanceof Truck) {
                total += 10;
            }

            if (victim instanceof Bus) {
                total += 15;
            }

        } else if (type instanceof Car) {
            if (victim instanceof Car) {
                total += 20;
            }

            if (victim instanceof Truck) {
                total += 10;
            }

            if (victim instanceof Bus) {
                total += 15;
            }

        }


        if (victim.getSize() > type.getSize()) {
            total += 7.5;
        } else {
            total += 10.5;
        }

        if (victim.getWeight() > type.getWeight()) {
            total += 15.5;
        } else {
            total += 20.5;
        }


        victim.getDamageStatus().updateStatus(total);


    }


    /**
     * This method calculates the damage generated by the collision victim during a front collision
     * @param victim the collision victim
     */
    private void calculateGeneratedBack(Vehicle victim) {
        double total = 0.0;

        victim.getDamageStatus().getGeneratedDamageHistory().add(total);

    }


    /**
     * This method calculates the damage a vehicle will suffer during a side collision
     *
     * @param victim the collision victims
     * @param isAtFault true if the type is at fault
     */
    private void calculateSufferedSide(Vehicle victim, boolean isAtFault) {

        double total = 0.0;

        if (isAtFault) {
            if (type instanceof Truck) {
                if (type.getSize() < victim.getSize()) {
                    total += 20.5;
                } else {
                    total += 15.5;
                }

            } else if (type instanceof Bus) {
                if (type.getSize() < victim.getSize()) {
                    total += 25.0;
                } else {
                    total += 15.5;
                }


            } else if (type instanceof Car) {
                if (type.getSize() < victim.getSize()) {
                    total += 40.5;
                } else {
                    total += 10.5;
                }
            }

            updateStatus(total);

        } else {
            if (victim instanceof Truck) {
                if (victim.getSize() < type.getSize()) {
                    total += 25.5;
                } else {
                    total += 15.5;
                }


            } else if (victim instanceof Bus) {
                if (victim.getSize() < type.getSize()) {
                    total += 20.0;
                } else {
                    total += 15.5;
                }


            } else if (victim instanceof Car) {
                if (victim.getSize() < type.getSize()) {
                    total += 40.5;
                } else {
                    total += 25.5;
                }


            }

            victim.getDamageStatus().updateStatus(total);
        }
    }


    /**
     * This method will calculate the amount of damage a vehicle will generate during a side collision
     * @param victim the collision victims
     * @param isAtFault true if the type is at fault
     */
    private void calculateGeneratedSide(Vehicle victim, boolean isAtFault) {
        double total = 0.0;


        if (isAtFault) {
            if (type instanceof Truck) {
                if (type.getSize() > victim.getSize()) {
                    total += 40.5;
                } else {
                    total += 20.5;
                }
            } else if (type instanceof Bus) {
                if (type.getSize() > victim.getSize()) {
                    total += 35.5;
                } else {
                    total += 15.5;
                }

            } else if (type instanceof Car) {
                total += 40.5;


            }

            generatedDamageHistory.add(total);

        } else {
            victim.getDamageStatus().getGeneratedDamageHistory().add(0.0);

        }

    }


}
