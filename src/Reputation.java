import java.util.ArrayList;

public class Reputation {
    final double MIN = 0;
    final double MAX = 100;
    double niceness;
    ArrayList<Double> reputationHistory;

    Reputation() {
        niceness = MIN;
        reputationHistory = new ArrayList();
    }
}
