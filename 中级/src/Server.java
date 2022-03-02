


import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class Server {

    static ServerSocket server;
    static List<Socket> sockets = new ArrayList<>();//用户线程集合
    static ArrayList<String> list1 = new ArrayList<String>();//用户列表集合
//    static File file = new File("his.txt");

    public static void main(String[] args) {
        try {
            server = new ServerSocket(61314);
            while (true) {
                Socket sc = server.accept();
                Chat c = new Chat(sc);
                Thread t = new Thread(c);

                t.start();

                sockets.add(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

class Chat implements Runnable {

    Socket sc;
    BufferedReader br;
    BufferedWriter bw;
    String msg;


    public Chat(Socket sc) {
        this.sc = sc;
    }

    public void run() {
        try {
            //System.out.println("建立连接\n");
            br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));

            while ((msg = br.readLine()) != null) {
                System.out.println(msg);

                String m = msg.replace("\n", "");
                if (m.equals("登录")) {

//                    System.out.println("开始登录");
                    m = br.readLine();
                    m = m.replace("\n", "");
                    String[] str = m.split(";");
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                    } catch (ClassNotFoundException e1) {
                    }

                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_chat?characterEncoding=utf-8&useUnicode=true&useSSL=false", "root", "123456");
                        Statement stat = con.createStatement();
                        ResultSet s = stat.executeQuery("select * from yhxx where zh='" + str[0] + "' and mm='" + str[1] + "'");

                        if (s.next()) {
                            String zhm = s.getString("zh");
                            String mmm = s.getString("mm");
                            String xbm = s.getString("xb");
                            String ncm = s.getString("nc");
                            String txm = s.getString("tx");
                            System.out.println("登录成功");
                            bw.write("成功\n");//发送登录成功标识
//                            bw.flush();
                            bw.write(zhm + ";" + mmm + ";" + xbm + ";" + ncm + ";" + txm + "\n");//返送用户名，密码，性别，昵称用于建立个人信息界面
                            bw.flush();
                        } else {
                            bw.write("不正确\n");
                            bw.flush();
                        }
                        stat.close();
                        con.close();
                    } catch (SQLException e2) {
                        bw.write("失败\n");
                        bw.flush();
                    }

                } else if (m.equals("开始注册")) {
//                    System.out.println("开始注册");
                    bw.write("开始注册\n");
                    bw.flush();
                } else if (m.equals("注册")) {
                    m = br.readLine();
                    m = m.replace("\n", "");
                    String[] str = m.split(";");
//                    String cmd = "create table yhxx (zh varchar(8),mm varchar(20),nc varchar(30),xb varchar(20),tx varchar(2))";
                    String sql = "insert into yhxx values ('" + str[0] + "','" + str[1] + "','" + str[2] + "','" + str[3] + "','" + str[4] + "')";

                    //连接数据库存入用户信息
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        //数据库连接
						

                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_chat?characterEncoding=utf-8&useUnicode=true&useSSL=false", "root", "123456");
                        Statement stat = con.createStatement();
//                        stat.executeUpdate(cmd);
                        int i = stat.executeUpdate(sql);
                        if (i > 0) {
                            bw.write("注册成功\n");
                        } else {
                            bw.write("注册失败\n");
                        }
                    } catch (SQLException e2) {
                        bw.write("注册失败\n");
                    }
                    bw.flush();
                } else if (m.equals("编辑信息")) {
                    m = br.readLine();

                    bw.write("开始编辑\n");
                    bw.write(m + "\n");
                    bw.flush();
                } else if (m.equals("修改")) {
                    m = br.readLine();
                    m.replace("\n", "");
                    String[] str = m.split(";");
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                    } catch (ClassNotFoundException e1) {
                    }

                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_chat?characterEncoding=utf-8&useUnicode=true&useSSL=false", "root", "123456");
                        Statement stat = con.createStatement();
                        String sql1 = "update yhxx set mm='" + str[1] + "',nc='" + str[2] + "',xb='" + str[3] + "',tx='" + str[4] + "' where zh='" + str[0]+"'";
                        stat.executeUpdate(sql1);
                        bw.write("修改成功\n");
                        stat.close();
                        con.close();
                    } catch (SQLException e2) {
                        bw.write("修改失败\n");
                    }
                    bw.flush();


                } else if (m.equals("修改结束")) {
                    m = br.readLine();
                    m = m.replace("\n", "");
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                    } catch (ClassNotFoundException e1) {
                    }

                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_chat?characterEncoding=utf-8&useUnicode=true&useSSL=false", "root", "123456");
                        Statement stat = con.createStatement();
                        ResultSet s = stat.executeQuery("select * from yhxx where zh='" + m.trim() + "'");
                        if (s.next()) {
                            String zhm = s.getString("zh");
                            String mmm = s.getString("mm");
                            if (m.trim().equals(zhm)) {
                                bw.write("修改结束\n");//发送修改结束返回指示
                                bw.write(m + ";" + mmm + "\n");
                                bw.flush();
                            }
                        }

                        stat.close();
                        con.close();
                    } catch (SQLException e2) {
                    }
                } else if (m.equals("开始聊天")) {
                    m = br.readLine();
                    System.out.println("进入聊天室");
                    bw.write("进入聊天室\n");
                    bw.write(m + "\n");
                    bw.flush();
                } else if (m.equals("好友列表")) {
                    m = br.readLine();
                    System.out.println(m);
                    m.replace("\n", "");
                    Server.list1.add(m);
                    Iterator<String> it = Server.list1.iterator();
                    String man;
                    String mans = "";
                    while (it.hasNext()) {
                        man = it.next() + ";";
                        mans += man;
                    }
                    try {
                        bw.write("好友列表\n");
                        bw.write(m + ":" + mans + "\n");
                        bw.flush();
                    } catch (Exception ex) {

                    }

                } else if (m.equals("用户进入")) {
                    m = br.readLine();
                    System.out.println(m);
                    for (int i = 0; i < Server.sockets.size(); i++) {
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Server.sockets.get(i).getOutputStream()));
                        bufferedWriter.write("用户进入\n");
                        bufferedWriter.write(m + "\n");
                        bufferedWriter.flush();
                    }
                } else if (m.equals("私聊")) {
                    m = br.readLine();
                    m.replace("\n", "");
                    String[] str1 = m.split(";");//将用户信息与消息内容分开
                    System.out.println(str1[1]);
                    String[] str2 = str1[0].split(":");

                    for (int i = 0; i < Server.list1.size(); i++) {
                        String s = Server.list1.get(i);
                       // String[] n = s.split("\\(");
                        if (str2[1].equals(s)) {
                            try {
                                bw.write("私聊\n");
                                bw.write(str1[1] + "\n");
                                bw.flush();

                                Socket soc = Server.sockets.get(i);
                                BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
                                bwr.write("私聊\n");
                                bwr.write(str1[1] + "\n");
                                bwr.flush();

                                break;
                            } catch (Exception ex) {

                            }
                        }
                    }
                } else if (m.equals("群聊")) {
                    m = br.readLine();

                    for (int i = 0; i < Server.sockets.size(); i++) {
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Server.sockets.get(i).getOutputStream()));
                        bufferedWriter.write("群聊\n");
                        bufferedWriter.write(m + "\n");
                        bufferedWriter.flush();
                    }

