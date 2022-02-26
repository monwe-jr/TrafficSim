import java.awt.*;
import java.util.ArrayList;


public class Intersection {

   private int ID;
   private Map map;
    private ArrayList<Segment> roads = new ArrayList<>();


    Intersection(Map m, int i) {
        this.ID = i;
        this.map = m;
        getSegments(m);

    }


    private void getSegments(Map m) {

        for (int i = 0; i < m.getMap().get(ID).size() ; i++) {
            roads.add(m.getMap().get(ID).get(i));
        }
    }





    private boolean containsSegment(int ID, Point segment){
        ArrayList<Segment> roads = map.getMap().get(ID);

        for (int i = 0; i < roads.size(); i++) {
            if(roads.get(i).getSegmentLocation().equals(segment) ){
                return true;
            }
        }

        return false;
    }



    public boolean isDeadEnd(){

        if (roads.size() == 1){
          Point p = roads.get(0).getSegmentLocation();

            if(containsSegment(p.y , new Point(p.y,p.x))){
                return true;
            }

        }

        return false;

    }




}
