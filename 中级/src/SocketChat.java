

import javax.swing.*;
//import javax.swing.border.TitledBorder;

//import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.text.SimpleDateFormat;


@SuppressWarnings("serial")
public class SocketChat extends JFrame {
    JLayeredPane layeredPane;
    //����һ��Panel��һ��Label���ڴ��ͼƬ����Ϊ������
    JPanel jp;
    JLabel jl;
    ImageIcon image;
    JTextArea t1;
    JTextArea t2;
    DefaultListModel<String> lst;
    JList<String> ls;
    JComboBox<String> t3;
    JCheckBox jcb;
    JTextField t4;
    JButton fs;
    JButton sx;
    JButton fh;
    JButton ltjl;
    String t;//�˺�
    String n;//�ǳ�
    String s;//�Ա�
    Socket sc;
    BufferedWriter bw;

    public SocketChat() {
        initFrame();
    }

    public SocketChat(String n) {
        this.n = n;
        initFrame();
    }

    public SocketChat(String s, String n) {
        this.n = n;
        this.s = s;
        initFrame();
    }

    public void initFrame() {
        layeredPane = new JLayeredPane();
        layeredPane.setSize(800, 600);
        image = new ImageIcon("Image\\�����ұ���.png");
        jp = new JPanel();
        jp.setSize(800, 600);
        jp.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        jl = new JLabel(image);
        // jl.setBounds(0,0,image.getIconWidth(),image.getIconHeight());
        jp.add(jl);


        t1 = new JTextArea(680, 170);
        t1.setBounds(50, 50, 680, 170);
        t2 = new JTextArea(680, 170);
        t2.setBounds(50, 310, 680, 170);


        lst = new DefaultListModel<String>();
        ls = new JList<String>(lst);
        ls.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ls.setVisibleRowCount(11);
        ls.setFixedCellHeight(430);
        ls.setFixedCellWidth(220);
        ls.setBounds(545, 50, 220, 430);


        t3 = new JComboBox<String>();
        t3.addItem("������");
        t3.setBounds(120, 520, 80, 30);

        jcb = new JCheckBox("˽��");
        jcb.setBounds(220, 520, 50, 30);

        t4 = new JTextField(30);
        t4.setBounds(50, 555, 400, 30);

        ButtonHandler buttonHandler = new ButtonHandler();

        fs = new JButton();
        fs.setIcon(new ImageIcon("Image\\����.png"));
        fs.setBounds(500, 555, 75, 35);
        fs.addActionListener(buttonHandler);


        sx = new JButton();
        sx.setIcon(new ImageIcon("Image\\ˢ��.png"));
        sx.setBounds(610, 555, 75, 35);
        sx.addActionListener(buttonHandler);

        fh = new JButton();
        fh.setIcon(new ImageIcon("Image\\����.png"));
        fh.setBounds(720, 555, 75, 35);
        fh.addActionListener(buttonHandler);

        ltjl = new JButton();
        ltjl.setIcon(new ImageIcon("Image\\��ʷ��¼.png"));
        ltjl.setBounds(300, 515, 75, 35);
        ltjl.addActionListener(buttonHandler);


        layeredPane.add(jp, JLayeredPane.DEFAULT_LAYER);

        layeredPane.add(t1, JLayeredPane.MODAL_LAYER);
        layeredPane.add(t2, JLayeredPane.MODAL_LAYER);
        //layeredPane.add(ls, JLayeredPane.MODAL_LAYER);
        layeredPane.add(fs, JLayeredPane.MODAL_LAYER);
        layeredPane.add(sx, JLayeredPane.MODAL_LAYER);
        layeredPane.add(fh, JLayeredPane.MODAL_LAYER);
        layeredPane.add(ltjl, JLayeredPane.MODAL_LAYER);
        layeredPane.add(t3, JLayeredPane.MODAL_LAYER);
        layeredPane.add(t4, JLayeredPane.MODAL_LAYER);
        layeredPane.add(jcb, JLayeredPane.MODAL_LAYER);

        this.setLayeredPane(layeredPane);
        this.setSize(image.getIconWidth()+20, image.getIconHeight()+50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocation(image.getIconWidth(), image.getIconHeight());
//        this.setVisible(true);


    }

    public void Conn() {
        try {
            Date c = new Date();
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = date.format(c);

            bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
            bw.write("�����б�\n");//���ͺ����б��ʶ
            bw.write(n + "(" + s + ")\n");//�����û��ǳƺ��Ա�
            bw.flush();
            bw.write("�û�����\n");//�����û����������ұ�ʶ
            bw.write("��" + n + "��" + "(" + time + ")" + "����������\n");//���ͺ��˽���������
            bw.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Date c;
            Object obj = e.getSource();
            if (obj == fs) {//����
                try {
                    c = new Date();
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = date.format(c);//ʱ��
//                    sc = new SocketUser();
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    if (!t4.getText().equals("")) {
                        if (jcb.isSelected()) {//˽��
                            String name = (String) t3.getSelectedItem();


                            bw.write("˽��\n");
                            bw.write(n + ":" + name + ";" + "�����Ļ���" + "(" + time + ")" + n + "��" + name + "˵��" + t4.getText() + "\n");
                            bw.flush();
                            t4.setText("");

                        } else {//Ⱥ��

                            bw.write("Ⱥ��\n");
                            bw.write(n + "˵��" + t4.getText() + "(" + time + ")\n");
                            bw.flush();
                            t4.setText("");
                        }
                    }

                } catch (Exception ex) {

                }

            }

            if (obj == sx) {//ˢ��
                try {
//                    sc = new SocketUser();
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("ˢ��\n");//����ˢ�±�ʶ
                    bw.flush();


                } catch (Exception ex) {

                }

            }

            if (obj == fh) {//����

                try {
                    c = new Date();
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = date.format(c);
//                    sc = new SocketUser();
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("����\n");//�������߱�ʶ
                    bw.write(n + "(" + s + ")" + ":�뿪������" + "/" + time + "\n");
                    bw.flush();

                    dispose();
                } catch (Exception ex) {

                }
            }

            if (obj == ltjl) {//��ʷ��¼

                try {
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("��ʷ��¼\n");
                    bw.flush();
                } catch (Exception ex) {

                }
            }
        }
    }


    public static void main(String[] args) {
        new SocketChat();
    }
}







