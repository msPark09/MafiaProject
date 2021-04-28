package com.mafia.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ProServer {

	HashMap<String, Integer> votHM;

	// DB������ ����-----------------------------------------------
	String url = "jdbc:oracle:thin:@203.236.209.207:1521:xe";
	String user = "scott";
	String password = "tiger";
	// ------------------------------------------------------------
	Statement stmt; // �������
	ArrayList nicName; // nicname����arr
	// -------------------------------------------------------------
	ArrayList<ProServerThread> ThreadList; // JJ
	Socket ProSocket; // JJ -- ������ Ŭ���̾�Ʈ�� ����Ǹ� ���� ����.
	public int randomPoint;

	// -------------------------------------------------------------
	public ProServer() throws IOException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			String sqlSelectAll = "select * from mafiauser";
			ResultSet rs = stmt.executeQuery(sqlSelectAll);	
			if(rs.next()){
				String sqlDelet = "delete from mafiauser";
				stmt.executeUpdate(sqlDelet);
				System.out.println("���̺� ����");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ThreadList = new ArrayList<ProServerThread>(); // JJ ��� ����Ʈ�� �����带 ����.
		ServerSocket ProServerSocket = new ServerSocket(5050); // ����Ǳ��� Ŭ���̾�Ʈ
															   // ������ ��ٸ� ��������.

		ProServerThread pst = null; // ���μ��������忡 �����ϱ� ���� ���������� pst.
		boolean ServerStop = false; // while�� ����ϱ� ���� �Ҹ��� ���� ����

		votHM = new HashMap<String, Integer>(); // ��� �����ϱ� �� �����ϴ� ��ü.

		while (!ServerStop) {
			System.out.println("�����غ�");
			ProSocket = ProServerSocket.accept(); // Ŭ���̾�Ʈ�� �����ٷ���
			System.out.println("��������");
			pst = new ProServerThread(this); // ���μ����� ��ü�� ���μ��� �����忡 �־���.
			ThreadList.add(pst);
			Thread t = new Thread(pst);
			t.start();
		}// while end

	}// proServer Cons
		// -------------------------------------------------------------

	public ArrayList<ProServerThread> getList() { // JJ ����Ʈ��ȯ �޼���
		return ThreadList;
	}

	// -------------------------------------------------------------
	public Socket getSocket() { // JJ ���Ϲ�ȯ �޼���
		return ProSocket;
	}

	// -------------------------------------------------------------
	// -------------------------------------------------------------
		public void deleDBList(String playerN) { // JJ ���Ϲ�ȯ �޼���
			String sqlDele = "delete from mafiauser where nicname ='"+playerN+"'";
			try {
				System.out.println("����Ʈ���� �����մϴ�.");
				stmt.executeQuery(sqlDele);
				System.out.println("���� ����");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// -------------------------------------------------------------

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new ProServer();
	}// main end

}// class end

