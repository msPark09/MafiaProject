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

			// bw.write("������ �����մϴ�_ProServerThread");
			// ----------------------------------------------------DB�����
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(ps.url, ps.user,
					ps.password);
			ps.stmt = conn.createStatement();
			String SqlSelectAll = "select * from mafiauser";
			ResultSet rs = ps.stmt.executeQuery(SqlSelectAll);

			// ----------------------------------------------------����� �г��� ����
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
				// 1. ��ǥ ��� �����ϱ�
				if (message.startsWith("!nic")) { // ������ msg���� ���� �ӽ������� ������.

					String sqlSelectAll = "select * from mafiauser";
					rs = ps.stmt.executeQuery(sqlSelectAll);
					ps.nicName.clear();
					// -----------------------------------------------------
					// db���� �ڷ� �ޱ�
					while (rs.next()) {
						ps.nicName.add(rs.getString(2));
					}// while end
						// -----------------------------------------------------
						// ��ǥ�� �ʱ�ȭ
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
					// 2. ó�� ����Ʈ ����
				} else if (message.startsWith("!startNic")) {
					String nicList = "";
					String sqlSelectAll = "select * from mafiauser";
					rs = ps.stmt.executeQuery(sqlSelectAll);
					while (rs.next()) {
						nicList += ("��" + rs.getString(2) + "\n");
					}
					broadCasting("!startNic" + nicList);
					// ------------------------------------------------------------------------------
					// 3. ���� ������ ����Ʈ ����
				} else if (str[1].equals("exit")) {
					System.out.println("������ �޽��� �۾�");
					System.out.println(str[0]);
					broadCasting(str[0] + "# ���� ���� �������ϴ�.");
					ps.deleDBList(str[0]);
					String nicList = "";
					String sqlSelectAll = "select * from mafiauser";
					rs = ps.stmt.executeQuery(sqlSelectAll);
					while (rs.next()) {
						nicList += ("��" + rs.getString(2) + "\n");
					}
					broadCasting("!startNic" + nicList);
					ServerStop = true;
					// ------------------------------------------------------------------------------
					// 4. ���Ǿ� �����ϱ�
				} else if (str[1].equals("!setMafi")) { // Ŭ���̾�Ʈ�� �������� ��û�޽���.

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
						System.out.println("���� ��: "
								+ Arrays.toString(tempStore));
						// ����ó�� ����-------------------------------------------
						String temp = null;

						for (int i = 0; i < tempStore.length; i++) {
							int randomChoice;
							int last = 5;
							int randomRange = 5;
							// ----------------------------------------
							while (true) { // ���⼭�� �������� ������ �°� �̾Ƴ��ߵ�.
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
							// ----------------------------------------���������
							// ��������.
						System.out.println("���� ��: "
								+ Arrays.toString(tempStore)); // ������� �����ֱ�

						String firstMafia = (String) tempStore[2]; // ���� �迭�������
																	// 3��° ������
																	// ���Ǿ�
						String secondMafia = (String) tempStore[5]; // ���� �迭�������
																	// 6��° ������
																	// ���Ǿ�

						System.out.println("who is firstMafia: " + firstMafia);
						System.out
								.println("who is secondMafia: " + secondMafia);

						String mafi = "mafia";
						String citizen = "citizen";

						// ���Ǿ� ���� �� db����
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

						// ���Ǿ� ���� �Ϸ� �޼��� ���� str[0]�� �����ID�� �ش��ϴ� playerN
						broadCasting(str[0] + "#!setMafi");

					}// inner if end -- 6���� Ŭ���̾�Ʈ�� ��ư�� ������ ���
					// ------------------------------------------------------------------------------
					// 5. �Ϲ� �޼��� 
					} else {
					broadCasting(message);
				}
			}// while end
				// ----------------------------------------------------------------------------------
			ps.getList().remove(this); // �޽����� ������ ���� ����. ������ ����. ... ���μ�����
										// ��ü.����
			System.out.println(ProSocket.getInetAddress() + " :��������");
			System.out.println("list size: " + ps.getList().size());

		} catch (Exception e) {
			ps.getList().remove(this);
			e.printStackTrace();
			System.out.println(ProSocket.getInetAddress() + " :����������");
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
