/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package to.struc.testlargestl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author SyeemMorshed
 */
public class EdgeMap {

    HashMap<Long, List<Integer>> edgeTriangles = new HashMap<>();

    public EdgeMap() {
    }

    public void Add(Edge edge, Integer triangleIndex) {
        //creates a new list if the key doesnt exist
        //if it does, adds the triangle to the existing list
        if (edgeTriangles.containsKey(edge.GetId())) {
            List<Integer> triangleList = edgeTriangles.get(edge.GetId());
            if (triangleList.contains(triangleIndex) == false) {
                triangleList.add(triangleIndex);
            }
        } else {
            List<Integer> triangleList = new ArrayList<>();
            triangleList.add(triangleIndex);
            edgeTriangles.put(edge.GetId(), triangleList);
        }
    }

    public HashMap<Long, List<Integer>> GetMap() {
        return edgeTriangles;
    }

}
