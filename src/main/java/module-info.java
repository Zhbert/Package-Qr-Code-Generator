module ru.zhbert {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.zhbert to javafx.fxml;
    exports ru.zhbert;
}
