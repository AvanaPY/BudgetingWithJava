package org.sture.java.budgeting.controller.settings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;

@SuppressWarnings("FieldCanBeLocal")
public class CategoryCell extends ListCell<BudgetEntryCategory> {
    private final HBox hbox = new HBox();
    private final Label label = new Label("...");
    private final Button button = new Button("Delete");
    private final CheckBox isPositiveCheckBox = new CheckBox();
    private final Pane pane = new Pane();
    private final Pane pane2 = new Pane();

    private BudgetEntryCategory lastItem;

    public CategoryCell(){
        super();
        hbox.getChildren().addAll(label, pane, isPositiveCheckBox, pane2, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        HBox.setHgrow(pane2, Priority.NEVER);
        pane2.setPrefWidth(10);

        button.setMaxHeight(15);
        button.setFont(new Font("", 10));

        setGraphic(hbox);

        setEditable(true);
    }

    @Override
    public void startEdit(){
        super.startEdit();
        label.requestFocus();
        System.out.println("Starting edit");
    }

    @Override
    public void commitEdit(BudgetEntryCategory t){
        super.commitEdit(t);
    }

    @Override
    public void cancelEdit(){
        super.cancelEdit();
        System.out.println("Cancelling edit");
    }

    @Override
    protected void updateItem(BudgetEntryCategory item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        if(empty){
            lastItem = null;
            setGraphic(null);
            return;
        }


        lastItem = item;
        label.setText(item.toString());
        isPositiveCheckBox.setSelected(item.isPositive());
        setGraphic(hbox);
    }
}
