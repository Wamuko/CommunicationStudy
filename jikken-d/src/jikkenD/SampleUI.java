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

	// UIの作成メソッド
	private void UICreate() {
		// フレームの作成
		button = new JButton("入力ボタン");
		ja = new JTextArea(6, 30);
		sc = new JScrollPane(ja);
		tf = new JTextField();
		jf = new JFrame("SampleUI");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(320, 240);
		jf.setVisible(true);

		tf.setBorder(new TitledBorder("入力"));
		jf.getContentPane().add(tf, BorderLayout.NORTH);

		sc.setBorder(new TitledBorder("出力"));
		jf.getContentPane().add(sc, BorderLayout.CENTER);

		jf.getContentPane().add(button, BorderLayout.SOUTH);

		//ボタンリスナーの 追加
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ja.append(tf.getText() + "\n");
				tf.setText("");
			}
		});
		button.addActionListener(new MyListener());
	}

	// ボタンのリスナー
	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			ja.append(tf.getText() + "22222\n");
			tf.setText("");
		}

	}
}
