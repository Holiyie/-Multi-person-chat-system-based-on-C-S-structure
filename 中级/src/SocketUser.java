

import java.io.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class SocketUser {

    Socket sc;
    Begin begin;
    Zhuce zhuce;


    public static void main(String[] args) {
        SocketUser socketUser = new SocketUser();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    socketUser.begin = new Begin();
                    socketUser.begin.sc = socketUser.sc;
                    socketUser.begin.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*������¼���new Runnable(���ü������ɼ�))��ӵ�awt���¼������̵߳���ȥ
		awt���¼������̻߳ᰴ�ն��е�˳�����ε���ÿ����������¼�������
	ʹ�ø÷�ʽ��ԭ���ǣ�awt�ǵ��߳�ģʽ�ģ�
	����awt�����ֻ����(�Ƽ���ʽ)�¼������߳��з��ʣ�
	�Ӷ���֤���״̬�Ŀ�ȷ���ԡ�*/
    
    public SocketUser() {
        try {
            sc = new Socket("127.0.0.1", 61314);//���ӷ�����
            Thread t = new Thread(new SocketThread(sc));//�����ͻ����߳�
            t.start();
        } catch (Exception ex) {

        }
    }


    class SocketThread implements Runnable {

        Socket socket;
        BufferedReader br;
        String msg;

        SocketChat chat;
        Person person;
        SetPerson setPerson;

        public SocketThread(Socket sc) {
            this.socket = sc;
        }

        public void run() {

            try {
//                System.out.println("���ӳɹ�\n");
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while ((msg = br.readLine()) != null) {
                    System.out.println(msg);
                    String m = msg.replace("\n", "");
                    if (m.equals("�ɹ�")) {
                        System.out.println("��¼�ɹ�");
                        begin.dispose();
                        m = br.readLine();

                        m = m.replace("\n", "");
                        String[] str = m.split(";");

                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    person = new Person(str[0], str[1], str[2], str[3], str[4]);
                                    person.sc = socket;
                                    person.setVisible(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else if (m.equals("����ȷ")) {
                        JOptionPane.showMessageDialog(null, "�û��������벻��ȷ��", "Message", JOptionPane.INFORMATION_MESSAGE);
                    } else if (m.equals("ʧ��")) {
                        JOptionPane.showMessageDialog(null, "��¼ʧ�ܣ�", "Message", JOptionPane.INFORMATION_MESSAGE);
                    } else if (m.equals("��ʼע��")) {

                        zhuce = new Zhuce();
                        zhuce.sc = socket;
                        zhuce.setVisible(true);
                    } else if (m.equals("ע��ɹ�")) {

                        JOptionPane.showMessageDialog(null, "ע��ɹ�", "Message", JOptionPane.INFORMATION_MESSAGE);

                    } else if (m.equals("ע��ʧ��")) {
                    	
                        JOptionPane.showMessageDialog(null, "ע��ʧ��", "Message", JOptionPane.INFORMATION_MESSAGE);
                    } else if (m.equals("��ʼ�༭")) {
                        m = br.readLine();
                        m = m.replace("\n", "");
                        String[] str = m.split(";");

                       // String finalM = m;
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    setPerson = new SetPerson(str[0],str[1],str[2],str[3],str[4]);
                                    setPerson.sc = socket;
                                    setPerson.setVisible(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else if (m.equals("�޸ĳɹ�")) {

//                        new SetPerson().zct.setText("�޸ĳɹ���");
                        JOptionPane.showMessageDialog(null, "�޸ĳɹ�", "Message", JOptionPane.INFORMATION_MESSAGE);


                    } else if (m.equals("�޸�ʧ��")) {

//                        new SetPerson().zct.setText("�޸�ʧ�ܣ�");
                        JOptionPane.showMessageDialog(null, "�޸�ʧ��", "Message", JOptionPane.INFORMATION_MESSAGE);

                    } else if (m.equals("�޸Ľ���")) {
                        m = br.readLine();
                        m = m.replace("\n", "");
                        //String[] str = m.split(";");

                        setPerson.dispose();

                        /*person = new Person(str[0], str[1]);
                        person.sc=socket;
                        person.setVisible(true);*/

                    } else if (m.equals("����������")) {
                        m = br.readLine();
                        m = m.replace("\n", "");
                        String[] str = m.split(";");

                        person.setVisible(false);

                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    chat = new SocketChat(str[1], str[0]);
                                    chat.sc = socket;
                                    chat.setVisible(true);

                                    chat.Conn();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else if (m.equals("�����б�")) {
                        m = br.readLine();
                        m = m.replace("\n", "");
                        String[] str1 = m.split(":");
                        String[] str2 = str1[1].split(";");

                     

                        chat.lst.clear();//����ǰ��պ����б�
                        chat.t3.removeAllItems();//���JCombox
                        chat.t3.addItem("������");

                        for (String ss : str2) {
                            chat.lst.addElement(ss);//���û���Ϣ��ӵ������б�
                            chat.t3.addItem(ss);//���û���Ϣ��ӵ�JCombox
                        }
                    } else if (m.equals("�û�����")) {
                        m = br.readLine();
                        System.out.println(m);
                        chat.t1.append(m + "\n");//���������
                        chat.t2.append(m + "\n");//�ҵĽ���
                    } else if (m.equals("˽��")) {
                        m = br.readLine();
                        System.out.println("�յ���" + m);
                        chat.t2.append(m + "\n");//���ҵ����������ʾ˵����Ϣ
                    } else if (m.equals("Ⱥ��")) {
                        System.out.println("��ʼȺ��");
                        m = br.readLine();
                        System.out.println(m);
                        chat.t1.append(m + "\n");//�����������ʾ
                        chat.t2.append(m + "\n");//�ҵ����������ʾ

                    } else if (m.equals("ˢ��")) {
                        chat.lst.clear();//����ǰ��պ����б�
                        chat.t3.removeAllItems();//���JCombox
                        chat.t3.addItem("������");
                        m = br.readLine();
                        m = m.replace("\n", "");
                        String[] str3 = m.split(";");
                        for (String ss : str3) {
                            chat.lst.addElement(ss);//���û���Ϣ��ӵ������б�
                            chat.t3.addItem(ss);//���û���Ϣ��ӵ�JCombox
                        }
                    } else if (m.equals("����")) {
                        m = br.readLine();
                        chat.t1.append(m);//�����������ʾ�û�����
                        chat.t2.append(m);//�ҵĽ���
                        chat.dispose();
                    } else if (m.equals("��ʷ��¼")) {
                        m = br.readLine();
                        m = m.replace("\n", "");
                        String[] str = m.split(";");

                        String[][] hismsg = new String[str.length][1];
                        for (int i = 0; i < str.length; i++) {
                            hismsg[i][0] = str[i];
                        }

                        History history = new History(hismsg);
                        history.setVisible(true);
//                        history.updateList(hismsg);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}


@SuppressWarnings("serial")
class Begin extends JFrame {

    JTextField t1;
    JTextField t2;
    JButton b1;
    JButton b2;
    String t3;//�û��������벻��ȷ
    JLayeredPane layeredPane;
    //����һ��Panel��һ��Label���ڴ��ͼƬ����Ϊ������
    JPanel jp;
    JLabel jl;
    ImageIcon image;
    Socket sc;
    BufferedWriter bw;


    public Begin() {
        initFrame();
    }

    public Begin(String l) {
        this.t3 = l;
        initFrame();
    }

    public void initFrame() {
        layeredPane = new JLayeredPane();
        layeredPane.setSize(600, 400);
        image = new ImageIcon("Image\\Login.png");
        jp = new JPanel();
        jp.setSize(600, 400);
        jp.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        jl = new JLabel(image);
        // jl.setBounds(0,0,image.getIconWidth(),image.getIconHeight());
        jp.add(jl);


        t1 = new JTextField(35);
        t1.setBounds(200, 255, 200, 35);
        t2 = new JTextField(35);
        t2.setBounds(200, 315, 200, 35);


        b2 = new JButton();
        b2.setIcon(new ImageIcon("Image\\ע��.png"));
        b2.setBounds(450, 255, 75, 35);

        b2.addActionListener(new ButtonHandler());

        b1 = new JButton();
        b1.setIcon(new ImageIcon("Image\\��¼.png"));

        b1.setBounds(450, 315, 75, 35);

        b1.addActionListener(new ButtonHandler());
        layeredPane.add(jp, JLayeredPane.DEFAULT_LAYER);
        //��jb�ŵ���һ��ĵط�
        layeredPane.add(b1, JLayeredPane.MODAL_LAYER);
        layeredPane.add(b2, JLayeredPane.MODAL_LAYER);
        layeredPane.add(t1, JLayeredPane.MODAL_LAYER);
        layeredPane.add(t2, JLayeredPane.MODAL_LAYER);

        this.setLayeredPane(layeredPane);
        this.setSize(image.getIconWidth(), image.getIconHeight());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(image.getIconWidth(), image.getIconHeight());
//        this.setVisible(true);
    }

    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            try {

                bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                if (obj == b1) {
                    String zh = t1.getText();
                    String mm = t2.getText();
                    bw.write("��¼\n");//���͵�¼��ʶ
                    bw.write(zh + ";" + mm + "\n");//���͵�¼��Ϣ
                    bw.flush();
//                    dispose();
                }

                if (obj == b2) {
                    bw.write("��ʼע��\n");//����ע���ʶ
                    bw.flush();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}


@SuppressWarnings("serial")
class Zhuce extends JDialog {

    JTextField zht;
    JTextField mmt;
    JTextField nct;
    JTextField xbt;
    JTextField txt;
    JTextField zct;
    JButton b;
    JButton f;
    JLayeredPane layeredPane;
    //����һ��Panel��һ��Label���ڴ��ͼƬ����Ϊ������
    JPanel jp;
    JLabel jl;
    ImageIcon image;
    Socket sc;
    BufferedWriter bw;

    public Zhuce() {
        initFrame();
    }

    public void initFrame() {
        layeredPane = new JLayeredPane();
        layeredPane.setSize(600, 800);
        image = new ImageIcon("Image\\Zhuce.png");
        jp = new JPanel();
        jp.setSize(600, 800);
        jp.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        jl = new JLabel(image);
        // jl.setBounds(0,0,image.getIconWidth(),image.getIconHeight());
        jp.add(jl);


        JButton b = new JButton();
        b.setIcon(new ImageIcon("Image\\ע��.png"));
        b.setBounds(455, 360, 75, 35);
        b.addActionListener(new ButtonHandler());


        zht = new JTextField(35);
        zht.setBounds(200, 360, 200, 35);

        mmt = new JTextField(35);
        mmt.setBounds(200, 440, 200, 35);

        nct = new JTextField(35);
        nct.setBounds(200, 520, 200, 35);

        xbt = new JTextField(35);
        xbt.setBounds(200, 600, 50, 35);

        txt = new JTextField(35);
        txt.setBounds(200, 680, 50, 35);


        JLabel l3 = new JLabel("����1��2");
        l3.setBounds(300, 680, 100, 50);


        f = new JButton();
        f.setIcon(new ImageIcon("Image\\����.png"));
        f.setBounds(455, 440, 75, 35);

        f.addActionListener(new BackHandler());


        layeredPane.add(jp, JLayeredPane.DEFAULT_LAYER);
        //��jb�ŵ���һ��ĵط�
        layeredPane.add(b, JLayeredPane.MODAL_LAYER);
        layeredPane.add(f, JLayeredPane.MODAL_LAYER);
        layeredPane.add(zht, JLayeredPane.MODAL_LAYER);
        layeredPane.add(mmt, JLayeredPane.MODAL_LAYER);
        layeredPane.add(nct, JLayeredPane.MODAL_LAYER);
        layeredPane.add(xbt, JLayeredPane.MODAL_LAYER);
        layeredPane.add(txt, JLayeredPane.MODAL_LAYER);

        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);

        this.setLayeredPane(layeredPane);
        this.setSize(image.getIconWidth(), image.getIconHeight());
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocation(image.getIconWidth(), image.getIconHeight());
//        this.setVisible(true);


    }


    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String zh = zht.getText();
            String mm = mmt.getText();
            String nc = nct.getText();
            String xb = xbt.getText();
            String tx = txt.getText();

            try {
//                sc = new SocketUser();
                bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                bw.write("ע��\n");
                bw.write(zh + ";" + mm + ";" + nc + ";" + xb + ";" + tx + "\n");
                bw.flush();
                dispose();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    class BackHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            dispose();
        }
    }

}

@SuppressWarnings("serial")
class Person extends JFrame {

    JButton b1;
    JButton b2;
    String t1;//�˺�
    String t2;//����
    String t3;//�Ա�
    String t4;//�ǳ�
    String t5;//ͷ��
    JLayeredPane layeredPane;
    //����һ��Panel��һ��Label���ڴ��ͼƬ����Ϊ������
    JPanel jp;
    JLabel jl;
    ImageIcon image;
    Socket sc;
    BufferedWriter bw;

    public Person(String t1, String t2) {
        this.t1 = t1;
        this.t2 = t2;
        initFrame();
    }

    public Person(String t1, String t2, String t3, String t4, String t5) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        initFrame();
    }

    public void initFrame() {

        layeredPane = new JLayeredPane();
        layeredPane.setSize(600, 400);
        image = new ImageIcon("Image\\������Ϣ.png");
        jp = new JPanel();
        jp.setSize(600, 400);
        jp.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        jl = new JLabel(image);
        // jl.setBounds(0,0,image.getIconWidth(),image.getIconHeight());
        jp.add(jl);


        if (t5.trim().equals("1")) {
            JLabel l = new JLabel(new ImageIcon("Image\\1 100.png"));
            l.setBounds(245, 100, 100, 100);
            layeredPane.add(l, JLayeredPane.MODAL_LAYER);
        }
        if (t5.trim().equals("2")) {
            JLabel l = new JLabel(new ImageIcon("Image\\2 100.png"));
            l.setBounds(245, 100, 100, 100);
            layeredPane.add(l, JLayeredPane.MODAL_LAYER);
        }


        JLabel nc = new JLabel("<html><body>"
                + "<p style='color :373F5E; font-size:20px; font-family = '΢���ź�''>�ǳƣ�"+t4+"</p>"

                + "</body></html>");
        nc.setBounds(170, 280, 150, 50);

        JLabel zh = new JLabel("<html><body>"
                + "<p style='color :373F5E; font-size:20px; font-family = '΢���ź�''>�˺ţ�"+t1+"</p>"

                + "</body></html>");
        zh.setBounds(170, 340, 150, 50);

        JLabel xb = new JLabel("<html><body>"
                + "<p style='color :373F5E; font-size:20px; font-family = '΢���ź�''>�Ա�"+t3+"</p>"

                + "</body></html>");
        xb.setBounds(170, 400, 150, 50);


        b1 = new JButton();
        b1.setIcon(new ImageIcon("Image\\�༭��Ϣ.png"));
        b1.setBounds(245, 550, 100, 50);
        ButtonHandler buttonHandler = new ButtonHandler();
        b1.addActionListener(buttonHandler);

        b2 = new JButton();
        b2.setIcon(new ImageIcon("Image\\��������.png"));
        b2.setBounds(245, 650, 100, 50);
        b2.addActionListener(buttonHandler);


        layeredPane.add(jp, JLayeredPane.DEFAULT_LAYER);

        layeredPane.add(b1, JLayeredPane.MODAL_LAYER);
        layeredPane.add(b2, JLayeredPane.MODAL_LAYER);
        layeredPane.add(nc, JLayeredPane.MODAL_LAYER);
        layeredPane.add(zh, JLayeredPane.MODAL_LAYER);
        layeredPane.add(xb, JLayeredPane.MODAL_LAYER);


//        setModal(true);
//        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);

        this.setLayeredPane(layeredPane);
        this.setSize(image.getIconWidth(), image.getIconHeight()+80);
//        this.setSize(image.getIconWidth(), image.getIconHeight());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocation(image.getIconWidth(), image.getIconHeight());
//        this.setVisible(true);


    }

    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            try {


                if (obj == b1) {
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("�༭��Ϣ\n");//�༭�����ʶ

                    bw.write(t1 + ";" + t2 + ";" + t3 + ";" + t4 + ";" + t5 + "\n");
                    bw.flush();
                }

                if (obj == b2) {
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("��ʼ����\n");//�������������ʶ
                    bw.write(t4 + ";" + t3 + "\n");
                    bw.flush();
                    dispose();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}

@SuppressWarnings("serial")
class SetPerson extends JDialog {


    JTextField mmt;
    JTextField nct;
    JTextField xbt;
    JTextField txt;
    JTextField zct;
    JButton b;
    JButton f;

    String t1;//�˺�
    String t2;//����
    String t3;//�Ա�
    String t4;//�ǳ�
    String t5;//ͷ��

    JLayeredPane layeredPane;
    //����һ��Panel��һ��Label���ڴ��ͼƬ����Ϊ������
    JPanel jp;
    JLabel jl;
    ImageIcon image;
    Socket sc;
    BufferedWriter bw;

    public SetPerson() {
        initFrame();
    }

    public SetPerson(String t1,String t2,String t3,String t4,String t5) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        initFrame();
    }

    public void initFrame() {
        layeredPane = new JLayeredPane();
        layeredPane.setSize(600, 800);
        image = new ImageIcon("Image\\Zhuce2.png");
        jp = new JPanel();
        jp.setSize(600, 800);
        jp.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        jl = new JLabel(image);
        // jl.setBounds(0,0,image.getIconWidth(),image.getIconHeight());
        jp.add(jl);


        JButton b = new JButton();
        b.setIcon(new ImageIcon("Image\\�޸�.png"));
        b.setBounds(455, 360, 75, 35);
        b.addActionListener(new ButtonHandler());


        mmt = new JTextField(35);
        mmt.setBounds(200, 440, 200, 35);
        mmt.setText(t2);

        nct = new JTextField(35);
        nct.setBounds(200, 520, 200, 35);
        nct.setText(t4);

        xbt = new JTextField(35);
        xbt.setBounds(200, 600, 50, 35);
        xbt.setText(t3);

        txt = new JTextField(35);
        txt.setBounds(200, 680, 50, 35);
        txt.setText(t5);

        JLabel l3 = new JLabel("����1��2");
        l3.setBounds(300, 680, 100, 50);


        f = new JButton();
        f.setIcon(new ImageIcon("Image\\����.png"));
        f.setBounds(455, 440, 75, 35);

        f.addActionListener(new BackHandler());


        layeredPane.add(jp, JLayeredPane.DEFAULT_LAYER);
        //��jb�ŵ���һ��ĵط�
        layeredPane.add(b, JLayeredPane.MODAL_LAYER);
        layeredPane.add(f, JLayeredPane.MODAL_LAYER);

        layeredPane.add(mmt, JLayeredPane.MODAL_LAYER);
        layeredPane.add(nct, JLayeredPane.MODAL_LAYER);
        layeredPane.add(xbt, JLayeredPane.MODAL_LAYER);
        layeredPane.add(txt, JLayeredPane.MODAL_LAYER);

        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);


        this.setLayeredPane(layeredPane);
        this.setSize(image.getIconWidth(), image.getIconHeight()+80);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocation(image.getIconWidth(), image.getIconHeight());
//        this.setVisible(true);
    }

    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String mm = mmt.getText();
            String nc = nct.getText();
            String xb = xbt.getText();
            String tx = txt.getText();


            try {
//                sc = new SocketUser();
                bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                bw.write("�޸�\n");//�����޸���Ϣ����
                bw.write(t1 + ";" + mm + ";" + nc + ";" + xb + ";" + tx + "\n");//�����޸���Ϣ
                bw.flush();
                dispose();
            } catch (Exception ex) {

            }
        }
    }


    class BackHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            try {
//                sc = new SocketUser();
//                bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
//                bw.write("�޸Ľ���\n");//�����޸Ľ������ظ�����Ϣ�����ʶ
//                bw.write(t1 + "\n");//�����û���
//                bw.flush();
                dispose();
            } catch (Exception ex) {

            }


        }

    }
}