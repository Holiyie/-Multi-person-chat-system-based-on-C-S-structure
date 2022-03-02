

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
    //创建一个Panel和一个Label用于存放图片，作为背景。
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
    String t;//账号
    String n;//昵称
    String s;//性别
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
        image = new ImageIcon("Image\\聊天室背景.png");
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
        t3.addItem("所有人");
        t3.setBounds(120, 520, 80, 30);

        jcb = new JCheckBox("私聊");
        jcb.setBounds(220, 520, 50, 30);

        t4 = new JTextField(30);
        t4.setBounds(50, 555, 400, 30);

        ButtonHandler buttonHandler = new ButtonHandler();

        fs = new JButton();
        fs.setIcon(new ImageIcon("Image\\发送.png"));
        fs.setBounds(500, 555, 75, 35);
        fs.addActionListener(buttonHandler);


        sx = new JButton();
        sx.setIcon(new ImageIcon("Image\\刷新.png"));
        sx.setBounds(610, 555, 75, 35);
        sx.addActionListener(buttonHandler);

        fh = new JButton();
        fh.setIcon(new ImageIcon("Image\\下线.png"));
        fh.setBounds(720, 555, 75, 35);
        fh.addActionListener(buttonHandler);

        ltjl = new JButton();
        ltjl.setIcon(new ImageIcon("Image\\历史记录.png"));
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
            bw.write("好友列表\n");//发送好友列表标识
            bw.write(n + "(" + s + ")\n");//发送用户昵称和性别
            bw.flush();
            bw.write("用户进入\n");//发送用户进入聊天室标识
            bw.write("【" + n + "】" + "(" + time + ")" + "进入聊天室\n");//发送何人进入聊天室
            bw.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Date c;
            Object obj = e.getSource();
            if (obj == fs) {//发送
                try {
                    c = new Date();
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = date.format(c);//时间
//                    sc = new SocketUser();
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    if (!t4.getText().equals("")) {
                        if (jcb.isSelected()) {//私聊
                            String name = (String) t3.getSelectedItem();


                            bw.write("私聊\n");
                            bw.write(n + ":" + name + ";" + "【悄悄话】" + "(" + time + ")" + n + "对" + name + "说：" + t4.getText() + "\n");
                            bw.flush();
                            t4.setText("");

                        } else {//群聊

                            bw.write("群聊\n");
                            bw.write(n + "说：" + t4.getText() + "(" + time + ")\n");
                            bw.flush();
                            t4.setText("");
                        }
                    }

                } catch (Exception ex) {

                }

            }

            if (obj == sx) {//刷新
                try {
//                    sc = new SocketUser();
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("刷新\n");//发送刷新标识
                    bw.flush();


                } catch (Exception ex) {

                }

            }

            if (obj == fh) {//下线

                try {
                    c = new Date();
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = date.format(c);
//                    sc = new SocketUser();
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("下线\n");//发送下线标识
                    bw.write(n + "(" + s + ")" + ":离开聊天室" + "/" + time + "\n");
                    bw.flush();

                    dispose();
                } catch (Exception ex) {

                }
            }

            if (obj == ltjl) {//历史记录

                try {
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("历史记录\n");
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







