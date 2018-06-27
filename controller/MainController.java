package controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static controller.LoginController.level;
import static tools.ScreenWidth.screenWidth.allHeight;
import static tools.ScreenWidth.screenWidth.allWidth;

public class MainController implements Initializable {

    @FXML private MenuItem fileTambahUser;
    @FXML private MenuItem fileRating;
    @FXML private MenuItem fileOut;
    @FXML private MenuItem fileClose;
    @FXML private MenuItem editRating;
    @FXML private MenuItem editAspect;
    @FXML private MenuItem editInstrument;
    @FXML private MenuItem editPreference;
    @FXML private MenuItem processAspect;
    @FXML private MenuItem processNorm;
    @FXML private MenuItem processResult;
    @FXML private MenuItem profileEdit;
    @FXML private MenuItem resultView;
    @FXML private MenuItem helpAbout;

    @FXML private TabPane tabPane;

    @FXML private Menu menuFIle;
    @FXML private Menu menuEdit;
    @FXML private Menu menuProfile;
    @FXML private Menu menuHelp;
    @FXML private Menu menuProcess;


    @FXML private AnchorPane mainPane;

    private Map<String, Tab> openTabs = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuClicked();
        setWelcome();
        setAccess();
    }

    private void setAccess(){
        if(level().equals("0")) memberMode();
        else adminMode();
    }

    private void adminMode(){
        menuEdit.setVisible(true);
        menuFIle.setVisible(true);
        menuHelp.setVisible(true);
        menuProfile.setVisible(true);
        menuProcess.setVisible(true);
        fileTambahUser.setVisible(true);
    }
    private void memberMode(){
        menuEdit.setVisible(false);
        menuFIle.setVisible(true);
        menuHelp.setVisible(true);
        menuProfile.setVisible(true);
        menuProcess.setVisible(false);
        fileTambahUser.setVisible(false);
    }

    private void setWelcome(){
        try {
            Tab welcomePane = new Tab();
            welcomePane.setText("Welcome Page");
            welcomePane.setClosable(false);
            Node welcome = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("view/welcome.fxml"));

            ScrollPane sp = new ScrollPane();
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            sp.setContent(welcome);
            sp.setMaxSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);

            welcomePane.setContent(sp);
            tabPane.getTabs().add(welcomePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tabOpener(String fxml, String title){
        if (openTabs.containsKey(fxml)) {
            tabPane.getSelectionModel().select(openTabs.get(fxml));
        } else {
            try {
                //Node nodeAkun = null;
                Node nodeAkun = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource(fxml));

                ScrollPane sp = new ScrollPane();
                //sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                sp.setContent(nodeAkun);
                sp.setMaxSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);

                Tab tb = new Tab(title);
                tb.setClosable(true);
                PauseTransition delay5 = new PauseTransition(Duration.millis(500));
                delay5.setOnFinished( event -> tb.setContent(sp)  );
                delay5.play();

                tabPane.getTabs().add(tb);
                tabPane.getSelectionModel().select(tb);

                openTabs.put(fxml, tb);
                tb.setOnClosed(e -> openTabs.remove(fxml));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void menuClicked(){
        fileTambahUser.setOnAction(event -> {
            tabOpener("view/file/add_user.fxml", "Add New User");
        });
        fileRating.setOnAction(event -> {
            tabOpener("view/file/inputRating.fxml", "Input of Interest Rating");
        });
        fileClose.setOnAction(event -> {
            System.exit(0);
        });

        editRating.setOnAction(event -> {
            tabOpener("view/edit/rating.fxml", "Set Rating");
        });
        editAspect.setOnAction(event -> {
            tabOpener("view/edit/aspect.fxml", "Set Aspect");
        });
        editInstrument.setOnAction(event -> {
            tabOpener("view/edit/instrument.fxml", "Set Instrument");
        });
        editPreference.setOnAction(event -> {
            tabOpener("view/edit/preference_weight.fxml", "Set Preference Weights");
        });

        processAspect.setOnAction(event -> {
            tabOpener("view/process/average_aspect.fxml", "Average Aspect Value");
        });
        processNorm.setOnAction(event -> {
            tabOpener("view/process/normalitation.fxml", "Normalitation Process");
        });
        processResult.setOnAction(event -> {
            tabOpener("view/process/result.fxml", "Result of Evaluation");
        });

        profileEdit.setOnAction(event -> {
            tabOpener("view/profile/edit_profile.fxml", "Edit Profile");
        });

        resultView.setOnAction(event -> {
            tabOpener("view/result/view_result.fxml", "View Result");
        });

        helpAbout.setOnAction(event -> {
            tabOpener("view/help/about.fxml", "About");
        });

    }


}
