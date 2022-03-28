import java.util.ArrayList;

public class Reputation {
    final private double MIN = 0;
    final private double MAX = 100;
    private double niceness;
    private ArrayList<Double> reputationHistory;
    private int repCounter = 0;

    /**
     * Default constructor, starts with the minimum value for niceness
     */
    Reputation() {
        niceness = MIN;
        reputationHistory = new ArrayList();
    }

    /**
     * Returns the current niceness value as a double
     * @return current niceness value
     */
    double getCurrentNiceness() {
        return niceness;
    }

    /**
     * Change the niceness value by a specified amount, if final values falls outside of range from MIN to MAX then
     * it will be adjusted accordingly.
     * @param amount the signed amount you want to change by
     */
    void changeNiceness(double amount) {
        if(niceness + amount < MIN) {
            niceness = MIN;
            reputationHistory.add(niceness);
        } else if(niceness + amount > MAX) {
            niceness = MAX;
            reputationHistory.add(niceness);
        } else {
            niceness = niceness + amount;
            reputationHistory.add(niceness);
        }
    }


    /**
     * If a player does not make a turn or go straight using the corresponding lane, this method iis called
     */
    public void correspondingLaneViolation(){
        changeNiceness(-10.0);
    }


    public void successfulGamble(){
        changeNiceness(25.0);
    }


 public void calculateReputation(ArrayList<Double> suffered,ArrayList<Double> generated){
        if(repCounter == 0){
            for (int i = 0; i < suffered.size(); i++) {
                if(suffered.get(i) < generated.get(i)){
                   changeNiceness(-20);
                }
            }

        }else {

            for (int i = 0; i < suffered.size(); i++) {
                if(i>repCounter){
                    if(suffered.get(i) < generated.get(i)){
                        changeNiceness(-20);
                    }
                }
            }

        }
     repCounter = suffered.size()-1;

 }




}
