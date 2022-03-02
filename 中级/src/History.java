import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class History extends JDialog {

    // �������
    private TableComponent table;
    private Object[] columnNames = {"����"};
    private String[][] rowData;

    public History(String[][] data) {
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setBounds(0, 0, 569, 401);
        getContentPane().setLayout(null);

        JPanel userListPanel = new JPanel();
        userListPanel.setBorder(BorderFactory.createTitledBorder("�б�"));
        userListPanel.setBounds(10, 10, 536, 346);
        getContentPane().add(userListPanel);
        // ���ñ������
        rowData =data;

        // ����һ�����ָ�� ���������� �� ��ͷ
        table = new TableComponent(rowData, columnNames);
        userListPanel.add(table.getTableHeader());                        // ��ӱ�ͷ
        userListPanel.setLayout(new BorderLayout(5, 5));
        JScrollPane scrollPane = new JScrollPane(table);                // �������ӵ����������
        userListPanel.add(scrollPane);

        
    }

    public void updateList(String[][] data) {
        rowData = data;
        table.updateModel(rowData, columnNames);
    }
}
