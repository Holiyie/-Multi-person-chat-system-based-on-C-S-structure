import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class History extends JDialog {

    // 表格数据
    private TableComponent table;
    private Object[] columnNames = {"内容"};
    private String[][] rowData;

    public History(String[][] data) {
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setBounds(0, 0, 569, 401);
        getContentPane().setLayout(null);

        JPanel userListPanel = new JPanel();
        userListPanel.setBorder(BorderFactory.createTitledBorder("列表"));
        userListPanel.setBounds(10, 10, 536, 346);
        getContentPane().add(userListPanel);
        // 设置表格数据
        rowData =data;

        // 创建一个表格，指定 所有行数据 和 表头
        table = new TableComponent(rowData, columnNames);
        userListPanel.add(table.getTableHeader());                        // 添加表头
        userListPanel.setLayout(new BorderLayout(5, 5));
        JScrollPane scrollPane = new JScrollPane(table);                // 将表格添加到滚动面板中
        userListPanel.add(scrollPane);

        
    }

    public void updateList(String[][] data) {
        rowData = data;
        table.updateModel(rowData, columnNames);
    }
}
