module org.sture.java.budgeting {
    requires javafx.controls;
    requires javafx.fxml;

    exports org.sture.java.budgeting;
    exports org.sture.java.budgeting.di;
    exports org.sture.java.budgeting.exceptions.di;
    exports org.sture.java.budgeting.controller.statusbar;

    exports org.sture.java.budgeting.controller.settings.export;
    exports org.sture.java.budgeting.controller.statusbar.export;
    exports org.sture.java.budgeting.controller.home.export;
    exports org.sture.java.budgeting.controller.iface;

    opens org.sture.java.budgeting to javafx.fxml;
    opens org.sture.java.budgeting.controller.settings.export to javafx.fxml;
    opens org.sture.java.budgeting.controller.statusbar.export to javafx.fxml;
    opens org.sture.java.budgeting.controller.home.export to javafx.fxml;
}