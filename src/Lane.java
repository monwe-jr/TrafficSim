import javax.swing.*;
import java.awt.*;

public class Lane {
    public static int location; //position on road segment. Depends on the number of lanes the road segment has
    private String position;  //position of lane

    //returns true if the user can change to lane l
    public static boolean canChange(int l){
        return false;
    }

    //changes to left lane
    public static void changeLeft(Point p, JPanel panel){
    }

    //changes to right lane
    public static void changeRight(Point p, JPanel panel){

    }



}
