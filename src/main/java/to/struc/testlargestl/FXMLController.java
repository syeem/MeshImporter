package to.struc.testlargestl;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;

public class FXMLController {

    @FXML
    private Group meshGroup;

    @FXML
    private void handleButtonAction(ActionEvent event) throws InvalidFormatException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Supported Files", "*.stl"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            StlMeshImporter meshImporter = new StlMeshImporter();
            meshImporter.read(selectedFile);
            TriangleMesh mesh = meshImporter.getImport();

            try {
                STLFileReader fileReader = new STLFileReader(selectedFile);
                int[] numberOfFacets = fileReader.getNumOfFacets();
                System.out.println("Number of Facets: " + numberOfFacets[0]);

                VertexMap vertexMap = new VertexMap();
                FacetMap facetMap = new FacetMap();

                for (int i = 0; i < numberOfFacets[0]; i++) {
                    double[] normal = new double[3];
                    double[][] vertices = new double[3][3];

                    fileReader.getNextFacet(normal, vertices);

                    List<Integer> vertexIndicesOfFacet = new ArrayList<>();

                    for (int j = 0; j < 3; j++) {
                        Point3D p = new Point3D(vertices[j][0], vertices[j][1], vertices[j][2]);
                        int index = vertexMap.Add(p);
                        if (index == -1) {
                            System.out.println("Added point could not be found");
                        }
                        vertexIndicesOfFacet.add(index);
                        if (vertexIndicesOfFacet.size() == 3) {
                            if (vertexIndicesOfFacet.get(0).equals(vertexIndicesOfFacet.get(1)) || vertexIndicesOfFacet.get(0).equals(vertexIndicesOfFacet.get(2)) || vertexIndicesOfFacet.get(1).equals(vertexIndicesOfFacet.get(2))) {
                                System.out.println("Triangle has common vertex " + i);
                                System.out.println(vertexIndicesOfFacet.get(0) + " " + vertexIndicesOfFacet.get(1) + " " + vertexIndicesOfFacet.get(2));
                            }
                        }
                    }

                    if (vertexIndicesOfFacet.size() != 3) {
                        System.out.println("Number of triangle vertex is equal to " + vertexIndicesOfFacet.size());
                    }

                    facetMap.AddFacet(vertexIndicesOfFacet);
                }
                mesh = CreateTriangleMesh(vertexMap, facetMap);
            } catch (IOException ex) {
                System.out.println("EXCEPTION loading file");
            }

            MeshAnalyser m = new MeshAnalyser(mesh);
            meshImporter.close();

            Group sceneRoot = GetSceneRoot(meshGroup);
            AddToScene(mesh, sceneRoot);

        }

    }

    public TriangleMesh CreateTriangleMesh(VertexMap vertexMap, FacetMap facetMap) {
        TriangleMesh triangleMesh = new TriangleMesh();

        int facetCount = facetMap.facetCount;
        HashMap<Integer, List<Integer>> map = facetMap.facetMap;
        double[] points = vertexMap.GetPointsArray();

        for (int i = 0; i < points.length; i = i + 3) {
            triangleMesh.getPoints().addAll((float) points[i], -(float) points[i + 1], -(float) points[i + 2]);
        }

        triangleMesh.getTexCoords().addAll(0, 0);

        for (int i = 1; i <= facetCount; i++) {
            List<Integer> vertexIndices = map.get(i);
            triangleMesh.getFaces().addAll(vertexIndices.get(0), 0, vertexIndices.get(1), 0, vertexIndices.get(2), 0);
        }

        triangleMesh.getFaceSmoothingGroups().addAll(
                0, 0, 1, 1, 2, 2, 3, 4
        );
        return triangleMesh;
    }

    private Point3D CalculateSurfaceNormal(double[][] points) {
        //http://math.stackexchange.com/questions/305642/how-to-find-surface-normal-of-a-triangle
        Point3D p1 = new Point3D(points[0][0], points[0][1], points[0][2]);
        Point3D p2 = new Point3D(points[1][0], points[1][1], points[1][2]);
        Point3D p3 = new Point3D(points[2][0], points[2][1], points[2][2]);

        Point3D v = p2.subtract(p1);
        Point3D w = p3.subtract(p1);

        Point3D normal = v.crossProduct(w);
        return normal.normalize();

//                    Point3D n = CalculateSurfaceNormal(vertices);
//
//                    if ( (int)n.getX() * 1000 != (int)normal[0] * 1000
//                            || (int)n.getY() * 1000 != (int)normal[1] * 1000
//                            || (int)n.getZ() * 1000 != (int)normal[2] * 1000) {
//                        System.out.println("Normals didnot match " + n.getX() + " " + normal[0]);
//                        System.out.println( (int)n.getY()*1000 + " " + (int)normal[1]*1000);
//                        System.out.println( (int)n.getZ()*1000 + " " + (int)normal[2]*1000);
//                    }
    }

    private Group GetSceneRoot(Group meshGroup) {

        SubScene subScene = (SubScene) meshGroup.getChildren().get(0);
        Group sceneRoot = (Group) subScene.getRoot();
        return sceneRoot;
    }

    private void AddToScene(TriangleMesh mesh, Group sceneRoot) {

        // Render mesh with flat shading effect by setting smoothing groups
        int[] faceSmoothingArray = new int[mesh.getFaceSmoothingGroups().size()];
        Arrays.fill(faceSmoothingArray, 0);
        mesh.getFaceSmoothingGroups().clear();
        //mesh.getFaceSmoothingGroups().addAll(faceSmoothingArray);

        MeshView meshView = new MeshView();
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.rgb(247, 148, 29));
        material.setSpecularColor(Color.rgb(247, 148, 29));
        meshView.setMaterial(material);

        meshView.setMesh(mesh);
        Bounds boundingBoxLocal = meshView.getBoundsInLocal();

        Point3D translationToCenter = TranslateToGroundOrigin(boundingBoxLocal);
        meshView.getTransforms().addAll(
                new Translate(translationToCenter.getX(), translationToCenter.getY(), translationToCenter.getZ())
        );

        meshView.setId(UUID.randomUUID().toString());
        sceneRoot.getChildren().add(meshView);
    }

    private Point3D TranslateToGroundOrigin(Bounds boundingBoxLocal) {

        // Translation to origin
        double moveX = -(boundingBoxLocal.getMinX() + (boundingBoxLocal.getWidth() / 2));
        double moveY = -(boundingBoxLocal.getMinY() + (boundingBoxLocal.getHeight() / 2));
        double moveZ = -(boundingBoxLocal.getMinZ() + (boundingBoxLocal.getDepth() / 2));

        // Ground object
        moveZ -= boundingBoxLocal.getDepth() / 2;

        return new Point3D(moveX, moveY, moveZ);
    }

}
