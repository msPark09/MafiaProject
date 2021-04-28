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
	ImageIcon im; // 타이틀

	// beforFrame
	public JTextField idInput; // 닉네임 입력창
	public JLabel info; // 닉네임 확인 창
	public JButton okayBtn; // 닉네임 확인버튼
	public JButton bfokbtn; // 닉네임 확인하고 게임 시작하기 버튼

	// mainFrame
	public JTextArea chatWin; // 채팅창
	public JTextField chat; // 채팅 입력창
	public JTextArea sysMsg; // 서버 공지사항
	public JButton send; // 보내기 버튼
	public JButton whisper; // 귓속말 버튼
	public JButton voteBtn; // 투표 버튼
	public JLabel userName; // username
	public JTextArea userList; // userlist
	public JButton jobName; // jobimg 직업 선정&표시 버튼
	public GameTimer timer; // 타이머
	public JPanel userP; // 유저이미지판넬

	// votFrame
	public JButton sendBtn; // 투표 보내기
	public JPanel checkP; // 체크박스를 위한 판넬

	// 배경화면
	ImageIcon iconMain, iconVot, iconBefor;// 배경이미지,투표창배경이미지

	public GameFrame() {
		im = new ImageIcon("./titlelogo.jpg"); // 타이틀에 이미지 넣기
		this.setIconImage(im.getImage());
		iconMain = new ImageIcon("./blakbg.jpg"); // 배경 화면 설정 메인 전체
		iconBefor = new ImageIcon("./graybg.jpg");
		iconVot = new ImageIcon("./votep.png"); // 투표함 배경 화면 설정

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
	}// 타이틀 작성

	public void setFrameBefore() {// game 전 닉네임을 설정하는 setFrame
		setSize(380, 350);
		setVisible(true);
		final ImageIcon icon = new ImageIcon("./graybg.jpg");

		JPanel p = new JPanel(new BorderLayout()) {
			public void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		// -----------------------------------------------
		// 1. befor 프레임의 게임로고 부분
		JPanel logo = new JPanel(new BorderLayout());

		JPanel empty = new JPanel();
		empty.setOpaque(false);

		JLabel mainlogo = new JLabel(new ImageIcon("./mafialogo.png")); // 마피아게임로고
		info = new JLabel("게임에 사용할 닉네임을 입력하세요");
		info.setHorizontalAlignment(JLabel.CENTER);
		logo.add(empty, BorderLayout.NORTH);
		logo.add(mainlogo, BorderLayout.CENTER);
		logo.add(info, BorderLayout.SOUTH);
		logo.setOpaque(false);

		// -----------------------------------------------
		// 2. befor 프레임의 닉네임입력창
		JLabel id = new JLabel("닉네임");
		JPanel nic = new JPanel(); // id panel
		nic.setOpaque(false);

		idInput = new JTextField(10); // id입력창
		okayBtn = new JButton(new ImageIcon("./idcheck.png")); // id체크버튼
		okayBtn.setPressedIcon(new ImageIcon("./idcheck1.png"));
		okayBtn.setRolloverIcon(new ImageIcon("./idcheck1.png"));
		okayBtn.setOpaque(false);
		okayBtn.setContentAreaFilled(false);
		okayBtn.setBorderPainted(false);

		nic.add(id);
		nic.add(idInput);
		nic.add(okayBtn);

		// -----------------------------------------------
		// 3. befor 프레임의 스타트 버튼
		JPanel startpl = new JPanel(); // 스타트panel
		startpl.setOpaque(false);
		bfokbtn = new JButton(new ImageIcon("./startbtn.png")); // 스타트버튼
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

	public void setFrameMain() {// game main 화면 setFrame

		JPanel mWindow = new JPanel(new BorderLayout()) {
			public void paintComponent(Graphics g) {
				g.drawImage(iconMain.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		}; // 메인화면

		// //////////////West layout////////////////////////
		JPanel p1 = new JPanel(new GridBagLayout());
		p1.setOpaque(false);

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.fill = GridBagConstraints.BOTH;

		// ------------------------------------------------
		// 1. 게임 로고
		ImageIcon img = new ImageIcon("./gamelogo.png"); // 게임로고
		JLabel logo = new JLabel(img);

		JLayeredPane userPN = new JLayeredPane();
		userPN.setOpaque(false);

		// -------------------------------------------------
		// 2. 게임 유저 이미지 와 닉네임
		final ImageIcon userPic = new ImageIcon("./mafiapic.png"); // 유저이미지
		userP = new JPanel(new GridBagLayout()) {
			public void paintComponent(Graphics g) {
				g.drawImage(userPic.getImage(), 0, 0, null);
				setOpaque(false); // 그림을 표시하게 설정,투명하게 조절
				super.paintComponent(g);
			}
		};
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		JPanel i = new JPanel();
		i.setOpaque(false);

		userName = new JLabel("username");
		userName.setFont(new Font("맑은 고딕", Font.BOLD, 12));
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
		// 3. 마피아 선정과 마피아 or 시민 표시
		jobName = new JButton(new ImageIcon("./mafiasel.png")); // 마피아선정버튼
		jobName.setRolloverIcon(new ImageIcon("./mafiasel1.png")); // 롤오버시
		jobName.setPressedIcon(new ImageIcon("./mafiasel2.png")); // 누를때
		jobName.setContentAreaFilled(false); // 영역투명화
		jobName.setOpaque(false);// 투명화
		jobName.setFocusPainted(false);
		jobName.setBorderPainted(false);

		// -------------------------------------------------
		// 4. 적용

		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.weightx = 1.0;
		gbc1.weighty = 0.1;
		p1.add(logo, gbc1); // 게임로고

		gbc1.gridx = 0;
		gbc1.gridy = 1;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.weightx = 1.0;
		gbc1.weighty = 5.0;
		p1.add(userP, gbc1); // 플레이어 이미지

		gbc1.gridx = 0;
		gbc1.gridy = 2;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		gbc1.weightx = 1.0;
		gbc1.weighty = 0.1;
		p1.add(jobName, gbc1); // 직업표시란

		// //////////////Center layout////////////////////////
		JPanel p2 = new JPanel(new BorderLayout());// p3+chatting
		p2.setOpaque(false);

		JPanel p3 = new JPanel(new BorderLayout()); // statChat,listvote
		p3.setOpaque(false);

		JPanel statChatWin = new JPanel(new GridBagLayout()); // 상태+채팅창
		statChatWin.setOpaque(false);
		JPanel listVote = new JPanel(new GridBagLayout()); // 리스트+투표창
		listVote.setOpaque(false);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.fill = GridBagConstraints.HORIZONTAL;

		chatWin = new JTextArea(20, 20); // 채팅view
		chatWin.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(chatWin,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // 스크롤
		chatWin.setEnabled(false); // 작성금지

		sysMsg = new JTextArea(1, 30); // 상태창(낮입니다, 밤입니다)
		sysMsg.setEnabled(false); // 작성금지
		JPanel statWin = new JPanel();
		statWin.setOpaque(false);
		statWin.add(sysMsg);

		gbc2.gridx = 0;
		gbc2.gridy = 0;
		gbc2.gridwidth = 1;
		gbc2.gridheight = 1;
		gbc2.weightx = 1.0;
		gbc2.weighty = 1.0;
		statChatWin.add(statWin, gbc2); // 상태창

		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 1;
		gbc2.gridheight = 1;
		gbc2.weightx = 1.0;
		gbc2.weighty = 1.0;
		statChatWin.add(scrollPane, gbc2); // 채팅창

		JPanel chatPl = new JPanel(new GridBagLayout()); // send버튼을 포함한 채팅입력창 패널
		chatPl.setOpaque(false);
		GridBagConstraints chatgbc = new GridBagConstraints();
		chatgbc.fill = GridBagConstraints.HORIZONTAL;

		chat = new JTextField();
		send = new JButton("send");
		send.setBackground(Color.BLACK);
		send.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		send.setForeground(new Color(133, 0, 0));
		whisper = new JButton("귓속말");
		 whisper.setBackground(Color.BLACK);
		 whisper.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		 whisper.setForeground(new Color(133, 0, 0));

		chatgbc.gridx = 0;
		chatgbc.gridy = 0;
		chatgbc.gridwidth = 1;
		chatgbc.gridheight = 1;
		chatgbc.weightx = 7.0;
		chatgbc.weighty = 1.0;
		chatPl.add(chat, chatgbc); // chat입력창

		chatgbc.gridx = 1;
		chatgbc.gridy = 0;
		chatgbc.gridwidth = 1;
		chatgbc.gridheight = 1;
		chatgbc.weightx = 0.2;
		chatgbc.weighty = 1.0;
		chatPl.add(send, chatgbc); // send버튼

		chatgbc.gridx = 2;
		chatgbc.gridy = 0;
		chatgbc.gridwidth = 1;
		chatgbc.gridheight = 1;
		chatgbc.weightx = 0.1;
		chatgbc.weighty = 1.0;
		chatPl.add(whisper, chatgbc); // 귓속말버튼

		// //////////////East layout////////////////////////
		timer = new GameTimer();
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.fill = GridBagConstraints.CENTER;
		ImageIcon img3 = new ImageIcon("./userlist.png");
		JLabel listName = new JLabel(img3);
		userList = new JTextArea(7, 10);
		JScrollPane userLists = new JScrollPane(userList); // 스크롤판 추가
		userList.setEnabled(false); // 유저리스트 작성금지

		voteBtn = new JButton(new ImageIcon("./votebox.png")); // 투표버튼
		voteBtn.setPressedIcon(new ImageIcon("./votebox2.png"));
		voteBtn.setRolloverIcon(new ImageIcon("./votebox1.png"));
		voteBtn.setContentAreaFilled(false); // 영역투명화
		voteBtn.setOpaque(false);// 투명화
		voteBtn.setFocusPainted(false);
		voteBtn.setBorderPainted(false);
		voteBtn.setSize(50, 50);

		gbc3.gridx = 0;
		gbc3.gridy = 0;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1.0;
		gbc3.weighty = 1.0;
		listVote.add(timer.p1, gbc3); // 타이머

		gbc3.gridx = 0;
		gbc3.gridy = 1;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1.0;
		gbc3.weighty = 0.1;
		listVote.add(listName, gbc3); // 유저리스트(네임라벨)

		gbc3.gridx = 0;
		gbc3.gridy = 2;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1.0;
		gbc3.weighty = 1.0;
		listVote.add(userLists, gbc3); // 유저 리스트창

		gbc3.gridx = 0;
		gbc3.gridy = 3;
		gbc3.gridwidth = 1;
		gbc3.gridheight = 1;
		gbc3.weightx = 1.0;
		gbc3.weighty = 1.0;
		listVote.add(voteBtn, gbc3); // 투표버튼

		p3.add(statChatWin, BorderLayout.CENTER);
		p3.add(listVote, BorderLayout.EAST);

		p2.add(p3, BorderLayout.CENTER);
		p2.add(chatPl, BorderLayout.SOUTH);

		// //////////////East layout////////////////////////

		mWindow.add(p1, BorderLayout.WEST);
		mWindow.add(p2, BorderLayout.CENTER);

		add(mWindow);

	}

	public void setFrameVoting() { // 투표창을 위한 setFrame

		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.black);

		JLabel title = new JLabel(new ImageIcon("./whosel.png"));
		title.setHorizontalAlignment(JLabel.CENTER);

		JPanel btnPan = new JPanel();
		btnPan.setOpaque(false);
		sendBtn = new JButton(new ImageIcon("./sendBtn.png"));
		sendBtn.setPressedIcon(new ImageIcon("./sendBtn1.png"));
		sendBtn.setRolloverIcon(new ImageIcon("./sendBtn1.png"));
		sendBtn.setContentAreaFilled(false); // 영역투명화
		sendBtn.setOpaque(false);// 투명화
		sendBtn.setFocusPainted(false);
		sendBtn.setBorderPainted(false);
		btnPan.add(sendBtn);

		checkP = new JPanel(); // 투표유저리스트 (빈판넬)
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