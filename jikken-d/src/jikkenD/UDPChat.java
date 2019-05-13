package jikkenD;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class UDPChat implements Runnable {
    // 受信用のスレッド
    Thread th;
    JButton button = null;
    JTextArea ja = null;
    JScrollPane sc = null;
    JTextField tf = null;
    JFrame jf = null;
    // 受信ソケットインスタンス
    PacketReceiver pr = null;
    PacketSender ps = null;

    public static void main(String[] args) throws SocketException {
        UDPChat ud = new UDPChat(20000);
    }

    public UDPChat(int portNum) throws SocketException {
        UICreate();
        pr = new PacketReceiver(portNum);
        ps = new PacketSender();
        th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        ja.append(pr.stayAndGetReceive());
        this.run();
    }

    // UIの作成メソッド
    private void UICreate() {
        // フレームの作成
        button = new JButton("送信ボタン");
        ja = new JTextArea(6, 30);
        sc = new JScrollPane(ja);
        tf = new JTextField();
        jf = new JFrame("SampleUI");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(500, 400);
        jf.setVisible(true);

        tf.setBorder(new TitledBorder("入力"));
        jf.getContentPane().add(tf, BorderLayout.NORTH);

        sc.setBorder(new TitledBorder("受信"));
        jf.getContentPane().add(sc, BorderLayout.CENTER);

        jf.getContentPane().add(button, BorderLayout.SOUTH);

        //ボタンリスナーの 追加
        button.addActionListener(new MyListener());
    }

    // ボタンのリスナー
    class MyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            String jaTxt = tf.getText() + "\n";
            tf.setText("");
            ps.sendText(jaTxt);
        }
    }

    // 受信ソケットクラス
    class PacketReceiver {
        // 受信ソケット
        DatagramSocket rcds = null;

        // socketNum : ソケットのローカルアドレス
        public PacketReceiver(int socketNum) throws SocketException {
            rcds = new DatagramSocket(socketNum);
        }

        public String stayAndGetReceive() {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                rcds.receive(packet);
                return new String(Arrays.copyOf(packet.getData(), packet.getLength()), "UTF-8");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("受信に失敗したよ");
                return null;
            }
        }

        public void endSocket() {
            rcds.close();
        }
    }

    // 送信ソケットクラス
    class PacketSender {
        // 送信ソケット
        DatagramSocket sds;

        public PacketSender() throws SocketException {
            sds = null;
            sds = new DatagramSocket();
        }

        private void sendText(String sendStr) {
            // TODO Auto-generated method stub
            try {
                byte[] buffer = sendStr.getBytes("UTF-8");
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, new InetSocketAddress("255.255.255.255", 20000));
                sds.send(packet);
                System.out.println("送信したよ");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void endSocket() {
            sds.close();
        }
    }
}
