package jikkenD;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class SimpleBrowser {
    JButton button = null;
    JTextArea ja = null;
    JScrollPane sc = null;
    JTextField tf = null;
    JFrame jf = null;

    public static void main(String[] args) {
        SimpleBrowser ud = new SimpleBrowser();
    }

    public SimpleBrowser() {
        UICreate();
    }

    // UIの作成メソッド
    private void UICreate() {
        // フレームの作成
        button = new JButton("読み込み");
        ja = new JTextArea(6, 30);
        sc = new JScrollPane(ja);
        tf = new JTextField();
        jf = new JFrame("SimpleBrowser");
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
            String jaTxt = tf.getText();
            tf.setText("");
            try {
                TCPCommunication tcpCom = new TCPCommunication(jaTxt, 80);
                ja.append(tcpCom.sendRequestAndGetResponse());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // TCP通信クラス
    class TCPCommunication {
        //
        Socket sock;
        int port;

        public TCPCommunication(String host, int port) throws IOException {
            sock = new Socket(host, port);
            this.port = port;
        }

        public String sendRequestAndGetResponse() {
            // TODO Auto-generated method stub
            try {
                //送信ストリームの取得
                OutputStream out = sock.getOutputStream();
                //送信データ
                String sendData = "GET　/index.html HTTP/1.0\nHost:\n";
                //文字列をUTF-8形式のバイト配列に変換して送信
                out.write(sendData.getBytes("UTF-8"));
                //送信データの表示
                System.out.println("「"+sendData+"」を送信しました。");

                // 受信処理
                byte[] buffer = new byte[1024];
                InputStream in = sock.getInputStream();
                int bufSize = in.read(buffer);
                String receivedStr = new String(Arrays.copyOf(buffer, bufSize), "UTF-8");
                System.out.println(receivedStr);
                in.close();
                out.close();
                sock.close();
                return receivedStr;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
    }
}
