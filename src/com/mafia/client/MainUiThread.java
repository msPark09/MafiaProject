package com.mafia.client;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MainUiThread extends Thread {

	MainUI mu;

	// GameFrame f;

	public MainUiThread(MainUI mu) {
		this.mu = mu;
	}

	public void run() {

		String message = null;
		String[] receivedMsg = null;
		boolean isStop = false;
		String nicList = null;
		// ----------------------------------------------------------------------------------
		while (!isStop) {

			try {
				message = (String) mu.getOis().readObject();
				receivedMsg = message.split("#");
				// �ӼӸ��� ���� �����۾�. -
				// String sqlJob = "select job from mafiauser where nicname= '"
				// + mu.playerN + "'";
				// ResultSet rsJob = null;
				// try {
				// rsJob = mu.stmt.executeQuery(sqlJob);
				// } catch (Exception e2) {
				// // TODO Auto-generated catch block
				// e2.printStackTrace();
				// }
				// // -----------------------------------
				// while (rsJob.next()) {
				// mu.userJob = rsJob.getString("job");
				// }// while end

				if (message.startsWith("!startNic")) { // ���� ����Ʈ�� �ǽð� �ݿ��� ����
					nicList = message.substring(9);
					mu.f.userList.setText(nicList);

				} else if (receivedMsg[1].equals("!setMafi")) {
					mu.f.timer.tm.start();
					System.out.println("Ȯ��");
					String sqlJob = "select job from mafiauser where nicname = '"
							+ mu.playerN + "'";
					ResultSet rs = mu.stmt.executeQuery(sqlJob);
					ImageIcon jobimg;

					while (rs.next()) {
						System.out.println(rs.getString(1));
						if (rs.getString(1).equals("mafia")) {
							jobimg = new ImageIcon("./mafiaimg.png");
							mu.chekMafi++;
							mu.f.jobName.setIcon(jobimg);
							mu.f.sysMsg.setText("����� ���Ǿ� �Դϴ�.");
							mu.f.chatWin.append("[SYSTEM] : ����� ���Ǿ� �Դϴ�.\n");
							mu.userJob = "mafia";
						} else {
							jobimg = new ImageIcon("./citizenimg.png");
							mu.chekMafi = 0;
							mu.f.jobName.setIcon(jobimg);
							mu.f.sysMsg.setText("����� �ù� �Դϴ�.");
							mu.f.chatWin.append("[SYSTEM] : ����� �ù� �Դϴ�.\n");
							mu.userJob = "citizen";
						}
					}
				} else if (receivedMsg[2].equals("!Mafia")) { // ���Ǿ� �޽����� �޾Ҵµ�

					System.out.println("���Ǿ� �޽���ó�� true.");

					// ���� ���Ǿ��� ���.
					// --�޽����� ���̰� �Ѵ�.
					System.out.println("case1 mu.userJob: " + mu.userJob);

					// if (mu.userJob.equals("mafia")) {
					// // ���� ���Ǿ��̸鼭, �޽����� �׳� ���°Ÿ� ���
					// mu.getJta().append(
					// "[ " + receivedMsg[0] + " ] :" + receivedMsg[1]
					// + System.lineSeparator());
					// mu.getJta().setLineWrap(true);
					// mu.getJta().setCaretPosition(
					// mu.getJta().getDocument().getLength());
					//
					// } else if (mu.userJob.equals("mafia")
					// && receivedMsg[1].startsWith("(")) { // ��ŸƮ����.
					// // ���� ���Ǿ� �̸鼭. �޽����� �Ӹ��� ���°Ÿ�
					// // --�޽����� ���̰� �Ѵ�.
					// System.out.println("case2 mu.userJob: " + mu.userJob);
					// mu.getJta().append(
					// "[ " + receivedMsg[0] + " ] :" + receivedMsg[1]
					// + System.lineSeparator());
					// mu.getJta().setLineWrap(true);
					// mu.getJta().setCaretPosition(
					// mu.getJta().getDocument().getLength());
					//
					// } else
					if (mu.userJob.equals("citizen")
							&& receivedMsg[1].startsWith("(")) { // ��ŸƮ����.
						// ���� �ù��̸鼭, �޽����� ���ǾƵ��� �Ӹ��� ���°Ÿ�
						// --�޽����� �Ⱥ��̰� �Ѵ�
						System.out.println("case2 mu.userJob: " + mu.userJob);
						mu.getJta().append(
								"secret message" + System.lineSeparator());
						mu.getJta().setLineWrap(true);
						mu.getJta().setCaretPosition(
								mu.getJta().getDocument().getLength());

					} else {
						// ���Ǿ� �޽����ε� ���� �ù��̸� �Ӹ� �ƴ� ���޽��� �׽� ���.

						mu.getJta().append(
								"[ " + receivedMsg[0] + " ] :" + receivedMsg[1]
										+ System.lineSeparator());
						mu.getJta().setLineWrap(true);
						mu.getJta().setCaretPosition(
								mu.getJta().getDocument().getLength());
					}// inner else end

				} else if (receivedMsg[2].equals("!Citizen")) { // �ù� �޽����� ������
					// ���� ���Ǿư�, �ù��̰� all ���
					mu.getJta().append(
							"[ " + receivedMsg[0] + " ] :" + receivedMsg[1]
									+ System.lineSeparator());
					mu.getJta().setLineWrap(true);
					mu.getJta().setCaretPosition(
							mu.getJta().getDocument().getLength());
				}
				// -----------------------------------------------------------------------

				// else {
				// System.out.println("message: " + message);
				// System.out.println("1.�غ�� �޽��� ����" + message);
				// System.out.println(receivedMsg[0] + "," + receivedMsg[1]);
				//
				// mu.getJta().append(
				// "[ " + receivedMsg[0] + " ] :" + receivedMsg[1]
				// + System.lineSeparator());
				// mu.getJta().setLineWrap(true);
				// mu.getJta().setCaretPosition(
				// mu.getJta().getDocument().getLength());
				// }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isStop = true;
			}

		}// while end
			// ----------------------------------------------------------------------------------
	}// run end

}// class end

