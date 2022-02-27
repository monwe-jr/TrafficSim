import java.awt.*;
import java.util.ArrayList;

public class Intersection {


    static public ArrayList<Segment>  getSegments(Map m, int ID) {
         ArrayList<Segment> temp = new ArrayList<>();

        for (int i = 0; i < m.getMap().get(ID).size() ; i++) {
            temp.add(m.getMap().get(ID).get(i));
        }

        return temp;
    }




    static private boolean containsSegment(Map m, int ID, Point segment){
        ArrayList<Segment> roads =  getSegments(m,ID);

        for (int i = 0; i < roads.size(); i++) {
            if(roads.get(i).getSegmentLocation().equals(segment) ){
                return true;
            }
        }

        return false;
    }



  static  public boolean isDeadEnd(Map m, int ID){
      ArrayList<Segment> roads =  getSegments(m,ID);

        if (roads.size() == 1){
            Point p = roads.get(0).getSegmentLocation();

            if(containsSegment(m, p.y , new Point(p.y,p.x))){
                return true;
            }

        }

        return false;

    }





}
