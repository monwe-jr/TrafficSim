import java.util.ArrayList;

public class DamageStatus {

    private boolean destroyed;
    private double currentStatus = 100.0;
    private ArrayList<Double> sufferedDamageHistory;
    private ArrayList<Double> generatedDamageHistory;

    DamageStatus() {

    }


    public double getCurrentDamageStatus() {
        return currentStatus;
    }


    public void updateStatus(double d) {
        if(currentStatus - d > 0) {
            currentStatus -= d;
            sufferedDamageHistory.add(currentStatus);
        }else{
            destroyed = true;
        }

    }


    public boolean isDestroyed(){
        if (destroyed){
            return true;
        }else{
            return false;
        }

    }





    public void calculatedSuffered(Vehicle thisV, Vehicle otherV){
        double total = 0.0;

         if(thisV.getSize() < otherV.getSize()){
             total += 10.5;
         }else{
             total += 5.5;
         }

         if(thisV.getWeight() < otherV.getWeight()){
             total+= 30.5;
         }else{
             total += 15.5;
         }

         if(thisV.getMaxSpeed() < otherV.getMaxSpeed()){
             total += 10.5;
        }else{
             total += 25.5;

         }

         updateStatus(total);


    }




    public void calculateGenerated(Vehicle thisV, Vehicle otherV){
        double total = 0.0;

        if(otherV.getSize() < thisV.getSize()){
            total += 5.5;
        }else{
            total += 10.5;
        }

        if(otherV.getWeight() < thisV.getWeight()){
            total += 15.5;
        }else{
            total += 30.5;
        }

        if(otherV.getMaxSpeed() < thisV.getMaxSpeed()){
           total += 25.5;
        }else{
            total += 10.5;

        }

        generatedDamageHistory.add(total);
    }







 }
