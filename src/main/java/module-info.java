module com.we.suck.at.java.budgettingiguess {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.we.suck.at.java.budgettingiguess to javafx.fxml;
    opens com.we.suck.at.java.budgettingiguess.models to javafx.base;
    exports com.we.suck.at.java.budgettingiguess;
}