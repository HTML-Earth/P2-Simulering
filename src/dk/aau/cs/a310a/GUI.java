package dk.aau.cs.a310a;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GUI extends Application {

    public void start(Stage stage) {
        stage.setTitle("WindowTest");

        HBox root = new HBox();

        Scene scene = new Scene(root, 800, 400);
        stage.setScene(scene);

        stage.show();

    }

}
