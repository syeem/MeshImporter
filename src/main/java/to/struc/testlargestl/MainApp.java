package to.struc.testlargestl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        Group sceneRoot = new Group();
        SubScene meshSubScene = new SubScene(sceneRoot, 800, 480, true, SceneAntialiasing.BALANCED);
        meshSubScene.setFill(Color.LIGHTGRAY);
        meshSubScene.setCamera(CreateCamera());
                
        Pane pane = (Pane) root.lookup("#mainPane");
        meshSubScene.heightProperty().bind(pane.heightProperty());
        meshSubScene.widthProperty().bind(pane.widthProperty());
        Group meshGroup = (Group) pane.lookup("#meshGroup");
        meshGroup.getChildren().add(meshSubScene);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("TestLargeSTL");
        stage.setScene(scene);
        stage.show();
    }

    public PerspectiveCamera CreateCamera() {

        PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setFieldOfView(6);
        camera.setFarClip(10000);

        camera.getTransforms().addAll(
                new Scale(3, 3, 3),
                new Rotate(-30, Rotate.Z_AXIS),
                new Rotate(60, Rotate.X_AXIS),
                new Translate(0, 0, -800)
        );

        return camera;
    }
    
}
