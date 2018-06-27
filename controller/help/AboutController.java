package controller.help;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

import static tools.ScreenWidth.screenWidth.allWidth;

/**
 * Created by funday on 09/02/2018.
 */
public class AboutController implements Initializable {
    @FXML
    private AnchorPane anchor;

    @FXML
    private Label lblAlert;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchor.setPrefWidth(allWidth()-20);
    }
}
