package tools;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Created by User on 02/02/2018.
 */
public class Logout {
    public void logout(Button btnOut) {
        Stage stage = (Stage) btnOut.getScene().getWindow();

        PauseTransition delay8 = new PauseTransition(Duration.millis(500));
        delay8.setOnFinished( event -> stage.close() );
        delay8.play();


        //open login view
        PauseTransition delay9 = new PauseTransition(Duration.millis(500));
        delay9.setOnFinished( event -> {
            Stage primaryStage = new Stage();
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/login.fxml"));
                primaryStage.setTitle("Login Panel");
                primaryStage.setScene(new Scene(root, 900, 600));
            } catch (IOException e) {
                e.printStackTrace();
            }

            primaryStage.show();
        } );
        delay9.play();
    }
}
