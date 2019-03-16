package muhammad.learning;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    
    MainController mainController;
    GenerateChart generateChart = new GenerateChart();
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        FXMLLoader loader = null;
        try {
            /*loader = new FXMLLoader(getClass().getResource("main.fxml"));
            //loader.setController(new MainController());
            root = loader.load();*/
            URL url = new File ("src/main/resources/muhammad/learning/main.fxml").toURI().toURL();
            loader = new FXMLLoader(url);
            root = loader.load();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        primaryStage.setTitle("Excel Reader");
        primaryStage.setScene(scene);
        primaryStage.show();
        mainController = loader.getController();
        mainController.getTableView().getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                /*int index = observableValue.getValue().intValue();
                mainpackage.RowWithSum r = c.getmChartList().get(index);*/
                //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graph_dialog.fxml"));
                /*
                 * if "fx:controller" is not set in fxml
                 * fxmlLoader.setController(NewWindowController);
                 */
                try {
                    //fxmlLoader.setController(generateChart);
                    /*Parent dialog = fxmlLoader.load();
                    Scene dialogScene = new Scene(dialog);*/
                    generateChart.closeStage();
                    generateChart.setClickedRow(observableValue.getValue().intValue());
                    generateChart.setMainController(mainController);
                    generateChart.displayChart();
                   /* Stage stage = new Stage();
                    stage.setScene(dialogScene);
                    stage.show();*/
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
    }
}
