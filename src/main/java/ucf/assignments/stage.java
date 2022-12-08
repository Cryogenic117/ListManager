package ucf.assignments;

import javafx.stage.Stage;

public class stage {
    private static Stage mainStage;
    public static void setStage(Stage givenStage) {
        mainStage = givenStage;
    }
    public static Stage getStage() {
        return mainStage;
    }
}