//                    FileOutputStream fileOutputStream = new FileOutputStream(Server.file);
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter("his.txt",true);

                        fw.write(m+"\n");

                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println(m);
                }
                if (m.equals("刷新")) {
                    Iterator<String> it = Server.list1.iterator();
                    String man;
                    String mans = "";
                    while (it.hasNext()) {
                        man = it.next() + ";";
                        mans += man;
                    }

                    bw.write("刷新\n");
                    bw.write(mans + "\n");
                    bw.flush();

                }
                if (m.equals("下线")) {
                    m = br.readLine();
                    System.out.println(m);

                    m = m.replace("\n", "");
                    String[] str = m.split(":");
                    Iterator<String> it = Server.list1.iterator();
                    while (it.hasNext()) {
                        String s = it.next();
                        if (s.equals(str[0])) {
                            Server.list1.remove(s);
                            Server.sockets.remove(sc);
                            break;
                        }
                    }

                    String man;
                    String mans = "";
                    for (int i = 0; i < Server.list1.size(); i++) {
                        man = Server.list1.get(i) + ";";
                        mans += man;
                    }
                    for (int i = 0; i < Server.sockets.size(); i++) {
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Server.sockets.get(i).getOutputStream()));

//                        bufferedWriter.write("下线\n");
//                        bufferedWriter.write(m + "\n");
//                        bufferedWriter.flush();

                        bufferedWriter.write("刷新\n");
                        bufferedWriter.write(mans + "\n");
                        bufferedWriter.flush();
                    }

                    sc.close();
                    break;
                }

                if (m.equals("历史记录")) {
                    m = "";
                    String t=null;
                    try{
                        FileReader f1 = new FileReader("his.txt");
                        BufferedReader br=new BufferedReader(f1);
                        while ((t= br.readLine())!= null) {
                            m += t;
                            m +=";";
                        }
                        f1.close();
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    bw.write("历史记录\n");
                    bw.write(m+"\n");
                    bw.flush();
                }
            }

        } catch (Exception e) {
//            e.printStackTrace();
            Server.sockets.remove(sc);
        }
    }

}	