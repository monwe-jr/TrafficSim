import java.util.ArrayList;

public class DamageStatus {

    private boolean destroyed;
    private double currentStatus = 100.0;
    private ArrayList<Double> sufferedDamageHistory = new ArrayList<>();
    private ArrayList<Double> generatedDamageHistory = new ArrayList<>();
    private Reputation currentReputation;
    private Vehicle type;


    DamageStatus(Reputation r, Vehicle v) {
        this.currentReputation = r;
        this.type = v;
    }


    public ArrayList<Double> getGeneratedDamageHistory() {
        return generatedDamageHistory;
    }

    public ArrayList<Double> getSufferedDamageHistory() {
        return sufferedDamageHistory;
    }

    public void updateStatus(double d) {
        if (currentStatus - d > 0) {
            currentStatus -= d;
            sufferedDamageHistory.add(currentStatus);
        } else {
            destroyed = true;
            type.getSegment().removeVehicle(type);
            type.removeSegment();
            type.setVehicleLocation(null);

            System.out.println(type + " Has been destroyed!");
        }

        System.out.println(type + " health is " + currentStatus);

    }




    public boolean isDestroyed() {
        if (destroyed) {
            return true;
        } else {
            return false;
        }

    }


    public void frontCollision( Vehicle victim) {
        calculatedSufferedFront(victim,true);
        calculateGeneratedFront(victim,true);
        calculatedSufferedBack(victim,false);
        calculateGeneratedBack(victim,false);

        currentReputation.calculateReputation(sufferedDamageHistory,generatedDamageHistory);
        victim.getReputation().calculateReputation(victim.getDamageStatus().getSufferedDamageHistory(), victim.getDamageStatus().getGeneratedDamageHistory());

    }


    public void sideCollision(ArrayList<Vehicle> victims) {

            for (int i = 0; i < victims.size(); i++) {
                calculateSufferedSide(victims.get(i),true);
                calculateGeneratedSide(victims.get(i),true);
                calculateSufferedSide(victims.get(i),false);
                calculateGeneratedSide(victims.get(i),false);

                currentReputation.calculateReputation(sufferedDamageHistory,generatedDamageHistory);
                victims.get(i).getReputation().calculateReputation(victims.get(i).getDamageStatus().getSufferedDamageHistory(), victims.get(i).getDamageStatus().getGeneratedDamageHistory());
            }

    }






    private void calculatedSufferedFront(Vehicle victim, boolean isAtFault) {
        double total = 0.0;

        if (isAtFault) {
            if (type instanceof Car) {
                total += 30;
            }

            if (type instanceof Truck) {
                total += 20;
            }

            if (type instanceof Bus) {
                total += 15;
            }


            if (type.getWeight() < victim.getWeight()) {
                total += 10.5;
            } else {
                total += 5.5;
            }

            if (type.getMaxSpeed() < victim.getMaxSpeed()) {
                total += 10.5;
            } else {
                total += 25.5;

            }

            updateStatus(total);

        } else {
            if (victim instanceof Car) {
                total += 30;
            }

            if (victim instanceof Truck) {
                total += 20;
            }

            if (victim instanceof Bus) {
                total += 15;
            }


            if (victim.getWeight() < type.getWeight()) {
                total += 10.5;
            } else {
                total += 5.5;
            }

            if (victim.getMaxSpeed() < type.getMaxSpeed()) {
                total += 10.5;
            } else {
                total += 25.5;

            }

            victim.getDamageStatus().updateStatus(total);

        }

    }


    private void calculateGeneratedFront( Vehicle victim, boolean isAtFault) {
        double total = 0.0;

        if (isAtFault) {
            if(type instanceof Truck){
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

                if (type.getMaxSpeed() > victim.getMaxSpeed()) {
                    total += 35.5;
                } else {
                    total += 25.5;
                }
            }else if(type instanceof Bus){
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

                if (type.getMaxSpeed() > victim.getMaxSpeed()) {
                    total += 30.5;
                } else {
                    total += 15.5;
                }

            } else if(type instanceof Car) {
                if (type.getSize() > victim.getSize()) {
                    total += 10.5;
                } else {
                    total += 5.5;
                }

                if (type.getWeight() > victim.getWeight()) {
                    total += 15.5;
                } else {
                    total += 10.5;
                }

                if (type.getMaxSpeed() > victim.getMaxSpeed()) {
                    total += 20.5;
                } else {
                    total += 15.5;
                }
            }

            generatedDamageHistory.add(total);

        } else {
            if(victim instanceof Truck){
                if (victim.getSize() > type.getSize()) {
                    total += 20.5;
                } else {
                    total += 15.5;
                }

                if (victim.getWeight() > type.getWeight()) {
                    total += 30.5;
                } else {
                    total += 20.5;
                }

                if (victim.getMaxSpeed() > type.getMaxSpeed()) {
                    total += 35.5;
                } else {
                    total += 25.5;
                }
            }else if(victim instanceof Bus){
                if (victim.getSize() > type.getSize()) {
                    total += 15.5;
                } else {
                    total += 10.5;
                }

                if (victim.getWeight() > type.getWeight()) {
                    total += 25.5;
                } else {
                    total += 20.5;
                }

                if (victim.getMaxSpeed() > type.getMaxSpeed()) {
                    total += 30.5;
                } else {
                    total += 15.5;
                }

            } else if(victim instanceof Car) {
                if (victim.getSize() > type.getSize()) {
                    total += 10.5;
                } else {
                    total += 5.5;
                }

                if (victim.getWeight() > type.getWeight()) {
                    total += 15.5;
                } else {
                    total += 10.5;
                }

                if (victim.getMaxSpeed() > type.getMaxSpeed()) {
                    total += 20.5;
                } else {
                    total += 15.5;
                }
            }

            victim.getDamageStatus().getGeneratedDamageHistory().add(total);
        }
    }



