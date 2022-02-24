import java.awt.*;
import java.util.ArrayList;


public class Intersection {

   private int ID;
    private ArrayList<Segment> roads = new ArrayList<>();


    Intersection(int i) {
        this.ID = i;
    }


    private void getSegments(Map m) {

        for (int i = 0; i < m.getMap().get(ID).size() ; i++) {
            roads.add(m.getMap().get(ID).get(i));
        }
    }


    public boolean canTurn(Map m, Segment s ) {

        if (!s.oneWay(m, s.getLocation())){
            return true;
        }

    return false;
    }




}
