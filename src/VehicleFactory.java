import java.awt.*;

public class VehicleFactory {
    public enum vehicleType{
        Car, Truck, Bus
    }

    public Vehicle getVehicle(vehicleType v, Color c, Boolean drivable) {
        switch (v) {
            case Car:
                return new Car(c, drivable);
            case Bus:
                return new Bus(c, drivable);
            case Truck:
                return new Truck(c, drivable);
            default:
                System.err.println("Invalid vehicleType");
                return null;
        }
    }

    public Vehicle getVehicle(String input, Color c, Boolean drivable) {
        vehicleType v = parseVehicle(input);
        return getVehicle(v, c, drivable);
    }

    public vehicleType parseVehicle(String input) {
        if (input == null) {
            return null;
        } else if (input.equalsIgnoreCase("CAR")) {
            return vehicleType.Car;
        } else if (input.equalsIgnoreCase("BUS")) {
            return vehicleType.Bus;
        } else if (input.equalsIgnoreCase("TRUCK")) {
            return vehicleType.Truck;
        } else {
            return null;
        }
    }
}
