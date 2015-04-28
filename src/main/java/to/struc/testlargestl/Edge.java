/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package to.struc.testlargestl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SyeemMorshed
 */
public class Edge {

    public int vertexA = 0;
    public int vertexB = 0;

    private Long edgeId;

    public Edge(int a, int b) {
        vertexA = a;
        vertexB = b;

        //makes sure vertexA is always smaller
        //to ensure there is no difference between 0,1 and 1,0
        if (vertexA > vertexB) {
            int temp = vertexA;
            vertexA = vertexB;
            vertexB = temp;
        }
        edgeId = GenerateUniqueId();
    }

    private Long GenerateUniqueId() {

        //cantor's paring function from http://szudzik.com/ElegantPairing.pdf
        int x = vertexA;
        int y = vertexB;

        long uniqueValue = (long) Math.pow(x, 2) + 3 * x + 2 * x * y + y + (long) Math.pow(y, 2);
        uniqueValue = uniqueValue / 2;

        if (uniqueValue < 0) {
            //negative unique values mean that a long variable is not enough store 
            //the result of the unique value calculation. 
        }

        return uniqueValue;
    }

    public Long GetId() {
        return edgeId;
    }

    public static List<Edge> GetEdgesFromTriangle(int[] vertices) {
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(vertices[0], vertices[1]));
        edges.add(new Edge(vertices[0], vertices[2]));
        edges.add(new Edge(vertices[1], vertices[2]));

        return edges;
    }

    public static boolean IsEqual(Edge a, Edge b) {
        boolean result = false;

        if ((a.vertexA == b.vertexA && a.vertexB == b.vertexB)
                || (a.vertexB == b.vertexA && a.vertexA == b.vertexB)) {
            result = true;
        }

        return result;
    }
}
