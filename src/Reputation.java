import java.util.ArrayList;

public class Reputation {
    final private double MIN = 0;
    final private double MAX = 100;
    private double niceness;
    ArrayList<Double> reputationHistory;

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
        } else if(niceness + amount > MAX) {
            niceness = MAX;
        } else {
            niceness = niceness + amount;
        }
    }
}
