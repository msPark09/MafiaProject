package com.mafia.client;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.mafia.utility.GameFrame;

public class MainUI extends Thread implements MouseListener, ActionListener {

	// StreamSet-------------------
	ObjectOutputStream oos; //
	ObjectInputStream ois; //

	PrintWriter pw;
	// -----------------------------
	GameFrame befor;
	GameFrame f;
	GameFrame sub;

	// -------------------------------
	int chekVot = 0; // 투표했나 확인
	int chekMafi = 0; // 마피아인가 확인
	Checkbox[] person; // 투표를 위한 체크박스
	String votPerson; // 투표한 사람

	// Socket------------------------
	Socket mainSocket;
	// Server 연결-----------------------
	String ip = "203.236.209.207";
	int port = 5050;

	// DB연결-----------------------
	String url = "jdbc:oracle:thin:@203.236.209.207:1521:xe";
	String user = "scott";
	String password = "tiger";

	Statement stmt; // 연결상태
	ArrayList nicName; // 닉네임 저장할 어레이 리스트

	String myIp; // 내 아이피
	String userJob = "citizen"; // 내 직업
	static String playerN; // 내 아이디

	public MainUI() {

		befor = new GameFrame("마피아Classic");
		befor.setFrameBefore();

		befor.setVisible(true);
		befor.bfokbtn.addActionListener(this);
		befor.okayBtn.addMouseListener(this);

		f = new GameFrame("마피아Classic");
		f.setFrameMain();
		f.chat.addActionListener(this);
		// f.voteBtn.addMouseListener(this);
		f.voteBtn.addActionListener(this);
		f.send.addMouseListener(this);
		f.jobName.addActionListener(this);
		f.whisper.addActionListener(this);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				try {
					oos.writeObject(playerN + "#exit");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		f.timer.tm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				f.timer.logic();
				if (f.timer.stateDN.equals(f.timer.DAY) && chekVot == 0) {
					f.voteBtn.setEnabled(true);
				} else if (f.timer.stateDN.equals(f.timer.NIGHT) && chekVot > 0) {
					if (chekMafi > 0) {
						f.voteBtn.setEnabled(true);
					}
					chekVot = 0;
				}
			}
		});
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			String sqlSelectAll = "select * from mafiauser";
			ResultSet rs = stmt.executeQuery(sqlSelectAll);
			nicName = new ArrayList();
			nicName.clear();
			while (rs.next()) {
				nicName.add(rs.getString(2));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// // ----------------------------------
		sub = new GameFrame("투표하기");
		sub.setSize(200, 200);
		sub.setFrameVoting();
		sub.sendBtn.addMouseListener(this); // 버튼 이벤트 연결 //

	}// cons end

	// MouseListener
	// Event-------------------------------------------------------
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		// ------------------------------------------------------------------
		// 1. 투표결과 보내기
		if (e.getSource().equals(sub.sendBtn)) { // sub의 버튼 누를시
			int count = 0;
			for (int i = 0; i < person.length; i++) {
				if (person[i].getState()) {
					votPerson = person[i].getLabel();
					count++;
				}
			}
			if (count > 0) {
				try {
					oos.writeObject("!nic" + votPerson);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("투표한 사람 : " + votPerson);
				f.sysMsg.setText("투표를 완료하였습니다.");
				f.chatWin.append("[SYSTEM] : 투표를 완료하였습니다.\n");
				nicName.clear();
				sub.checkP.removeAll();
				f.voteBtn.setEnabled(false);
				sub.dispose(); // sub 종료
			}

			// ------------------------------------------------------------------
			// 2. 닉네임의 중복확인
		} else if (e.getSource().equals(befor.okayBtn)) { // 닉네임 확인
			int index = 0;
			playerN = befor.idInput.getText();
			String sqlSelectAll = "select * from mafiauser";
			ResultSet rs;
			try {
				rs = stmt.executeQuery(sqlSelectAll);
				nicName = new ArrayList();
				nicName.clear();
				while (rs.next()) {
					nicName.add(rs.getString(2));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (playerN.equals("")) {
				befor.info.setText("닉네임이 입력되지 않았습니다. 입력해 주세요.");
				befor.bfokbtn.setEnabled(false);
			} else {
				for (int i = 0; i < nicName.size(); i++) {
					if (playerN.equals(nicName.get(i))) {
						befor.info.setText("닉네임이 중복됩니다. 확인 후 다시 입력해 주세요.");
						befor.bfokbtn.setEnabled(false);
						befor.idInput.setText("");
						break;
					} else {
						index++;
					}
				}
				if (index == nicName.size()) {
					befor.info.setText("닉네임 [" + playerN + "] 으로 게임을 시작합니다.");
					befor.bfokbtn.setEnabled(true);
					befor.okayBtn.setIcon(new ImageIcon("./idcheck1.png"));
				}
			}
			// ------------------------------------------------------------------
			// 3. send버튼 이벤트
		} else if (e.getSource().equals(f.send)) {
			// send버튼 눌렀을 경우 처리법.

			String msg = f.chat.getText();
			if (msg.equals("")) {
			} else {
				try {
					oos.writeObject(playerN + "#" + msg);
					System.out.println("메시지출력: " + msg);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				f.chat.setText("");
			}// inner if end
		}// outer if end
	}// mouse cliked end.

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	// ActionListener Event----------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		// ------------------------------------------------------------------
		// 1. 투표 액션
		if (e.getSource().equals(f.voteBtn)) {
			chekVot++;
			f.sysMsg.setText("투표를 진행합니다.");
			f.chatWin.append("[SYSTEM] : 투표를 진행합니다.\n");
			String sql = "select * from mafiauser";
			ResultSet rs;

			try {
				rs = stmt.executeQuery(sql);
				nicName = new ArrayList();
				nicName.clear();
				while (rs.next()) {
					nicName.add(rs.getString(2));
				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// ----------------------------------
			CheckboxGroup cbg = new CheckboxGroup();

			person = new Checkbox[nicName.size()];
			for (int i = 0; i < person.length; i++) {
				person[i] = new Checkbox((String) nicName.get(i), cbg, false);
				person[i].setBackground(Color.black);
				person[i].setForeground(Color.red);
				sub.checkP.add(person[i]);
			}
			sub.setVisible(true); // sub 보여주기
			// ------------------------------------------------------------------
			// 2. start 게임 시작 액션
		} else if (e.getSource().equals(befor.bfokbtn)) { // 게임시작
			// String sqlInsert = "insert into mafiauser(ip,nicname) values ('"
			// + myIp + "','" + playerN + "')";
			String sqlInsert = "insert into mafiauser values ('" + myIp + "','"
					+ playerN + "','citizen',1, 0)"; // 임시로
														// citizen입력
														// null값일 경우
														// error
			// state : 입력시 1(생존)으로 세팅
			try {
				stmt.executeQuery(sqlInsert);

				System.out.println("입력성공");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			f.userName.setText(playerN);
			f.sysMsg.setText("게임을 시작합니다.");
			f.chatWin.append("[SYSTEM] : 게임을 시작합니다......\n");
			f.chatWin.append("[SYSTEM] : 마피아 선정을 위해 마피아 선정하기 버튼을 눌러주세요.\n");
			f.chatWin.append("[SYSTEM] : 투표는 제한된 시간 내에 해야합니다.\n");
			f.chatWin
					.append("[SYSTEM] : 마피아로 선정되면 귓속말은 '('를 작성 하거나 귓속말 버튼을 눌러 채팅해 주세요.\n");

			try {
				oos.writeObject("!startNic");
				System.out.println("처음 플레이 함을 전달합니다.");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			f.setVisible(true);
			befor.dispose();
			// ------------------------------------------------------------------
			// 3. 마피아 선정하기
		} else if (e.getSource().equals(f.jobName)) {
			f.jobName.removeActionListener(this);
			f.jobName.setIcon(new ImageIcon("./mafiasel2.png"));
			f.jobName.setRolloverIcon(new ImageIcon("./mafiasel2.png")); // 롤오버시

			f.sysMsg.setText("마피아를 선정하는 중입니다.");
			f.chatWin.append("[SYSTEM] : 마피아를 선정하는 중입니다.\n");

			try {
				oos.writeObject(playerN + "#!setMafi");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// ------------------------------------------------------------------
			// 4. main msg보내기
		} else if (e.getSource().equals(f.whisper)) {
			if(userJob.equals("mafia")){
				f.whisper.setEnabled(true);
				f.chat.setText("(");
			}else{
				f.whisper.setEnabled(false);
			}
		} else {

			String msg = f.chat.getText();

			if (msg.equals("")) {
				// NoAction
				System.out.println("메시지 노입력");
				// -------------------------------
				// 마피아 메세지
			} else if (userJob.equals("mafia")) {

				try {
					oos.writeObject(playerN + "#" + msg + "#" + "!Mafia"); // 마피아용
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				f.chat.setText(""); // 공백처리.
				// -------------------------------
				// 시민 메세지
			} else if (userJob.equals("citizen")) {
				try {
					oos.writeObject(playerN + "#" + msg + "#" + "!Citizen");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				f.chat.setText("");
			}// inner if else chain end
		}// outer if else end
	}// action end

	// -----------------------------------------------------------
	public void init() throws IOException {

		mainSocket = new Socket(ip, port); // ip 서버 아이피
		System.out.println("MainUI connected...");
		myIp = mainSocket.getLocalAddress().toString();

		oos = new ObjectOutputStream(mainSocket.getOutputStream());
		ois = new ObjectInputStream(mainSocket.getInputStream());

		MainUiThread mut = new MainUiThread(this);
		Thread t = new Thread(mut);
		t.start();
	}//

	// 메인메서드------------------------------------------------
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MainUI mu = new MainUI();
		JFrame.setDefaultLookAndFeelDecorated(true);
		// ----------------------------------------------------
		mu.init();
	}// main end

	// ---------------------------------
	public ObjectInputStream getOis() {
		return ois;
	}

	// ---------------------------------
	public JTextArea getJta() {
		return f.chatWin;
	}

}// class end
