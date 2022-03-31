import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class XMLManager {
    static Map readFile() {
        Map m;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("Map.xml"));
            doc.getDocumentElement().normalize();
            m = new Map(Integer.parseInt(doc.getDocumentElement().getAttribute("interCount")));
            NodeList nList = doc.getElementsByTagName("Segment");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element)n;
                    m.addSegment(new Segment(
                            new Point(
                                    Integer.parseInt(e.getElementsByTagName("x").item(0).getTextContent()),
                                    Integer.parseInt(e.getElementsByTagName("y").item(0).getTextContent())),
                            Direction.valueOf(e.getElementsByTagName("direction").item(0).getTextContent()),
                            Integer.parseInt(e.getElementsByTagName("laneCount").item(0).getTextContent()),
                            Integer.parseInt(e.getElementsByTagName("segLength").item(0).getTextContent())
                    ));
                }
            }
            return m;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }
}
