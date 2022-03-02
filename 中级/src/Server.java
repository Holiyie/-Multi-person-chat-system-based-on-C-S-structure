


import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.*;

public class Server {

    static ServerSocket server;
    static List<Socket> sockets = new ArrayList<>();//�û��̼߳���
    static ArrayList<String> list1 = new ArrayList<String>();//�û��б���
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
            //System.out.println("��������\n");
            br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));

            while ((msg = br.readLine()) != null) {
                System.out.println(msg);

                String m = msg.replace("\n", "");
                if (m.equals("��¼")) {

//                    System.out.println("��ʼ��¼");
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
                            System.out.println("��¼�ɹ�");
                            bw.write("�ɹ�\n");//���͵�¼�ɹ���ʶ
//                            bw.flush();
                            bw.write(zhm + ";" + mmm + ";" + xbm + ";" + ncm + ";" + txm + "\n");//�����û��������룬�Ա��ǳ����ڽ���������Ϣ����
                            bw.flush();
                        } else {
                            bw.write("����ȷ\n");
                            bw.flush();
                        }
                        stat.close();
                        con.close();
                    } catch (SQLException e2) {
                        bw.write("ʧ��\n");
                        bw.flush();
                    }

                } else if (m.equals("��ʼע��")) {
//                    System.out.println("��ʼע��");
                    bw.write("��ʼע��\n");
                    bw.flush();
                } else if (m.equals("ע��")) {
                    m = br.readLine();
                    m = m.replace("\n", "");
                    String[] str = m.split(";");
//                    String cmd = "create table yhxx (zh varchar(8),mm varchar(20),nc varchar(30),xb varchar(20),tx varchar(2))";
                    String sql = "insert into yhxx values ('" + str[0] + "','" + str[1] + "','" + str[2] + "','" + str[3] + "','" + str[4] + "')";

                    //�������ݿ�����û���Ϣ
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        //���ݿ�����
						

                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_chat?characterEncoding=utf-8&useUnicode=true&useSSL=false", "root", "123456");
                        Statement stat = con.createStatement();
//                        stat.executeUpdate(cmd);
                        int i = stat.executeUpdate(sql);
                        if (i > 0) {
                            bw.write("ע��ɹ�\n");
                        } else {
                            bw.write("ע��ʧ��\n");
                        }
                    } catch (SQLException e2) {
                        bw.write("ע��ʧ��\n");
                    }
                    bw.flush();
                } else if (m.equals("�༭��Ϣ")) {
                    m = br.readLine();

                    bw.write("��ʼ�༭\n");
                    bw.write(m + "\n");
                    bw.flush();
                } else if (m.equals("�޸�")) {
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
                        bw.write("�޸ĳɹ�\n");
                        stat.close();
                        con.close();
                    } catch (SQLException e2) {
                        bw.write("�޸�ʧ��\n");
                    }
                    bw.flush();


                } else if (m.equals("�޸Ľ���")) {
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
                                bw.write("�޸Ľ���\n");//�����޸Ľ�������ָʾ
                                bw.write(m + ";" + mmm + "\n");
                                bw.flush();
                            }
                        }

                        stat.close();
                        con.close();
                    } catch (SQLException e2) {
                    }
                } else if (m.equals("��ʼ����")) {
                    m = br.readLine();
                    System.out.println("����������");
                    bw.write("����������\n");
                    bw.write(m + "\n");
                    bw.flush();
                } else if (m.equals("�����б�")) {
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
                        bw.write("�����б�\n");
                        bw.write(m + ":" + mans + "\n");
                        bw.flush();
                    } catch (Exception ex) {

                    }

                } else if (m.equals("�û�����")) {
                    m = br.readLine();
                    System.out.println(m);
                    for (int i = 0; i < Server.sockets.size(); i++) {
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Server.sockets.get(i).getOutputStream()));
                        bufferedWriter.write("�û�����\n");
                        bufferedWriter.write(m + "\n");
                        bufferedWriter.flush();
                    }
                } else if (m.equals("˽��")) {
                    m = br.readLine();
                    m.replace("\n", "");
                    String[] str1 = m.split(";");//���û���Ϣ����Ϣ���ݷֿ�
                    System.out.println(str1[1]);
                    String[] str2 = str1[0].split(":");

                    for (int i = 0; i < Server.list1.size(); i++) {
                        String s = Server.list1.get(i);
                       // String[] n = s.split("\\(");
                        if (str2[1].equals(s)) {
                            try {
                                bw.write("˽��\n");
                                bw.write(str1[1] + "\n");
                                bw.flush();

                                Socket soc = Server.sockets.get(i);
                                BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
                                bwr.write("˽��\n");
                                bwr.write(str1[1] + "\n");
                                bwr.flush();

                                break;
                            } catch (Exception ex) {

                            }
                        }
                    }
                } else if (m.equals("Ⱥ��")) {
                    m = br.readLine();

                    for (int i = 0; i < Server.sockets.size(); i++) {
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Server.sockets.get(i).getOutputStream()));
                        bufferedWriter.write("Ⱥ��\n");
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
                if (m.equals("ˢ��")) {
                    Iterator<String> it = Server.list1.iterator();
                    String man;
                    String mans = "";
                    while (it.hasNext()) {
                        man = it.next() + ";";
                        mans += man;
                    }

                    bw.write("ˢ��\n");
                    bw.write(mans + "\n");
                    bw.flush();

                }
                if (m.equals("����")) {
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

//                        bufferedWriter.write("����\n");
//                        bufferedWriter.write(m + "\n");
//                        bufferedWriter.flush();

                        bufferedWriter.write("ˢ��\n");
                        bufferedWriter.write(mans + "\n");
                        bufferedWriter.flush();
                    }

                    sc.close();
                    break;
                }

                if (m.equals("��ʷ��¼")) {
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

                    bw.write("��ʷ��¼\n");
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