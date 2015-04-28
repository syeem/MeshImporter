/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package to.struc.testlargestl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author SyeemMorshed
 */
public class MeshAnalyser {

    TriangleMesh triangleMesh;
    float[] meshPointsArray;

    public MeshAnalyser(TriangleMesh mesh) {
        triangleMesh = mesh;
        meshPointsArray = mesh.getPoints().toArray(null);
        GetAdjacentTriangles();
        //System.out.println(mesh.getFaces());
        //System.out.println(mesh.getPoints());

    }

    private List<int[]> GetPointIndexedFaces() {
        int[] meshFaceArray = triangleMesh.getFaces().toArray(null);
        List<int[]> pointIndexFaces = new ArrayList();
        for (int index = 0; index < meshFaceArray.length; index += triangleMesh.getFaceElementSize()) {
            int[] points = new int[3];
            int indexToPointArray = meshFaceArray[index] * triangleMesh.getPointElementSize();
            points[0] = indexToPointArray;

            indexToPointArray = meshFaceArray[index + 2] * triangleMesh.getPointElementSize();
            points[1] = indexToPointArray;

            indexToPointArray = meshFaceArray[index + 4] * triangleMesh.getPointElementSize();
            points[2] = indexToPointArray;
            pointIndexFaces.add(points);
        }
        System.out.println("PointIndexFaces Size:" + pointIndexFaces.size());
        return pointIndexFaces;
    }

    private Map<Integer, List<Integer>> GetAdjacentTriangles() {
        int[] meshFaceArray = triangleMesh.getFaces().toArray(null);
        List<int[]> vertexIndicesOfTriangles = GetPointIndexedFaces();
        Map<Integer, List<Integer>> AdjacentTriangleMap = new HashMap<>();

        Map<Integer, List<Edge>> TriangleEdges = new HashMap<>();
        EdgeMap edgeMap = new EdgeMap();
        Map<Long, List<Integer>> EdgeTriangles = new HashMap<>();

        for (int i = 0; i < vertexIndicesOfTriangles.size(); i++) {
            int[] arr = vertexIndicesOfTriangles.get(i);
//            if (arr.length != 3) {
//                System.out.println("Triangle does not have 3 vertex");
//            }
        }

        for (int i = 0; i < vertexIndicesOfTriangles.size(); i++) {
            List<Edge> edgeList = Edge.GetEdgesFromTriangle(vertexIndicesOfTriangles.get(i));

            TriangleEdges.put(i, edgeList);

            for (Edge e : edgeList) {
                edgeMap.Add(e, i);
            }
        }
        EdgeTriangles = edgeMap.GetMap();

        for (Integer i : TriangleEdges.keySet()) {
            List<Edge> edges = TriangleEdges.get(i);
            List<Integer> adjacentTriangles = new ArrayList<>();
            for (Edge e : edges) {
                List<Integer> trianglesSharingThisEdge = EdgeTriangles.get(e.GetId());
                if (trianglesSharingThisEdge.size() != 2) {
                    System.out.println("More/less than 2 triangles sharing this edge" + trianglesSharingThisEdge.size());
                }
                for (Integer j : trianglesSharingThisEdge) {
                    if (j.equals(i) == false && adjacentTriangles.contains(j) == false) {
                        adjacentTriangles.add(j);
                    }
                }
            }
            AdjacentTriangleMap.put(i, adjacentTriangles);
        }

        float[] a = triangleMesh.getPoints().toArray(null);
//
//        for (Integer i : tri) {
//            System.out.println(i);
//        }

        return AdjacentTriangleMap;
    }
}
