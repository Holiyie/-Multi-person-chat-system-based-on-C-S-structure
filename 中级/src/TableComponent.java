import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public class TableComponent extends JTable {

    public TableComponent() {
        super();
    }

    public TableComponent(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        setSelectionBackground(Color.CYAN);
        setSelectionForeground(Color.BLACK);
        setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // ±ÌÕ∑…Ë÷√
        getTableHeader().setResizingAllowed(true);
        getTableHeader().setReorderingAllowed(false);
        setVisible(true);
    }

    public void updateModel(Object[][] rowData, Object[] columnNames) {
        DefaultTableModel dataModel = new DefaultTableModel(rowData, columnNames);
        setModel(dataModel);
        revalidate();
    }
}
