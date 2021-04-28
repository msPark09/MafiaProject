package com.mafia.utility;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;

import java.awt.Label;
import java.awt.Panel;

import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import com.mafia.client.GameTimer;

public class GameFrame extends JFrame implements WindowListener {
	ImageIcon im; // Ÿ��Ʋ

	// beforFrame
	public JTextField idInput; // �г��� �Է�â
	public JLabel info; // �г��� Ȯ�� â
	public JButton okayBtn; // �г��� Ȯ�ι�ư
	public JButton bfokbtn; // �г��� Ȯ���ϰ� ���� �����ϱ� ��ư

	// mainFrame
	public JTextArea chatWin; // ä��â
	public JTextField chat; // ä�� �Է�â
	public JTextArea sysMsg; // ���� ��������
	public JButton send; // ������ ��ư
	public JButton whisper; // �ӼӸ� ��ư
	public JButton voteBtn; // ��ǥ ��ư
	public JLabel userName; // username
	public JTextArea userList; // userlist
	public JButton jobName; // jobimg ���� ����&ǥ�� ��ư
	public GameTimer timer; // Ÿ�̸�
	public JPanel userP; // �����̹����ǳ�

	// votFrame
	public JButton sendBtn; // ��ǥ ������
	public JPanel checkP; // üũ�ڽ��� ���� �ǳ�

	// ���ȭ��
	ImageIcon iconMain, iconVot, iconBefor;// ����̹���,��ǥâ����̹���

