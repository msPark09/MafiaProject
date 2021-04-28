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
	int chekVot = 0; // ��ǥ�߳� Ȯ��
	int chekMafi = 0; // ���Ǿ��ΰ� Ȯ��
	Checkbox[] person; // ��ǥ�� ���� üũ�ڽ�
	String votPerson; // ��ǥ�� ���

	// Socket------------------------
	Socket mainSocket;
	// Server ����-----------------------
	String ip = "203.236.209.207";
	int port = 5050;

	// DB����-----------------------
	String url = "jdbc:oracle:thin:@203.236.209.207:1521:xe";
	String user = "scott";
	String password = "tiger";

	Statement stmt; // �������
	ArrayList nicName; // �г��� ������ ��� ����Ʈ

	String myIp; // �� ������
	String userJob = "citizen"; // �� ����
	static String playerN; // �� ���̵�

	public MainUI() {

		befor = new GameFrame("���Ǿ�Classic");
		befor.setFrameBefore();

		befor.setVisible(true);
		befor.bfokbtn.addActionListener(this);
		befor.okayBtn.addMouseListener(this);

		f = new GameFrame("���Ǿ�Classic");
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
		sub = new GameFrame("��ǥ�ϱ�");
		sub.setSize(200, 200);
		sub.setFrameVoting();
		sub.sendBtn.addMouseListener(this); // ��ư �̺�Ʈ ���� //

	}// cons end

	// MouseListener
	// Event-------------------------------------------------------
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		// ------------------------------------------------------------------
		// 1. ��ǥ��� ������
		if (e.getSource().equals(sub.sendBtn)) { // sub�� ��ư ������
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
				System.out.println("��ǥ�� ��� : " + votPerson);
				f.sysMsg.setText("��ǥ�� �Ϸ��Ͽ����ϴ�.");
				f.chatWin.append("[SYSTEM] : ��ǥ�� �Ϸ��Ͽ����ϴ�.\n");
				nicName.clear();
				sub.checkP.removeAll();
				f.voteBtn.setEnabled(false);
				sub.dispose(); // sub ����
			}

			// ------------------------------------------------------------------
			// 2. �г����� �ߺ�Ȯ��
		} else if (e.getSource().equals(befor.okayBtn)) { // �г��� Ȯ��
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
				befor.info.setText("�г����� �Էµ��� �ʾҽ��ϴ�. �Է��� �ּ���.");
				befor.bfokbtn.setEnabled(false);
			} else {
				for (int i = 0; i < nicName.size(); i++) {
					if (playerN.equals(nicName.get(i))) {
						befor.info.setText("�г����� �ߺ��˴ϴ�. Ȯ�� �� �ٽ� �Է��� �ּ���.");
						befor.bfokbtn.setEnabled(false);
						befor.idInput.setText("");
						break;
					} else {
						index++;
					}
				}
				if (index == nicName.size()) {
					befor.info.setText("�г��� [" + playerN + "] ���� ������ �����մϴ�.");
					befor.bfokbtn.setEnabled(true);
					befor.okayBtn.setIcon(new ImageIcon("./idcheck1.png"));
				}
			}
			// ------------------------------------------------------------------
			// 3. send��ư �̺�Ʈ
		} else if (e.getSource().equals(f.send)) {
			// send��ư ������ ��� ó����.

			String msg = f.chat.getText();
			if (msg.equals("")) {
			} else {
				try {
					oos.writeObject(playerN + "#" + msg);
					System.out.println("�޽������: " + msg);
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
		// 1. ��ǥ �׼�
		if (e.getSource().equals(f.voteBtn)) {
			chekVot++;
			f.sysMsg.setText("��ǥ�� �����մϴ�.");
			f.chatWin.append("[SYSTEM] : ��ǥ�� �����մϴ�.\n");
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
			sub.setVisible(true); // sub �����ֱ�
			// ------------------------------------------------------------------
			// 2. start ���� ���� �׼�
		} else if (e.getSource().equals(befor.bfokbtn)) { // ���ӽ���
			// String sqlInsert = "insert into mafiauser(ip,nicname) values ('"
			// + myIp + "','" + playerN + "')";
			String sqlInsert = "insert into mafiauser values ('" + myIp + "','"
					+ playerN + "','citizen',1, 0)"; // �ӽ÷�
														// citizen�Է�
														// null���� ���
														// error
			// state : �Է½� 1(����)���� ����
			try {
				stmt.executeQuery(sqlInsert);

				System.out.println("�Է¼���");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			f.userName.setText(playerN);
			f.sysMsg.setText("������ �����մϴ�.");
			f.chatWin.append("[SYSTEM] : ������ �����մϴ�......\n");
			f.chatWin.append("[SYSTEM] : ���Ǿ� ������ ���� ���Ǿ� �����ϱ� ��ư�� �����ּ���.\n");
			f.chatWin.append("[SYSTEM] : ��ǥ�� ���ѵ� �ð� ���� �ؾ��մϴ�.\n");
			f.chatWin
					.append("[SYSTEM] : ���ǾƷ� �����Ǹ� �ӼӸ��� '('�� �ۼ� �ϰų� �ӼӸ� ��ư�� ���� ä���� �ּ���.\n");

			try {
				oos.writeObject("!startNic");
				System.out.println("ó�� �÷��� ���� �����մϴ�.");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			f.setVisible(true);
			befor.dispose();
			// ------------------------------------------------------------------
			// 3. ���Ǿ� �����ϱ�
		} else if (e.getSource().equals(f.jobName)) {
			f.jobName.removeActionListener(this);
			f.jobName.setIcon(new ImageIcon("./mafiasel2.png"));
			f.jobName.setRolloverIcon(new ImageIcon("./mafiasel2.png")); // �ѿ�����

			f.sysMsg.setText("���ǾƸ� �����ϴ� ���Դϴ�.");
			f.chatWin.append("[SYSTEM] : ���ǾƸ� �����ϴ� ���Դϴ�.\n");

			try {
				oos.writeObject(playerN + "#!setMafi");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// ------------------------------------------------------------------
			// 4. main msg������
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
				System.out.println("�޽��� ���Է�");
				// -------------------------------
				// ���Ǿ� �޼���
			} else if (userJob.equals("mafia")) {

				try {
					oos.writeObject(playerN + "#" + msg + "#" + "!Mafia"); // ���Ǿƿ�
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				f.chat.setText(""); // ����ó��.
				// -------------------------------
				// �ù� �޼���
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

		mainSocket = new Socket(ip, port); // ip ���� ������
		System.out.println("MainUI connected...");
		myIp = mainSocket.getLocalAddress().toString();

		oos = new ObjectOutputStream(mainSocket.getOutputStream());
		ois = new ObjectInputStream(mainSocket.getInputStream());

		MainUiThread mut = new MainUiThread(this);
		Thread t = new Thread(mut);
		t.start();
	}//

	// ���θ޼���------------------------------------------------
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
