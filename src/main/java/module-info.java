module org.sture.java.budgeting {
    requires javafx.controls;
    requires javafx.fxml;

    exports org.sture.java.budgeting;
    exports org.sture.java.budgeting.di;
    exports org.sture.java.budgeting.exceptions.di;
    exports org.sture.java.budgeting.controller.statusbar;

    opens org.sture.java.budgeting to javafx.fxml;
    opens org.sture.java.budgeting.services.tracking.models to javafx.base;
    opens org.sture.java.budgeting.controller.statusbar to javafx.fxml;
}