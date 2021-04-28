package com.mafia.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.*;

import javax.swing.Timer;

public class GameTimer extends JPanel implements ActionListener {
	public final String DAY = "day";
	public final String NIGHT = "night";
	final int TIME = 11;

	public JPanel p1 = new JPanel(new BorderLayout());

	ImageIcon day = new ImageIcon("./dayimgm.png");
	ImageIcon night = new ImageIcon("./nightimgm.png");

	JLabel show = new JLabel();

	int time = TIME;
	int count = 0;
	JLabel jl = new JLabel("start");

	public Timer tm = new Timer(1000, this);

	public String stateDN = DAY;

	public GameTimer() {

		show.setIcon(day);
		jl.setHorizontalAlignment(JLabel.CENTER);
		jl.setFont(new Font("Serif", Font.BOLD, 12));
		jl.setForeground(Color.white);
		p1.setOpaque(false);
		p1.add(jl, BorderLayout.SOUTH);
		p1.add(show, BorderLayout.CENTER);

	}

	public void logic() {
		--time;
		jl.setText("" + time + "Ка");

		if (time == 0 && count == 0) {
			show.setIcon(night);
			time = TIME;
			count = 1;
			stateDN = NIGHT;
		} else if (time == 0 && count == 1) {
			show.setIcon(day);
			time = TIME;
			count = 0;
			stateDN = DAY;
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}