/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package to.struc.testlargestl;

import java.util.HashMap;
import java.util.Set;
import javafx.geometry.Point3D;

/**
 *
 * @author SyeemMorshed
 */
public class VertexMap {

    HashMap<Point3D, Integer> vertexMap;
    int mapSize;

    public VertexMap() {

        vertexMap = new HashMap<>();
        mapSize = 0;
    }

    public int Add(Point3D v) {
        if (vertexMap.containsKey(v) == false) {
            vertexMap.put(v, mapSize);
            mapSize = mapSize + 1;

            //return the index
            return mapSize - 1;
        } else {
//            if (vertexMap.get(v) == 16524) {
//                System.out.println();
//            }
            return vertexMap.get(v);
        }
    }

    public int GetVertexIndex(Point3D pt) {
        if (vertexMap.containsKey(pt)) {
            return vertexMap.get(pt);
        } else {
            System.out.println("Point not found in map");
            return -1;
        }
    }

    public double[] GetPointsArray() {
        int arraySize = mapSize * 3;
        double[] points = new double[arraySize];
        Set keySet = vertexMap.keySet();

        for (Object obj : keySet) {
            Point3D p = (Point3D) obj;
            int index = vertexMap.get(p) * 3;
            points[index] = p.getX();
            points[index + 1] = p.getY();
            points[index + 2] = p.getZ();
        }
        return points;
    }

}
