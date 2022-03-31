import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Players extends Vehicle {
    private ArrayList<Vehicle> playerVehicles = new ArrayList<>();
    private int playerCount = 0;


    public Players(Map m) {
        super(m);

    }


    public void prompt() {
        VehicleFactory vF = new VehicleFactory();
        Scanner input = new Scanner(System.in);

        System.out.println("You can operate a car, bus, or truck. What type of vehicle do you want to drive?");
        String type = input.nextLine();
        while (!type.equals("car") && !type.equals("bus") && !type.equals("truck")) {
            System.out.println("Please type 'car' if you want to operate a car, 'bus' if you want to operate a bus, or 'truck' if you want to operate a truck.");
            type = input.nextLine();
        }
        Random random = new Random();

        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        Vehicle v;

        if (type.equals("car")) {
            v = vF.getVehicle(VehicleFactory.vehicleType.Car, new Color(r, g, b), true);
        } else if (type.equals("bus")) {
            v = vF.getVehicle(VehicleFactory.vehicleType.Bus, new Color(r, g, b), true);
        } else {
            v = vF.getVehicle(VehicleFactory.vehicleType.Truck, new Color(r, g, b), true);
        }


        System.out.println();
        System.out.println("You selected " + type + "! ");
        addPlayer(v);
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

    }


    public void addPlayer(Vehicle v) {
        ArrayList<Segment> mapSegments = map.getSegments();
        Segment s = mapSegments.get((int) (Math.random() * mapSegments.size()));
        playerVehicles.add(v);
        playerCount += 1;

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

        s.callListener(map,v);


    }


    public Vehicle getPlayers() {
           return playerVehicles.get(0);
    }


}
