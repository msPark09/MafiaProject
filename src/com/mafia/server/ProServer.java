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

	// DB연결을 위함-----------------------------------------------
	String url = "jdbc:oracle:thin:@203.236.209.207:1521:xe";
	String user = "scott";
	String password = "tiger";
	// ------------------------------------------------------------
	Statement stmt; // 연결상태
	ArrayList nicName; // nicname저장arr
	// -------------------------------------------------------------
	ArrayList<ProServerThread> ThreadList; // JJ
	Socket ProSocket; // JJ -- 서버가 클라이언트와 연결되면 만들 소켓.
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
				System.out.println("테이블 비우기");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ThreadList = new ArrayList<ProServerThread>(); // JJ 어레이 리스트에 쓰레드를 담음.
		ServerSocket ProServerSocket = new ServerSocket(5050); // 연결되기전 클라이언트
															   // 접속을 기다릴 서버소켓.

		ProServerThread pst = null; // 프로서버스레드에 접근하기 위한 참조변수로 pst.
		boolean ServerStop = false; // while에 사용하기 위한 불린형 변수 선언

		votHM = new HashMap<String, Integer>(); // 디비에 저장하기 전 저장하는 매체.

		while (!ServerStop) {
			System.out.println("서버준비");
			ProSocket = ProServerSocket.accept(); // 클라이언트의 연결기다려줌
			System.out.println("서버연결");
			pst = new ProServerThread(this); // 프로서버의 객체를 프로서버 쓰레드에 넣어줌.
			ThreadList.add(pst);
			Thread t = new Thread(pst);
			t.start();
		}// while end

	}// proServer Cons
		// -------------------------------------------------------------

	public ArrayList<ProServerThread> getList() { // JJ 리스트반환 메서드
		return ThreadList;
	}

	// -------------------------------------------------------------
	public Socket getSocket() { // JJ 소켓반환 메서드
		return ProSocket;
	}

	// -------------------------------------------------------------
	// -------------------------------------------------------------
		public void deleDBList(String playerN) { // JJ 소켓반환 메서드
			String sqlDele = "delete from mafiauser where nicname ='"+playerN+"'";
			try {
				System.out.println("리스트에서 삭제합니다.");
				stmt.executeQuery(sqlDele);
				System.out.println("삭제 성공");
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

