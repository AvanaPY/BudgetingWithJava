module org.sture.worlddomination.budgettingiguess {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens org.sture.java.budgeting to javafx.fxml;
    exports org.sture.java.budgeting;
    exports org.sture.java.budgeting.di;
    exports org.sture.java.budgeting.exceptions.di;
    opens org.sture.java.budgeting.services.tracking.models to javafx.base;
}