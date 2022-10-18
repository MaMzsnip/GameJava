module fr.mamz.launcher {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens fr.mamz.launcher to javafx.fxml;
    exports fr.mamz.launcher;
}