    private void calculatedSufferedBack( Vehicle victim, boolean isAtFault) {
        double total = 0.0;

        if (isAtFault) {
            if (type instanceof Car) {
                total += 5;
            }

            if (type instanceof Truck) {
                total += 5;
            }

            if (type instanceof Bus) {
                total += 30;
            }

            if (type.getSize() < victim.getSize()) {
                total += 10.5;
            } else {
                total += 5.5;
            }

            if (type.getWeight() < victim.getWeight()) {
                total += 20.5;
            } else {
                total += 15.5;
            }

            if (type.getMaxSpeed() < victim.getMaxSpeed()) {
                total += 25.5;
            } else {
                total += 5.5;

            }

            updateStatus(total);

        } else {
            if (victim instanceof Car) {
                total += 5;
            }

            if (victim instanceof Truck) {
                total += 5;
            }

            if (victim instanceof Bus) {
                total += 30;
            }


            if (victim.getSize() < type.getSize()) {
                total += 10.5;
            } else {
                total += 5.5;
            }

            if (victim.getWeight() < type.getWeight()) {
                total += 20.5;
            } else {
                total += 15.5;
            }

            if (victim.getMaxSpeed() < type.getMaxSpeed()) {
                total += 25.5;
            } else {
                total += 5.5;

            }

            victim.getDamageStatus().updateStatus(total);

        }


    }


    private void calculateGeneratedBack(Vehicle victim, boolean isAtFault) {
        double total = 0.0;

        if (isAtFault) {

            if(type instanceof Truck){
                if (type.getSize() > victim.getSize()) {
                    total += 20.5;
                } else {
                    total += 15.5;
                }

                if (type.getWeight() > victim.getWeight()) {
                    total += 35.5;
                } else {
                    total += 25.5;
                }

                if (type.getMaxSpeed() > victim.getMaxSpeed()) {
                    total += 40.5;
                } else {
                    total += 30.5;

                }
            } else if(type instanceof Bus){
                if (type.getSize() > victim.getSize()) {
                    total += 10.5;
                } else {
                    total += 5.5;
                }

                if (type.getWeight() > victim.getWeight()) {
                    total += 30.5;
                } else {
                    total += 15.5;
                }

                if (type.getMaxSpeed() > victim.getMaxSpeed()) {
                    total += 35.5;
                } else {
                    total += 25.5;

                }
            } else if(type instanceof Car) {
                if (type.getSize() > victim.getSize()) {
                    total += 10.5;
                } else {
                    total += 5.5;
                }

                if (type.getWeight() > victim.getWeight()) {
                    total += 20.5;
                } else {
                    total += 10.5;
                }

                if (type.getMaxSpeed() > victim.getMaxSpeed()) {
                    total += 25.5;
                } else {
                    total += 15.5;

                }
            }

            generatedDamageHistory.add(total);

        } else {
            victim.getDamageStatus().getGeneratedDamageHistory().add(total);
        }
    }


    private void calculateSufferedSide(Vehicle victim, boolean isAtFault) {
        double total = 0.0;

        if (isAtFault) {
            if( type instanceof Truck){
                if (type.getSize() < victim.getSize()) {
                    total += 20.5;
                } else {
                    total += 15.5;
                }

            } else if(type instanceof Bus){
                if (type.getSize() < victim.getSize()) {
                    total += 25.0;
                } else {
                    total += 15.5;
                }


            } else if(type instanceof  Car) {
                if (type.getSize() < victim.getSize()) {
                    total += 40.5;
                } else {
                    total += 10.5;
                }
            }

           updateStatus(total);

        } else {
            if( victim instanceof Truck){
                if (victim.getSize() < type.getSize()) {
                    total += 25.5;
                } else {
                    total += 15.5;
                }


            } else if(victim instanceof Bus){
                if (victim.getSize() < type.getSize()) {
                    total += 20.0;
                } else {
                    total += 15.5;
                }


            } else if(victim instanceof Car) {
                if (victim.getSize() < type.getSize()) {
                    total += 40.5;
                } else {
                    total += 25.5;
                }



            }

            victim.getDamageStatus().updateStatus(total);
        }
    }


    private void calculateGeneratedSide( Vehicle victim, boolean isAtFault) {
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
