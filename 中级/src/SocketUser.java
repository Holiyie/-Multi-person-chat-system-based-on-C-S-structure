

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

    /*把这个事件（new Runnable(设置计算器可见))添加到awt的事件处理线程当中去
		awt的事件处理线程会按照队列的顺序依次调用每个待处理的事件来运行
	使用该方式的原因是：awt是单线程模式的，
	所有awt的组件只能在(推荐方式)事件处理线程中访问，
	从而保证组件状态的可确定性。*/
    
    public SocketUser() {
        try {
            sc = new Socket("127.0.0.1", 61314);//连接服务器
            Thread t = new Thread(new SocketThread(sc));//建立客户端线程
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
//                System.out.println("连接成功\n");
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while ((msg = br.readLine()) != null) {
                    System.out.println(msg);
                    String m = msg.replace("\n", "");
                    if (m.equals("成功")) {
                        System.out.println("登录成功");
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
                    } else if (m.equals("不正确")) {
                        JOptionPane.showMessageDialog(null, "用户名或密码不正确！", "Message", JOptionPane.INFORMATION_MESSAGE);
                    } else if (m.equals("失败")) {
                        JOptionPane.showMessageDialog(null, "登录失败！", "Message", JOptionPane.INFORMATION_MESSAGE);
                    } else if (m.equals("开始注册")) {

                        zhuce = new Zhuce();
                        zhuce.sc = socket;
                        zhuce.setVisible(true);
                    } else if (m.equals("注册成功")) {

                        JOptionPane.showMessageDialog(null, "注册成功", "Message", JOptionPane.INFORMATION_MESSAGE);

                    } else if (m.equals("注册失败")) {
                    	
                        JOptionPane.showMessageDialog(null, "注册失败", "Message", JOptionPane.INFORMATION_MESSAGE);
                    } else if (m.equals("开始编辑")) {
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

                    } else if (m.equals("修改成功")) {

//                        new SetPerson().zct.setText("修改成功！");
                        JOptionPane.showMessageDialog(null, "修改成功", "Message", JOptionPane.INFORMATION_MESSAGE);


                    } else if (m.equals("修改失败")) {

//                        new SetPerson().zct.setText("修改失败！");
                        JOptionPane.showMessageDialog(null, "修改失败", "Message", JOptionPane.INFORMATION_MESSAGE);

                    } else if (m.equals("修改结束")) {
                        m = br.readLine();
                        m = m.replace("\n", "");
                        //String[] str = m.split(";");

                        setPerson.dispose();

                        /*person = new Person(str[0], str[1]);
                        person.sc=socket;
                        person.setVisible(true);*/

                    } else if (m.equals("进入聊天室")) {
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

                    } else if (m.equals("好友列表")) {
                        m = br.readLine();
                        m = m.replace("\n", "");
                        String[] str1 = m.split(":");
                        String[] str2 = str1[1].split(";");

                     

                        chat.lst.clear();//接收前清空好友列表
                        chat.t3.removeAllItems();//清空JCombox
                        chat.t3.addItem("所有人");

                        for (String ss : str2) {
                            chat.lst.addElement(ss);//将用户信息添加到好友列表
                            chat.t3.addItem(ss);//将用户信息添加到JCombox
                        }
                    } else if (m.equals("用户进入")) {
                        m = br.readLine();
                        System.out.println(m);
                        chat.t1.append(m + "\n");//主聊天界面
                        chat.t2.append(m + "\n");//我的界面
                    } else if (m.equals("私聊")) {
                        m = br.readLine();
                        System.out.println("收到：" + m);
                        chat.t2.append(m + "\n");//在我的聊天界面显示说话信息
                    } else if (m.equals("群聊")) {
                        System.out.println("开始群聊");
                        m = br.readLine();
                        System.out.println(m);
                        chat.t1.append(m + "\n");//主聊天界面显示
                        chat.t2.append(m + "\n");//我的聊天界面显示

                    } else if (m.equals("刷新")) {
                        chat.lst.clear();//接收前清空好友列表
                        chat.t3.removeAllItems();//清空JCombox
                        chat.t3.addItem("所有人");
                        m = br.readLine();
                        m = m.replace("\n", "");
                        String[] str3 = m.split(";");
                        for (String ss : str3) {
                            chat.lst.addElement(ss);//将用户信息添加到好友列表
                            chat.t3.addItem(ss);//将用户信息添加到JCombox
                        }
                    } else if (m.equals("下线")) {
                        m = br.readLine();
                        chat.t1.append(m);//主聊天界面显示用户下线
                        chat.t2.append(m);//我的界面
                        chat.dispose();
                    } else if (m.equals("历史记录")) {
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
    String t3;//用户名或密码不正确
    JLayeredPane layeredPane;
    //创建一个Panel和一个Label用于存放图片，作为背景。
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
        b2.setIcon(new ImageIcon("Image\\注册.png"));
        b2.setBounds(450, 255, 75, 35);

        b2.addActionListener(new ButtonHandler());

        b1 = new JButton();
        b1.setIcon(new ImageIcon("Image\\登录.png"));

        b1.setBounds(450, 315, 75, 35);

        b1.addActionListener(new ButtonHandler());
        layeredPane.add(jp, JLayeredPane.DEFAULT_LAYER);
        //将jb放到高一层的地方
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
                    bw.write("登录\n");//发送登录标识
                    bw.write(zh + ";" + mm + "\n");//发送登录信息
                    bw.flush();
//                    dispose();
                }

                if (obj == b2) {
                    bw.write("开始注册\n");//发送注册标识
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
    //创建一个Panel和一个Label用于存放图片，作为背景。
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
        b.setIcon(new ImageIcon("Image\\注册.png"));
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


        JLabel l3 = new JLabel("请填1或2");
        l3.setBounds(300, 680, 100, 50);


        f = new JButton();
        f.setIcon(new ImageIcon("Image\\返回.png"));
        f.setBounds(455, 440, 75, 35);

        f.addActionListener(new BackHandler());


        layeredPane.add(jp, JLayeredPane.DEFAULT_LAYER);
        //将jb放到高一层的地方
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
                bw.write("注册\n");
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
    String t1;//账号
    String t2;//密码
    String t3;//性别
    String t4;//昵称
    String t5;//头像
    JLayeredPane layeredPane;
    //创建一个Panel和一个Label用于存放图片，作为背景。
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
        image = new ImageIcon("Image\\个人信息.png");
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
                + "<p style='color :373F5E; font-size:20px; font-family = '微软雅黑''>昵称："+t4+"</p>"

                + "</body></html>");
        nc.setBounds(170, 280, 150, 50);

        JLabel zh = new JLabel("<html><body>"
                + "<p style='color :373F5E; font-size:20px; font-family = '微软雅黑''>账号："+t1+"</p>"

                + "</body></html>");
        zh.setBounds(170, 340, 150, 50);

        JLabel xb = new JLabel("<html><body>"
                + "<p style='color :373F5E; font-size:20px; font-family = '微软雅黑''>性别："+t3+"</p>"

                + "</body></html>");
        xb.setBounds(170, 400, 150, 50);


        b1 = new JButton();
        b1.setIcon(new ImageIcon("Image\\编辑信息.png"));
        b1.setBounds(245, 550, 100, 50);
        ButtonHandler buttonHandler = new ButtonHandler();
        b1.addActionListener(buttonHandler);

        b2 = new JButton();
        b2.setIcon(new ImageIcon("Image\\进入聊天.png"));
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
                    bw.write("编辑信息\n");//编辑请求标识

                    bw.write(t1 + ";" + t2 + ";" + t3 + ";" + t4 + ";" + t5 + "\n");
                    bw.flush();
                }

                if (obj == b2) {
                    bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    bw.write("开始聊天\n");//打开聊天室请求标识
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

    String t1;//账号
    String t2;//密码
    String t3;//性别
    String t4;//昵称
    String t5;//头像

    JLayeredPane layeredPane;
    //创建一个Panel和一个Label用于存放图片，作为背景。
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
        b.setIcon(new ImageIcon("Image\\修改.png"));
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

        JLabel l3 = new JLabel("请填1或2");
        l3.setBounds(300, 680, 100, 50);


        f = new JButton();
        f.setIcon(new ImageIcon("Image\\返回.png"));
        f.setBounds(455, 440, 75, 35);

        f.addActionListener(new BackHandler());


        layeredPane.add(jp, JLayeredPane.DEFAULT_LAYER);
        //将jb放到高一层的地方
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
                bw.write("修改\n");//发送修改信息请求
                bw.write(t1 + ";" + mm + ";" + nc + ";" + xb + ";" + tx + "\n");//发送修改信息
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
//                bw.write("修改结束\n");//发送修改结束返回个人信息界面标识
//                bw.write(t1 + "\n");//发送用户名
//                bw.flush();
                dispose();
            } catch (Exception ex) {

            }


        }

    }
}