	public GameFrame() {
		im = new ImageIcon("./titlelogo.jpg"); // Ÿ��Ʋ�� �̹��� �ֱ�
		this.setIconImage(im.getImage());
		iconMain = new ImageIcon("./blakbg.jpg"); // ��� ȭ�� ���� ���� ��ü
		iconBefor = new ImageIcon("./graybg.jpg");
		iconVot = new ImageIcon("./votep.png"); // ��ǥ�� ��� ȭ�� ����

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		setSize(750, 460);
		setLocation((int) ((screen.getWidth() / 2) - (this.getWidth() / 2)),
				(int) ((screen.getHeight() / 2) - (this.getHeight() / 2)));

		addWindowListener(this);

		setVisible(false);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public GameFrame(String title) {
		this();
		setTitle(title);
	}// Ÿ��Ʋ �ۼ�

	public void setFrameBefore() {// game �� �г����� �����ϴ� setFrame
		setSize(380, 350);
		setVisible(true);
		final ImageIcon icon = new ImageIcon("./graybg.jpg");

		JPanel p = new JPanel(new BorderLayout()) {
			public void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
				super.paintComponent(g);
			}
		};
		// -----------------------------------------------
		// 1. befor �������� ���ӷΰ� �κ�
		JPanel logo = new JPanel(new BorderLayout());

		JPanel empty = new JPanel();
		empty.setOpaque(false);

		JLabel mainlogo = new JLabel(new ImageIcon("./mafialogo.png")); // ���Ǿư��ӷΰ�
		info = new JLabel("���ӿ� ����� �г����� �Է��ϼ���");
		info.setHorizontalAlignment(JLabel.CENTER);
		logo.add(empty, BorderLayout.NORTH);
		logo.add(mainlogo, BorderLayout.CENTER);
		logo.add(info, BorderLayout.SOUTH);
		logo.setOpaque(false);

		// -----------------------------------------------
		// 2. befor �������� �г����Է�â
		JLabel id = new JLabel("�г���");
		JPanel nic = new JPanel(); // id panel
		nic.setOpaque(false);

		idInput = new JTextField(10); // id�Է�â
		okayBtn = new JButton(new ImageIcon("./idcheck.png")); // idüũ��ư
		okayBtn.setPressedIcon(new ImageIcon("./idcheck1.png"));
		okayBtn.setRolloverIcon(new ImageIcon("./idcheck1.png"));
		okayBtn.setOpaque(false);
		okayBtn.setContentAreaFilled(false);
		okayBtn.setBorderPainted(false);

		nic.add(id);
		nic.add(idInput);
		nic.add(okayBtn);

		// -----------------------------------------------
		// 3. befor �������� ��ŸƮ ��ư
		JPanel startpl = new JPanel(); // ��ŸƮpanel
		startpl.setOpaque(false);
		bfokbtn = new JButton(new ImageIcon("./startbtn.png")); // ��ŸƮ��ư
		bfokbtn.setPressedIcon(new ImageIcon("./startbtn1.png"));
		bfokbtn.setContentAreaFilled(false);
		bfokbtn.setBorderPainted(false);
		bfokbtn.setEnabled(false);

		startpl.add(bfokbtn);

		p.add(logo, BorderLayout.NORTH);
		p.add(nic, BorderLayout.CENTER);
		p.add(startpl, BorderLayout.SOUTH);

		add(p);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void setFrameMain() {// game main ȭ�� setFrame

		JPanel mWindow = new JPanel(new BorderLayout()) {
			public void paintComponent(Graphics g) {
				g.drawImage(iconMain.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		}; // ����ȭ��

		// //////////////West layout////////////////////////
		JPanel p1 = new JPanel(new GridBagLayout());
		p1.setOpaque(false);

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.fill = GridBagConstraints.BOTH;

		// ------------------------------------------------
		// 1. ���� �ΰ�
		ImageIcon img = new ImageIcon("./gamelogo.png"); // ���ӷΰ�
		JLabel logo = new JLabel(img);

		JLayeredPane userPN = new JLayeredPane();
		userPN.setOpaque(false);

		// -------------------------------------------------
		// 2. ���� ���� �̹��� �� �г���
		final ImageIcon userPic = new ImageIcon("./mafiapic.png"); // �����̹���
		userP = new JPanel(new GridBagLayout()) {
			public void paintComponent(Graphics g) {
				g.drawImage(userPic.getImage(), 0, 0, null);
				setOpaque(false); // �׸��� ǥ���ϰ� ����,�����ϰ� ����
				super.paintComponent(g);
			}
		};
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		JPanel i = new JPanel();
		i.setOpaque(false);

		userName = new JLabel("username");
		userName.setFont(new Font("���� ���", Font.BOLD, 12));
		userName.setEnabled(false);
		userName.setOpaque(false);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 6.0;
		userP.add(i, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 4.0;
		userP.add(userName, gbc);

		// -------------------------------------------------
		// 3. ���Ǿ� ������ ���Ǿ� or �ù� ǥ��
		jobName = new JButton(new ImageIcon("./mafiasel.png")); // ���ǾƼ�����ư
		jobName.setRolloverIcon(new ImageIcon("./mafiasel1.png")); // �ѿ�����
		jobName.setPressedIcon(new ImageIcon("./mafiasel2.png")); // ������
		jobName.setContentAreaFilled(false); // ��������ȭ
		jobName.setOpaque(false);// ����ȭ
		jobName.setFocusPainted(false);
		jobName.setBorderPainted(false);

		// -------------------------------------------------
		// 4. ����

		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.weightx = 1.0;
		gbc1.weighty = 0.1;
		p1.add(logo, gbc1); // ���ӷΰ�

		gbc1.gridx = 0;
		gbc1.gridy = 1;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.weightx = 1.0;
		gbc1.weighty = 5.0;
		p1.add(userP, gbc1); // �÷��̾� �̹���

		gbc1.gridx = 0;
		gbc1.gridy = 2;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.weightx = 1.0;
		gbc1.weighty = 0.1;
		p1.add(jobName, gbc1); // ����ǥ�ö�

		// //////////////Center layout////////////////////////
		JPanel p2 = new JPanel(new BorderLayout());// p3+chatting
		p2.setOpaque(false);

		JPanel p3 = new JPanel(new BorderLayout()); // statChat,listvote
		p3.setOpaque(false);

		JPanel statChatWin = new JPanel(new GridBagLayout()); // ����+ä��â
		statChatWin.setOpaque(false);
		JPanel listVote = new JPanel(new GridBagLayout()); // ����Ʈ+��ǥâ
		listVote.setOpaque(false);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.fill = GridBagConstraints.HORIZONTAL;

		chatWin = new JTextArea(20, 20); // ä��view
		chatWin.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(chatWin,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // ��ũ��
		chatWin.setEnabled(false); // �ۼ�����

		sysMsg = new JTextArea(1, 30); // ����â(���Դϴ�, ���Դϴ�)
		sysMsg.setEnabled(false); // �ۼ�����
		JPanel statWin = new JPanel();
		statWin.setOpaque(false);
		statWin.add(sysMsg);

		gbc2.gridx = 0;
		gbc2.gridy = 0;
		gbc2.gridwidth = 1;
		gbc2.gridheight = 1;
		gbc2.weightx = 1.0;
		gbc2.weighty = 1.0;
		statChatWin.add(statWin, gbc2); // ����â

		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 1;
		gbc2.gridheight = 1;
		gbc2.weightx = 1.0;
		gbc2.weighty = 1.0;
		statChatWin.add(scrollPane, gbc2); // ä��â

		JPanel chatPl = new JPanel(new GridBagLayout()); // send��ư�� ������ ä���Է�â �г�
		chatPl.setOpaque(false);
		GridBagConstraints chatgbc = new GridBagConstraints();
		chatgbc.fill = GridBagConstraints.HORIZONTAL;

		chat = new JTextField();
		send = new JButton("send");
		send.setBackground(Color.BLACK);
		send.setFont(new Font("���� ���", Font.BOLD, 12));
		send.setForeground(new Color(133, 0, 0));
		whisper = new JButton("�ӼӸ�");
		 whisper.setBackground(Color.BLACK);
		 whisper.setFont(new Font("���� ���", Font.BOLD, 12));
		 whisper.setForeground(new Color(133, 0, 0));

		chatgbc.gridx = 0;
		chatgbc.gridy = 0;
		chatgbc.gridwidth = 1;
		chatgbc.gridheight = 1;
		chatgbc.weightx = 7.0;
		chatgbc.weighty = 1.0;
		chatPl.add(chat, chatgbc); // chat�Է�â

		chatgbc.gridx = 1;
		chatgbc.gridy = 0;
		chatgbc.gridwidth = 1;
		chatgbc.gridheight = 1;
		chatgbc.weightx = 0.2;
		chatgbc.weighty = 1.0;
		chatPl.add(send, chatgbc); // send��ư

		chatgbc.gridx = 2;
		chatgbc.gridy = 0;
		chatgbc.gridwidth = 1;
		chatgbc.gridheight = 1;
		chatgbc.weightx = 0.1;
		chatgbc.weighty = 1.0;
		chatPl.add(whisper, chatgbc); // �ӼӸ���ư

		// //////////////East layout////////////////////////
		timer = new GameTimer();
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.fill = GridBagConstraints.CENTER;
		ImageIcon img3 = new ImageIcon("./userlist.png");
		JLabel listName = new JLabel(img3);
		userList = new JTextArea(7, 10);
		JScrollPane userLists = new JScrollPane(userList); // ��ũ���� �߰�
		userList.setEnabled(false); // ��������Ʈ �ۼ�����

		voteBtn = new JButton(new ImageIcon("./votebox.png")); // ��ǥ��ư
		voteBtn.setPressedIcon(new ImageIcon("./votebox2.png"));
		voteBtn.setRolloverIcon(new ImageIcon("./votebox1.png"));
		voteBtn.setContentAreaFilled(false); // ��������ȭ
		voteBtn.setOpaque(false);// ����ȭ
		voteBtn.setFocusPainted(false);
		voteBtn.setBorderPainted(false);
		voteBtn.setSize(50, 50);

		gbc3.gridx = 0;
		gbc3.gridy = 0;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1.0;
		gbc3.weighty = 1.0;
		listVote.add(timer.p1, gbc3); // Ÿ�̸�

		gbc3.gridx = 0;
		gbc3.gridy = 1;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1.0;
		gbc3.weighty = 0.1;
		listVote.add(listName, gbc3); // ��������Ʈ(���Ӷ�)

		gbc3.gridx = 0;
		gbc3.gridy = 2;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1.0;
		gbc3.weighty = 1.0;
		listVote.add(userLists, gbc3); // ���� ����Ʈâ

		gbc3.gridx = 0;
		gbc3.gridy = 3;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1.0;
		gbc3.weighty = 1.0;
		listVote.add(voteBtn, gbc3); // ��ǥ��ư

		p3.add(statChatWin, BorderLayout.CENTER);
		p3.add(listVote, BorderLayout.EAST);

		p2.add(p3, BorderLayout.CENTER);
		p2.add(chatPl, BorderLayout.SOUTH);

		// //////////////East layout////////////////////////

		mWindow.add(p1, BorderLayout.WEST);
		mWindow.add(p2, BorderLayout.CENTER);

		add(mWindow);

	}

	public void setFrameVoting() { // ��ǥâ�� ���� setFrame

		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.black);

		JLabel title = new JLabel(new ImageIcon("./whosel.png"));
		title.setHorizontalAlignment(JLabel.CENTER);

		JPanel btnPan = new JPanel();
		btnPan.setOpaque(false);
		sendBtn = new JButton(new ImageIcon("./sendBtn.png"));
		sendBtn.setPressedIcon(new ImageIcon("./sendBtn1.png"));
		sendBtn.setRolloverIcon(new ImageIcon("./sendBtn1.png"));
		sendBtn.setContentAreaFilled(false); // ��������ȭ
		sendBtn.setOpaque(false);// ����ȭ
		sendBtn.setFocusPainted(false);
		sendBtn.setBorderPainted(false);
		btnPan.add(sendBtn);

		checkP = new JPanel(); // ��ǥ��������Ʈ (���ǳ�)
		checkP.setOpaque(false);

		p.add(title, BorderLayout.NORTH);
		p.add(checkP, BorderLayout.CENTER);
		p.add(btnPan, BorderLayout.SOUTH);
		add(p);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

	}// setFrame

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

		// dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}