package jikkenD;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class UDPChat implements Runnable {
	// ��M�p�̃X���b�h
	Thread th;
	JButton button = null;
	JTextArea ja = null;
	JScrollPane sc = null;
	JTextField tf = null;
	JFrame jf = null;
	// ��M�\�P�b�g�C���X�^���X
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

	// UI�̍쐬���\�b�h
	private void UICreate() {
		// �t���[���̍쐬
		button = new JButton("���M�{�^��");
		ja = new JTextArea(6, 30);
		sc = new JScrollPane(ja);
		tf = new JTextField();
		jf = new JFrame("SampleUI");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(500, 400);
		jf.setVisible(true);

		tf.setBorder(new TitledBorder("����"));
		jf.getContentPane().add(tf, BorderLayout.NORTH);

		sc.setBorder(new TitledBorder("��M"));
		jf.getContentPane().add(sc, BorderLayout.CENTER);

		jf.getContentPane().add(button, BorderLayout.SOUTH);

		//�{�^�����X�i�[�� �ǉ�
		button.addActionListener(new MyListener());
	}

	// �{�^���̃��X�i�[
	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String jaTxt = tf.getText() + "\n";
			tf.setText("");
			ps.sendText(jaTxt);
		}
	}
	
	// ��M�\�P�b�g�N���X
	class PacketReceiver {
		// ��M�\�P�b�g
		DatagramSocket rcds = null;
		
		// socketNum : �\�P�b�g�̃��[�J���A�h���X
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
				System.out.println("��M�Ɏ��s������");
				return null;
			}
		}
		
		public void endSocket() {
			rcds.close();
		}
	}
	
	// ���M�\�P�b�g�N���X
	class PacketSender {
		// ���M�\�P�b�g
		DatagramSocket sds = null;
		String sendStr;
		
		public PacketSender() throws SocketException {
			sds = new DatagramSocket();
		}
		
		private void sendText(String sendStr) {
			// TODO Auto-generated method stub
			try {
				byte[] buffer = sendStr.getBytes("UTF-8");
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, new InetSocketAddress("255.255.255.255", 20000));
				sds.send(packet);
				System.out.println("���M������");
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
