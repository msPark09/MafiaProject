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
				// 귓속말을 위한 사전작업. -
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

				if (message.startsWith("!startNic")) { // 유저 리스트의 실시간 반영을 위함
					nicList = message.substring(9);
					mu.f.userList.setText(nicList);

				} else if (receivedMsg[1].equals("!setMafi")) {
					mu.f.timer.tm.start();
					System.out.println("확인");
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
							mu.f.sysMsg.setText("당신은 마피아 입니다.");
							mu.f.chatWin.append("[SYSTEM] : 당신은 마피아 입니다.\n");
							mu.userJob = "mafia";
						} else {
							jobimg = new ImageIcon("./citizenimg.png");
							mu.chekMafi = 0;
							mu.f.jobName.setIcon(jobimg);
							mu.f.sysMsg.setText("당신은 시민 입니다.");
							mu.f.chatWin.append("[SYSTEM] : 당신은 시민 입니다.\n");
							mu.userJob = "citizen";
						}
					}
				} else if (receivedMsg[2].equals("!Mafia")) { // 마피아 메시지를 받았는데

					System.out.println("마피아 메시지처리 true.");

					// 내가 마피아인 경우.
					// --메시지가 보이게 한다.
					System.out.println("case1 mu.userJob: " + mu.userJob);

					// if (mu.userJob.equals("mafia")) {
					// // 내가 마피아이면서, 메시지가 그냥 오는거면 출력
					// mu.getJta().append(
					// "[ " + receivedMsg[0] + " ] :" + receivedMsg[1]
					// + System.lineSeparator());
					// mu.getJta().setLineWrap(true);
					// mu.getJta().setCaretPosition(
					// mu.getJta().getDocument().getLength());
					//
					// } else if (mu.userJob.equals("mafia")
					// && receivedMsg[1].startsWith("(")) { // 스타트위드.
					// // 내가 마피아 이면서. 메시지가 귓말로 오는거면
					// // --메시지가 보이게 한다.
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
							&& receivedMsg[1].startsWith("(")) { // 스타트위드.
						// 내가 시민이면서, 메시지가 마피아들의 귓말로 오는거면
						// --메시지가 안보이게 한다
						System.out.println("case2 mu.userJob: " + mu.userJob);
						mu.getJta().append(
								"secret message" + System.lineSeparator());
						mu.getJta().setLineWrap(true);
						mu.getJta().setCaretPosition(
								mu.getJta().getDocument().getLength());

					} else {
						// 마피아 메시지인데 내가 시민이면 귓말 아닌 모든메시지 항시 출력.

						mu.getJta().append(
								"[ " + receivedMsg[0] + " ] :" + receivedMsg[1]
										+ System.lineSeparator());
						mu.getJta().setLineWrap(true);
						mu.getJta().setCaretPosition(
								mu.getJta().getDocument().getLength());
					}// inner else end

				} else if (receivedMsg[2].equals("!Citizen")) { // 시민 메시지를 받으면
					// 내가 마피아건, 시민이건 all 출력
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
				// System.out.println("1.준비된 메시지 전송" + message);
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

