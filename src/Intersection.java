import java.awt.*;
import java.util.ArrayList;

public class Intersection {




    static private boolean containsSegment(Map m, int ID, Point segment) {
        ArrayList<Segment> roads = m.getMap().get(ID);

        for (int i = 0; i < roads.size(); i++) {
            if (roads.get(i).getSegmentLocation().equals(segment)) {
                return true;
            }
        }

        return false;
    }


    static public boolean isDeadEnd(Map m, int ID) {
        ArrayList<Segment> roads = m.getMap().get(ID);

        if (roads.size() == 1) {
            Point p = roads.get(0).getSegmentLocation();

            if (containsSegment(m, p.y, new Point(p.y, p.x))) {
                return true;
            }

        }

        return false;

    }


    /**
     * Returns an Array list of vehicles trying to turn at an intersection. Used to implement gambling
     */
    static public ArrayList<Vehicle> getCompetitors(Map m, int ID, Vehicle v) {
        ArrayList<Vehicle> competitors = new ArrayList<>();

        for (int i = 0; i < m.getMap().size(); i++) {
            for (int j = 0; j < m.getMap().get(i).size(); j++) {
                if (m.getMap().get(i).get(j).getSegmentLocation().y == ID && !m.getMap().get(i).get(j).getSegmentLocation().equals(v.getSegment().getSegmentLocation()) ) {
                    Segment s = m.getMap().get(i).get(j);
                    ArrayList<Vehicle> temp = s.getVehiclesAtEnd();

                    for (int k = 0; k < temp.size(); k++) {
                        competitors.add(temp.get(k));

                    }
                }
            }

        }

        return competitors;

    }




}





