/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package to.struc.testlargestl;

import java.util.HashMap;
import java.util.List;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author SyeemMorshed
 */
public class FacetMap {

    HashMap<Integer, List<Integer>> facetMap;
    int facetCount;

    public FacetMap() {
        facetMap = new HashMap<>();
        facetCount = 0;
    }

    public void AddFacet(List<Integer> vertexIndices) {
        facetCount = facetCount + 1;
        facetMap.put(facetCount, vertexIndices);
    }

}
