package com.mafia.server;

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
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

public class ProServerThread implements Runnable {

	Socket ProSocket;
	ProServer ps;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public ProServerThread(ProServer ps) {
		this.ps = ps;
	}// cons end

	@Override
	public synchronized void run() {
		// TODO Auto-generated method stub

		boolean ServerStop = false;

		try {

			ProSocket = ps.getSocket();
			oos = new ObjectOutputStream(ProSocket.getOutputStream());
			ois = new ObjectInputStream(ProSocket.getInputStream());

			String message = null;
			String msg = "";

			// bw.write("게임을 시작합니다_ProServerThread");
			// ----------------------------------------------------DB연결부
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(ps.url, ps.user,
					ps.password);
			ps.stmt = conn.createStatement();
			String SqlSelectAll = "select * from mafiauser";
			ResultSet rs = ps.stmt.executeQuery(SqlSelectAll);

			// ----------------------------------------------------사용자 닉네임 저장
			ps.nicName = new ArrayList();
			while (rs.next()) {
				ps.nicName.add(rs.getString(2));
			}
			for (int i = 0; i < ps.nicName.size(); i++) {
				ps.votHM.put((String) ps.nicName.get(i), 0);
			}
			// -----------------------------------------------------

			while (!ServerStop) {

				message = (String) ois.readObject();
				String[] str = message.split("#");

				// ------------------------------------------------------------------------------
				// 1. 투표 결과 저장하기
				if (message.startsWith("!nic")) { // 원래는 msg였던 것을 임시적으로 돌려씀.

					String sqlSelectAll = "select * from mafiauser";
					rs = ps.stmt.executeQuery(sqlSelectAll);
					ps.nicName.clear();
					// -----------------------------------------------------
					// db에서 자료 받기
					while (rs.next()) {
						ps.nicName.add(rs.getString(2));
					}// while end
						// -----------------------------------------------------
						// 투표값 초기화
					for (int i = 0; i < ps.nicName.size(); i++) {
						if (!ps.votHM.containsKey(ps.nicName.get(i))) {
							ps.votHM.put((String) ps.nicName.get(i), 0);
						}
					}// for end
						// -----------------------------------------------------
					String nic = message.substring(4);
					// -----------------------------------------------------
					for (int i = 0; i < ps.nicName.size(); i++) {
						if (ps.nicName.get(i).equals(nic)) {
							int count = ps.votHM.get(nic).intValue() + 1;
							ps.votHM.put(nic, count);
						}
					}// for end
						// -----------------------------------------------------
					System.out.println(ps.votHM.toString());

					String sqlUpdate = "update mafiauser set count ="
							+ ps.votHM.get(nic) + "where nicname='" + nic + "'";
					ps.stmt.executeUpdate(sqlUpdate);

					// ------------------------------------------------------------------------------
					// 2. 처음 리스트 갱신
				} else if (message.startsWith("!startNic")) {
					String nicList = "";
					String sqlSelectAll = "select * from mafiauser";
					rs = ps.stmt.executeQuery(sqlSelectAll);
					while (rs.next()) {
						nicList += ("◆" + rs.getString(2) + "\n");
					}
					broadCasting("!startNic" + nicList);
					// ------------------------------------------------------------------------------
					// 3. 빠져 나갈때 리스트 갱신
				} else if (str[1].equals("exit")) {
					System.out.println("서버의 메시지 작업");
					System.out.println(str[0]);
					broadCasting(str[0] + "# 님이 방을 나갔습니다.");
					ps.deleDBList(str[0]);
					String nicList = "";
					String sqlSelectAll = "select * from mafiauser";
					rs = ps.stmt.executeQuery(sqlSelectAll);
					while (rs.next()) {
						nicList += ("◆" + rs.getString(2) + "\n");
					}
					broadCasting("!startNic" + nicList);
					ServerStop = true;
					// ------------------------------------------------------------------------------
					// 4. 마피아 선정하기
				} else if (str[1].equals("!setMafi")) { // 클라이언트의 직업선정 요청메시지.

					ps.randomPoint++;
					ps.nicName.add(str[0]);

					if (ps.randomPoint == 6) {

						String[] tempStore = new String[ps.nicName.size()];

						for (int i = 0; i < ps.nicName.size(); i++) {
							tempStore[i] = (String) ps.nicName.get(i);
						}
						System.out.println("ps.nicName.size(): "
								+ ps.nicName.size());
						System.out.println("tempStore.length: "
								+ tempStore.length);
						System.out.println("랜덤 전: "
								+ Arrays.toString(tempStore));
						// 랜덤처리 로직-------------------------------------------
						String temp = null;

						for (int i = 0; i < tempStore.length; i++) {
							int randomChoice;
							int last = 5;
							int randomRange = 5;
							// ----------------------------------------
							while (true) { // 여기서는 랜덥값을 범위에 맞게 뽑아내야됨.
								randomChoice = (int) (Math.random() * 10);
								// int randomRange = 5;

								if (randomChoice <= randomRange) {
									randomRange--;
									break;
								}// if end

								// randomRange--;
								if (randomRange == 1) {
									break;
								}
							}// while end
								// --------------------------------------
							temp = tempStore[last];
							tempStore[last] = tempStore[randomChoice];
							tempStore[randomChoice] = temp;

							last--;

						}// for end
							// ----------------------------------------여기까지가
							// 랜덤섞기.
						System.out.println("랜덤 후: "
								+ Arrays.toString(tempStore)); // 섞은결과 보여주기

						String firstMafia = (String) tempStore[2]; // 섞은 배열결과에서
																	// 3번째 유저가
																	// 마피아
						String secondMafia = (String) tempStore[5]; // 섞은 배열결과에서
																	// 6번째 유저가
																	// 마피아

						System.out.println("who is firstMafia: " + firstMafia);
						System.out
								.println("who is secondMafia: " + secondMafia);

						String mafi = "mafia";
						String citizen = "citizen";

						// 마피아 선정 후 db저장
						String sqlCitizen = "update mafiauser set job = '"
								+ citizen + "'";
						String sqlFirstMf = "update mafiauser set job = '"
								+ mafi + "'" + "where nicname = '" + firstMafia
								+ "'";
						String sqlSecondMf = "update mafiauser set job = '"
								+ mafi + "'" + "where nicname = '"
								+ secondMafia + "'";

						ps.stmt.executeUpdate(sqlCitizen);
						ps.stmt.executeUpdate(sqlFirstMf);
						ps.stmt.executeUpdate(sqlSecondMf);

						// 마피아 선정 완료 메세지 전송 str[0]은 사용자ID에 해당하는 playerN
						broadCasting(str[0] + "#!setMafi");

					}// inner if end -- 6명의 클라이언트가 버튼을 눌렀을 경우
					// ------------------------------------------------------------------------------
					// 5. 일반 메세지 
					} else {
					broadCasting(message);
				}
			}// while end
				// ----------------------------------------------------------------------------------
			ps.getList().remove(this); // 메시지를 보내고 나서 제거. 쓰레드 제거. ... 프로서버의
										// 객체.컨스
			System.out.println(ProSocket.getInetAddress() + " :정상종료");
			System.out.println("list size: " + ps.getList().size());

		} catch (Exception e) {
			ps.getList().remove(this);
			e.printStackTrace();
			System.out.println(ProSocket.getInetAddress() + " :비정상종료");
			System.out.println("list size: " + ps.getList().size());
		}

	}// run end

	// ----------------------------------------------------------------------------------
	public void broadCasting(String message) throws IOException {
		for (ProServerThread ct : ps.getList()) {
			ct.send(message);
		}
	}

	// ----------------------------------------------------------------------------------
	public void send(String message) throws IOException {
		oos.writeObject(message);
	}

}// class end
