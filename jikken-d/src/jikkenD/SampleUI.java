package jikkenD;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class SampleUI {
	JButton button = null;
	JTextArea ja = null;
	JScrollPane sc = null;
	JTextField tf = null;
	JFrame jf = null;

	public SampleUI() {
		UICreate();
	}

	// UI�̍쐬���\�b�h
	private void UICreate() {
		// �t���[���̍쐬
		button = new JButton("���̓{�^��");
		ja = new JTextArea(6, 30);
		sc = new JScrollPane(ja);
		tf = new JTextField();
		jf = new JFrame("SampleUI");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(320, 240);
		jf.setVisible(true);

		tf.setBorder(new TitledBorder("����"));
		jf.getContentPane().add(tf, BorderLayout.NORTH);

		sc.setBorder(new TitledBorder("�o��"));
		jf.getContentPane().add(sc, BorderLayout.CENTER);

		jf.getContentPane().add(button, BorderLayout.SOUTH);

		//�{�^�����X�i�[�� �ǉ�
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ja.append(tf.getText() + "\n");
				tf.setText("");
			}
		});
		button.addActionListener(new MyListener());
	}

	// �{�^���̃��X�i�[
	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			ja.append(tf.getText() + "22222\n");
			tf.setText("");
		}

	}
}
