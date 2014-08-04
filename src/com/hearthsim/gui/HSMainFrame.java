package com.hearthsim.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpringLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.SwingConstants;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.border.MatteBorder;
import javax.swing.JList;

import com.hearthsim.GameResult;
import com.hearthsim.card.Card;
import com.hearthsim.event.HSSimulationEventListener;
import com.hearthsim.exception.HSInvalidCardException;
import com.hearthsim.exception.HSInvalidHeroException;
import com.hearthsim.io.DeckListFile;
import com.hearthsim.util.BoardState;
import com.ptplot.Plot;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.CardLayout;
import java.awt.FlowLayout;

public class HSMainFrame implements HSSimulationEventListener {

	private JFrame frame;
	private final JPanel ControlPane = new JPanel();
	
	private static final Color BACKGROUND_COLOR = new Color(32, 32, 32);
	private static final Color DEFAULT_BUTTON_COLOR = new Color(0, 140, 186);
	private static final Color SUCCESS_BUTTON_COLOR = new Color(67, 172, 106);
	private static final Color ERROR_BUTTON_COLOR = new Color(240, 65, 36);
	private static final Color WARNING_BUTTON_COLOW = new Color(240, 138, 36);
	
	private final HSMainFrameModel hsModel_;
	
	private DefaultListModel deckListModel0_;
	private DefaultListModel deckListModel1_;
	
	private JFileChooser fileChooser_;
	
	private JLabel lblHero_0;
	private JLabel lblHero_1;
	
	private JLabel lblWin_0;
	private JLabel lblWinRate_0;
	private JLabel lblConfNum_0;
	
	private JLabel lblWin_1;
	private JLabel lblWinRate_1;
	private JLabel lblConfNum_1;
	
	private JButton btnRun;
	
	private JPanel plotCardPane;
	private Plot plot_aveMinions;
	private Plot plot_aveCards;
	
	private static final DecimalFormat pFormatter_ = new DecimalFormat("0.00");


	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HSMainFrame window = new HSMainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HSMainFrame() {
		hsModel_ = new HSMainFrameModel(this);
		initialize();
		hsModel_.getSimulation().addSimulationEventListener(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(BACKGROUND_COLOR);
		frame.setBounds(100, 100, 960, 640);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, ControlPane, -40, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, ControlPane, 0, SpringLayout.SOUTH, frame.getContentPane());
		frame.getContentPane().setLayout(springLayout);
		
		JPanel Player0Panel = new JPanel();
		Player0Panel.setBorder(new MatteBorder(0, 0, 0, 1, (Color) Color.GRAY));
		Player0Panel.setOpaque(false);
		Player0Panel.setBackground(Color.DARK_GRAY);
		springLayout.putConstraint(SpringLayout.WEST, ControlPane, 0, SpringLayout.EAST, Player0Panel);
		springLayout.putConstraint(SpringLayout.NORTH, Player0Panel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, Player0Panel, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, Player0Panel, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, Player0Panel, 200, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(Player0Panel);
		
		JPanel InfoPane = new JPanel();
		InfoPane.setOpaque(false);
		springLayout.putConstraint(SpringLayout.NORTH, InfoPane, 0, SpringLayout.NORTH, Player0Panel);
		springLayout.putConstraint(SpringLayout.WEST, InfoPane, 0, SpringLayout.EAST, Player0Panel);
		springLayout.putConstraint(SpringLayout.SOUTH, InfoPane, 250, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().add(InfoPane);
		
		JPanel PlotPane = new JPanel();
		PlotPane.setBorder(null);
		PlotPane.setOpaque(false);
		springLayout.putConstraint(SpringLayout.NORTH, PlotPane, 0, SpringLayout.SOUTH, InfoPane);
		springLayout.putConstraint(SpringLayout.WEST, PlotPane, 0, SpringLayout.EAST, Player0Panel);
		SpringLayout sl_Player0Panel = new SpringLayout();
		Player0Panel.setLayout(sl_Player0Panel);
		
		JPanel HeroPane_0 = new JPanel();
		HeroPane_0.setOpaque(false);
		HeroPane_0.setBackground(Color.DARK_GRAY);
		sl_Player0Panel.putConstraint(SpringLayout.NORTH, HeroPane_0, 0, SpringLayout.NORTH, Player0Panel);
		sl_Player0Panel.putConstraint(SpringLayout.WEST, HeroPane_0, 0, SpringLayout.WEST, Player0Panel);
		sl_Player0Panel.putConstraint(SpringLayout.SOUTH, HeroPane_0, 60, SpringLayout.NORTH, Player0Panel);
		sl_Player0Panel.putConstraint(SpringLayout.EAST, HeroPane_0, 0, SpringLayout.EAST, Player0Panel);
		springLayout.putConstraint(SpringLayout.NORTH, HeroPane_0, 0, SpringLayout.NORTH, Player0Panel);
		springLayout.putConstraint(SpringLayout.WEST, HeroPane_0, 0, SpringLayout.WEST, Player0Panel);
		springLayout.putConstraint(SpringLayout.SOUTH, HeroPane_0, 50, SpringLayout.NORTH, Player0Panel);
		springLayout.putConstraint(SpringLayout.EAST, HeroPane_0, 0, SpringLayout.EAST, Player0Panel);
		Player0Panel.add(HeroPane_0);
		
		JScrollPane DeckPane_0 = new JScrollPane();
		sl_Player0Panel.putConstraint(SpringLayout.WEST, DeckPane_0, 5, SpringLayout.WEST, Player0Panel);
		DeckPane_0.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		DeckPane_0.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		DeckPane_0.setOpaque(false);
		DeckPane_0.setBackground(Color.DARK_GRAY);
		DeckPane_0.setLayout(new ScrollPaneLayout());
		DeckPane_0.getViewport().setOpaque(false);
		DeckPane_0.setBorder(BorderFactory.createEmptyBorder());
		sl_Player0Panel.putConstraint(SpringLayout.NORTH, DeckPane_0, 0, SpringLayout.SOUTH, HeroPane_0);
		HeroPane_0.setLayout(null);
		
		lblHero_0 = new JLabel("Hero0");
		lblHero_0.setHorizontalAlignment(SwingConstants.CENTER);
		lblHero_0.setBounds(6, 6, 187, 48);
		lblHero_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		lblHero_0.setForeground(Color.WHITE);
		HeroPane_0.add(lblHero_0);
		sl_Player0Panel.putConstraint(SpringLayout.EAST, DeckPane_0, 0, SpringLayout.EAST, Player0Panel);
		Player0Panel.add(DeckPane_0);
		
		JPanel ControlPane_0 = new JPanel();
		ControlPane_0.setBackground(Color.DARK_GRAY);
		ControlPane_0.setOpaque(false);
		sl_Player0Panel.putConstraint(SpringLayout.SOUTH, DeckPane_0, 0, SpringLayout.NORTH, ControlPane_0);
		
		final JList deckList_0 = new JList();
		deckList_0.setForeground(Color.WHITE);
		deckList_0.setBackground(BACKGROUND_COLOR);
		deckList_0.setOpaque(false);
		DeckPane_0.setViewportView(deckList_0);
		sl_Player0Panel.putConstraint(SpringLayout.NORTH, ControlPane_0, -40, SpringLayout.SOUTH, Player0Panel);
		sl_Player0Panel.putConstraint(SpringLayout.WEST, ControlPane_0, 0, SpringLayout.WEST, Player0Panel);
		sl_Player0Panel.putConstraint(SpringLayout.SOUTH, ControlPane_0, 0, SpringLayout.SOUTH, Player0Panel);
		sl_Player0Panel.putConstraint(SpringLayout.EAST, ControlPane_0, 0, SpringLayout.EAST, Player0Panel);
		Player0Panel.add(ControlPane_0);

		SpringLayout sl_ControlPane_0 = new SpringLayout();
		ControlPane_0.setLayout(sl_ControlPane_0);
		
		HSButton p0_load = new HSButton("Load...");
		p0_load.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (fileChooser_ == null) fileChooser_ = new JFileChooser();
				int retVal = fileChooser_.showOpenDialog(frame);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					try {
						DeckListFile deckList = new DeckListFile(fileChooser_.getSelectedFile().toPath());
						deckListModel0_ = new DefaultListModel();
						for (int indx = 0; indx < deckList.getDeck().getNumCards(); ++indx) {
							Card card = deckList.getDeck().drawCard(indx);
							deckListModel0_.addElement("["+card.getMana()+"] " + card.getName());
						}
						deckList_0.setModel(deckListModel0_);
						hsModel_.getSimulation().setDeck_p0(deckList.getDeck());
						hsModel_.getSimulation().setHero_p0(deckList.getHero());
						lblHero_0.setText(deckList.getHero().getName());
					} catch (HSInvalidCardException | HSInvalidHeroException
							| IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(frame,
							    "Error opening the deck file",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		p0_load.setForeground(Color.WHITE);
		p0_load.setBackground(DEFAULT_BUTTON_COLOR);
		p0_load.setPreferredSize(new Dimension(80, 30));
		sl_ControlPane_0.putConstraint(SpringLayout.NORTH, p0_load, 5, SpringLayout.NORTH, ControlPane_0);
		sl_ControlPane_0.putConstraint(SpringLayout.WEST, p0_load, 5, SpringLayout.WEST, ControlPane_0);
		ControlPane_0.add(p0_load);
		
		JButton p0_save = new HSButton("Save...");
		p0_save.setForeground(Color.WHITE);
		p0_save.setBackground(DEFAULT_BUTTON_COLOR);
		sl_ControlPane_0.putConstraint(SpringLayout.EAST, p0_save, -5, SpringLayout.EAST, ControlPane_0);
		p0_save.setPreferredSize(new Dimension(80, 30));
		sl_ControlPane_0.putConstraint(SpringLayout.NORTH, p0_save, 5, SpringLayout.NORTH, ControlPane_0);
		ControlPane_0.add(p0_save);
		springLayout.putConstraint(SpringLayout.SOUTH, PlotPane, 0, SpringLayout.NORTH, ControlPane);
		frame.getContentPane().add(PlotPane);
		
		JPanel Player1Panel = new JPanel();
		Player1Panel.setBorder(new MatteBorder(0, 1, 0, 0, (Color) Color.GRAY));
		Player1Panel.setOpaque(false);
		springLayout.putConstraint(SpringLayout.EAST, ControlPane, 0, SpringLayout.WEST, Player1Panel);
		springLayout.putConstraint(SpringLayout.EAST, InfoPane, 0, SpringLayout.WEST, Player1Panel);
		SpringLayout sl_InfoPane = new SpringLayout();
		InfoPane.setLayout(sl_InfoPane);
		
		JPanel Player0Info = new JPanel();
		sl_InfoPane.putConstraint(SpringLayout.WEST, Player0Info, 25, SpringLayout.WEST, InfoPane);
		sl_InfoPane.putConstraint(SpringLayout.SOUTH, Player0Info, -5, SpringLayout.SOUTH, InfoPane);
		sl_InfoPane.putConstraint(SpringLayout.EAST, Player0Info, 170, SpringLayout.WEST, InfoPane);
		Player0Info.setOpaque(false);
		sl_InfoPane.putConstraint(SpringLayout.NORTH, Player0Info, 5, SpringLayout.NORTH, InfoPane);
		InfoPane.add(Player0Info);
		
		JLabel lblLabel1_0 = new JLabel("P0 Wins");
		lblLabel1_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
		lblLabel1_0.setForeground(Color.WHITE);
		Player0Info.add(lblLabel1_0);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_2.setPreferredSize(new Dimension(135, 10));
		Player0Info.add(rigidArea_2);
		
		lblWin_0 = new JLabel("--");
		lblWin_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		lblWin_0.setHorizontalAlignment(SwingConstants.CENTER);
		lblWin_0.setPreferredSize(new Dimension(135, 40));
		lblWin_0.setForeground(Color.WHITE);
		Player0Info.add(lblWin_0);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_1.setPreferredSize(new Dimension(135, 10));
		Player0Info.add(rigidArea_1);
		
		lblWinRate_0 = new JLabel("--");
		lblWinRate_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		lblWinRate_0.setHorizontalAlignment(SwingConstants.CENTER);
		lblWinRate_0.setHorizontalTextPosition(SwingConstants.CENTER);
		lblWinRate_0.setForeground(Color.WHITE);
		lblWinRate_0.setPreferredSize(new Dimension(135, 40));
		Player0Info.add(lblWinRate_0);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		rigidArea.setPreferredSize(new Dimension(135, 20));
		Player0Info.add(rigidArea);
		
		JLabel lblConf_0 = new JLabel("95% Conf Range");
		lblConf_0.setForeground(Color.WHITE);
		lblConf_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
		Player0Info.add(lblConf_0);
		
		lblConfNum_0 = new JLabel("--");
		lblConfNum_0.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfNum_0.setPreferredSize(new Dimension(135, 30));
		lblConfNum_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
		lblConfNum_0.setForeground(Color.WHITE);
		Player0Info.add(lblConfNum_0);
		
		JPanel Player1Info = new JPanel();
		sl_InfoPane.putConstraint(SpringLayout.WEST, Player1Info, -170, SpringLayout.EAST, InfoPane);
		sl_InfoPane.putConstraint(SpringLayout.EAST, Player1Info, -25, SpringLayout.EAST, InfoPane);
		Player1Info.setOpaque(false);
		sl_InfoPane.putConstraint(SpringLayout.NORTH, Player1Info, 5, SpringLayout.NORTH, InfoPane);
		sl_InfoPane.putConstraint(SpringLayout.SOUTH, Player1Info, -5, SpringLayout.SOUTH, InfoPane);
		InfoPane.add(Player1Info);
		springLayout.putConstraint(SpringLayout.EAST, PlotPane, 0, SpringLayout.WEST, Player1Panel);
		SpringLayout sl_PlotPane = new SpringLayout();
		PlotPane.setLayout(sl_PlotPane);
		
		JPanel plotTabPane = new JPanel();
		sl_PlotPane.putConstraint(SpringLayout.SOUTH, plotTabPane, 20, SpringLayout.NORTH, PlotPane);
		FlowLayout flowLayout = (FlowLayout) plotTabPane.getLayout();
		flowLayout.setVgap(1);
		flowLayout.setHgap(1);
		plotTabPane.setBackground(BACKGROUND_COLOR);
		sl_PlotPane.putConstraint(SpringLayout.NORTH, plotTabPane, 0, SpringLayout.NORTH, PlotPane);
		sl_PlotPane.putConstraint(SpringLayout.WEST, plotTabPane, 0, SpringLayout.WEST, PlotPane);
		sl_PlotPane.putConstraint(SpringLayout.EAST, plotTabPane, 0, SpringLayout.EAST, PlotPane);
		PlotPane.add(plotTabPane);
		
		plotCardPane = new JPanel();
		sl_PlotPane.putConstraint(SpringLayout.NORTH, plotCardPane, 0, SpringLayout.SOUTH, plotTabPane);
		
		HSTabButton tabAveMinions = new HSTabButton("Ave # Minions");
		tabAveMinions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout cl = (CardLayout)(plotCardPane.getLayout());
				cl.show(plotCardPane, "plot_aveMinions");
			}
		});
		tabAveMinions.setBorder(null);
		tabAveMinions.setFont(new Font("Helvetica Neue", Font.PLAIN, 10));
		tabAveMinions.setBackground(WARNING_BUTTON_COLOW);
		tabAveMinions.setForeground(Color.WHITE);
		plotTabPane.add(tabAveMinions);
		
		HSTabButton tabAveCards = new HSTabButton("Ave # Cards");
		tabAveCards.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CardLayout cl = (CardLayout)(plotCardPane.getLayout());
				cl.show(plotCardPane, "plot_aveCards");
			}
		});
		tabAveCards.setBorder(null);
		tabAveCards.setFont(new Font("Helvetica Neue", Font.PLAIN, 10));
		tabAveCards.setBackground(WARNING_BUTTON_COLOW);
		tabAveCards.setForeground(Color.WHITE);
		plotTabPane.add(tabAveCards);

		sl_PlotPane.putConstraint(SpringLayout.WEST, plotCardPane, 0, SpringLayout.WEST, PlotPane);
		sl_PlotPane.putConstraint(SpringLayout.SOUTH, plotCardPane, 0, SpringLayout.SOUTH, PlotPane);
		sl_PlotPane.putConstraint(SpringLayout.EAST, plotCardPane, 0, SpringLayout.EAST, PlotPane);
		PlotPane.add(plotCardPane);
		plotCardPane.setLayout(new CardLayout(0, 0));
		
		plot_aveMinions = new Plot();
		FlowLayout flowLayout_1 = (FlowLayout) plot_aveMinions.getLayout();
		flowLayout_1.setVgap(0);
		plot_aveMinions.setYRange(0, 6.0);
		plot_aveMinions.setXRange(0, 30.0);
		plot_aveMinions.setTitle("Average Number of Minions");
		plot_aveMinions.addLegend(0, "Player0");
		plot_aveMinions.addLegend(1, "Player1");
		plot_aveMinions.setXLabel("Turn");
		plot_aveMinions.setBackground(BACKGROUND_COLOR);
		plot_aveMinions.setForeground(Color.WHITE);
		plot_aveMinions.setGrid(false);
		plot_aveMinions.setLabelFont("Helvetica Neue");
		plotCardPane.add(plot_aveMinions, "plot_aveMinions");

		plot_aveCards = new Plot();
		FlowLayout flowLayout_2 = (FlowLayout) plot_aveCards.getLayout();
		flowLayout_2.setVgap(0);
		plot_aveCards.setYRange(0, 6.0);
		plot_aveCards.setXRange(0, 30.0);
		plot_aveCards.setTitle("Average Number of Cards");
		plot_aveCards.addLegend(0, "Player0");
		plot_aveCards.addLegend(1, "Player1");
		plot_aveCards.setXLabel("Turn");
		plot_aveCards.setBackground(BACKGROUND_COLOR);
		plot_aveCards.setForeground(Color.WHITE);
		plot_aveCards.setGrid(false);
		plot_aveCards.setLabelFont("Helvetica Neue");
		plotCardPane.add(plot_aveCards, "plot_aveCards");

		ControlPane.setOpaque(false);
		frame.getContentPane().add(ControlPane);
		
		JLabel lblLabel1_1 = new JLabel("P1 Wins");
		lblLabel1_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
		lblLabel1_1.setForeground(Color.WHITE);
		Player1Info.add(lblLabel1_1);
		
		Component rigidArea_1_0 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_1_0.setPreferredSize(new Dimension(135, 10));
		Player1Info.add(rigidArea_1_0);
		
		lblWin_1 = new JLabel("--");
		lblWin_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		lblWin_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblWin_1.setPreferredSize(new Dimension(135, 40));
		lblWin_1.setForeground(Color.WHITE);
		Player1Info.add(lblWin_1);
		
		Component rigidArea_1_1 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_1_1.setPreferredSize(new Dimension(135, 10));
		Player1Info.add(rigidArea_1_1);
		
		lblWinRate_1 = new JLabel("--");
		lblWinRate_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		lblWinRate_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblWinRate_1.setHorizontalTextPosition(SwingConstants.CENTER);
		lblWinRate_1.setForeground(Color.WHITE);
		lblWinRate_1.setPreferredSize(new Dimension(135, 40));
		Player1Info.add(lblWinRate_1);
		
		Component rigidArea_0_2 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_0_2.setPreferredSize(new Dimension(135, 20));
		Player1Info.add(rigidArea_0_2);
		
		JLabel lblConf_1 = new JLabel("95% Conf Range");
		lblConf_1.setForeground(Color.WHITE);
		lblConf_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
		Player1Info.add(lblConf_1);
		
		lblConfNum_1 = new JLabel("--");
		lblConfNum_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfNum_1.setPreferredSize(new Dimension(135, 30));
		lblConfNum_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
		lblConfNum_1.setForeground(Color.WHITE);
		Player1Info.add(lblConfNum_1);
		
		JPanel generalInfo = new JPanel();
		generalInfo.setForeground(Color.WHITE);
		generalInfo.setBackground(BACKGROUND_COLOR);
		sl_InfoPane.putConstraint(SpringLayout.NORTH, generalInfo, 0, SpringLayout.NORTH, InfoPane);
		sl_InfoPane.putConstraint(SpringLayout.WEST, generalInfo, 0, SpringLayout.EAST, Player0Info);
		sl_InfoPane.putConstraint(SpringLayout.SOUTH, generalInfo, 0, SpringLayout.SOUTH, InfoPane);
		sl_InfoPane.putConstraint(SpringLayout.EAST, generalInfo, 0, SpringLayout.WEST, Player1Info);
		InfoPane.add(generalInfo);
		
		HSButton btnSetting = new HSButton("Settings");
		btnSetting.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HSSimulationSettingsFrame settingsFrame = new HSSimulationSettingsFrame(frame, hsModel_.getSimulation());
				settingsFrame.setVisible(true);
			}
		});
		btnSetting.setForeground(Color.WHITE);
		btnSetting.setBackground(DEFAULT_BUTTON_COLOR);
		ControlPane.add(btnSetting);
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		rigidArea_3.setPreferredSize(new Dimension(100, 20));
		ControlPane.add(rigidArea_3);
		
		HSButton btnReset = new HSButton("Reset");
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				hsModel_.resetSimulationResults();
				HSMainFrame.this.updatePlotPanel();
				HSMainFrame.this.updateInfoPanel();
			}
		});
		btnReset.setForeground(Color.WHITE);
		btnReset.setBackground(DEFAULT_BUTTON_COLOR);
		ControlPane.add(btnReset);

		btnRun = new HSButton("Run");
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (hsModel_.getSimulation().getDeck_p0() == null) {
					JOptionPane.showMessageDialog(frame,
						    "Player0 deck is missing",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (hsModel_.getSimulation().getDeck_p1() == null) {
					JOptionPane.showMessageDialog(frame,
						    "Player1 deck is missing",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Runnable runner = new Runnable() {
					public void run() {
						hsModel_.runSimulation();
					}
				};
				new Thread(runner).start();
			}
		});		
		btnRun.setBackground(SUCCESS_BUTTON_COLOR);
		btnRun.setForeground(Color.WHITE);
		ControlPane.add(btnRun);
		springLayout.putConstraint(SpringLayout.NORTH, Player1Panel, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, Player1Panel, -200, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, Player1Panel, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, Player1Panel, 0, SpringLayout.EAST, frame.getContentPane());


		frame.getContentPane().add(Player1Panel);
		SpringLayout sl_Player1Panel = new SpringLayout();
		Player1Panel.setLayout(sl_Player1Panel);
		
		JPanel HeroPane_1 = new JPanel();
		HeroPane_1.setOpaque(false);
		sl_Player1Panel.putConstraint(SpringLayout.NORTH, HeroPane_1, 0, SpringLayout.NORTH, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.WEST, HeroPane_1, 0, SpringLayout.WEST, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.SOUTH, HeroPane_1, 60, SpringLayout.NORTH, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.EAST, HeroPane_1, 0, SpringLayout.EAST, Player1Panel);
		Player1Panel.add(HeroPane_1);
		
		JScrollPane DeckPane_1 = new JScrollPane();
		sl_Player1Panel.putConstraint(SpringLayout.WEST, DeckPane_1, 5, SpringLayout.WEST, Player1Panel);
		DeckPane_1.setOpaque(false);
		DeckPane_1.setBackground(Color.DARK_GRAY);
		DeckPane_1.setLayout(new ScrollPaneLayout());
		DeckPane_1.getViewport().setOpaque(false);
		DeckPane_1.setBorder(BorderFactory.createEmptyBorder());
		sl_Player1Panel.putConstraint(SpringLayout.NORTH, DeckPane_1, 0, SpringLayout.SOUTH, HeroPane_1);
		HeroPane_1.setLayout(null);
		
		lblHero_1 = new JLabel("Hero1");
		lblHero_1.setBounds(6, 6, 187, 48);
		lblHero_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblHero_1.setForeground(Color.WHITE);
		lblHero_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		HeroPane_1.add(lblHero_1);
		sl_Player1Panel.putConstraint(SpringLayout.EAST, DeckPane_1, 0, SpringLayout.EAST, Player1Panel);
		Player1Panel.add(DeckPane_1);
		
		JPanel ControlPane_1 = new JPanel();
		ControlPane_1.setOpaque(false);
		sl_Player1Panel.putConstraint(SpringLayout.SOUTH, DeckPane_1, 0, SpringLayout.NORTH, ControlPane_1);
		sl_Player1Panel.putConstraint(SpringLayout.NORTH, ControlPane_1, -40, SpringLayout.SOUTH, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.WEST, ControlPane_1, 0, SpringLayout.WEST, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.SOUTH, ControlPane_1, 0, SpringLayout.SOUTH, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.EAST, ControlPane_1, 0, SpringLayout.EAST, Player1Panel);
		Player1Panel.add(ControlPane_1);
		SpringLayout sl_ControlPane_1 = new SpringLayout();
		ControlPane_1.setLayout(sl_ControlPane_1);

		final JList deckList_1 = new JList();
		deckList_1.setForeground(Color.WHITE);
		deckList_1.setBackground(BACKGROUND_COLOR);
		deckList_1.setOpaque(false);
		DeckPane_1.setViewportView(deckList_1);
		sl_Player1Panel.putConstraint(SpringLayout.NORTH, ControlPane_1, -40, SpringLayout.SOUTH, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.WEST, ControlPane_1, 0, SpringLayout.WEST, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.SOUTH, ControlPane_1, 0, SpringLayout.SOUTH, Player1Panel);
		sl_Player1Panel.putConstraint(SpringLayout.EAST, ControlPane_1, 0, SpringLayout.EAST, Player1Panel);

		HSButton p1_Load = new HSButton("Load...");
		p1_Load.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (fileChooser_ == null) fileChooser_ = new JFileChooser();
				int retVal = fileChooser_.showOpenDialog(frame);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					try {
						DeckListFile deckList = new DeckListFile(fileChooser_.getSelectedFile().toPath());
						deckListModel1_ = new DefaultListModel();
						for (int indx = 0; indx < deckList.getDeck().getNumCards(); ++indx) {
							Card card = deckList.getDeck().drawCard(indx);
							deckListModel1_.addElement("["+card.getMana()+"] " + card.getName());
						}
						deckList_1.setModel(deckListModel1_);
						hsModel_.getSimulation().setDeck_p1(deckList.getDeck());
						hsModel_.getSimulation().setHero_p1(deckList.getHero());
						lblHero_1.setText(deckList.getHero().getName());
					} catch (HSInvalidCardException | HSInvalidHeroException
							| IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(frame,
							    "Error opening the deck file",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		sl_ControlPane_1.putConstraint(SpringLayout.NORTH, p1_Load, 5, SpringLayout.NORTH, ControlPane_1);
		sl_ControlPane_1.putConstraint(SpringLayout.WEST, p1_Load, 5, SpringLayout.WEST, ControlPane_1);
		p1_Load.setPreferredSize(new Dimension(80, 30));
		p1_Load.setForeground(Color.WHITE);
		p1_Load.setBackground(new Color(0, 140, 186));
		ControlPane_1.add(p1_Load);
		
		JButton p1_save = new HSButton("Save...");
		p1_save.setForeground(Color.WHITE);
		p1_save.setBackground(DEFAULT_BUTTON_COLOR);
		p1_save.setPreferredSize(new Dimension(80, 30));
		sl_ControlPane_1.putConstraint(SpringLayout.EAST, p1_save, -5, SpringLayout.EAST, ControlPane_1);
		sl_ControlPane_1.putConstraint(SpringLayout.NORTH, p1_save, 5, SpringLayout.NORTH, ControlPane_1);
		ControlPane_1.add(p1_save);

	}
	
	public void updateInfoPanel() {
		int nWins_0 = hsModel_.getGameStats().getWins_p0();
		int nWins_1 = hsModel_.getGameStats().getWins_p1();
		lblWin_0.setText("" + nWins_0);
		lblWin_1.setText("" + nWins_1);
		lblWinRate_0.setText(pFormatter_.format(100.0 * hsModel_.getGameStats().getWinRate_p0()) + "%");
		lblWinRate_1.setText(pFormatter_.format(100.0 * hsModel_.getGameStats().getWinRate_p1()) + "%");

		try {
			lblConfNum_0.setText(
					pFormatter_.format(100.0 * hsModel_.getGameStats().getWinRateContRange_lower(0.95, nWins_0, nWins_0 + nWins_1)) + "%"
					+ " -- " + 
					pFormatter_.format(100.0 * hsModel_.getGameStats().getWinRateContRange_upper(0.95, nWins_0, nWins_0 + nWins_1)) + "%"
					);
		} catch (Exception e) {
			lblConfNum_0.setText("--");
		}
		
		try {
			lblConfNum_1.setText(
					pFormatter_.format(100.0 * hsModel_.getGameStats().getWinRateContRange_lower(0.95, nWins_1, nWins_0 + nWins_1)) + "%"
					+ " -- " + 
					pFormatter_.format(100.0 * hsModel_.getGameStats().getWinRateContRange_upper(0.95, nWins_1, nWins_0 + nWins_1)) + "%"
					);
		} catch (Exception e) {
			lblConfNum_1.setText("--");
		}
		frame.repaint();
	}

	public void updatePlotPanel() {
		plot_aveMinions.clear(false);
		plot_aveMinions.repaint();
		plot_aveCards.clear(false);
		plot_aveCards.repaint();
		double[] aveMinions_p0 = hsModel_.getGameStats().getAveNumMinions_p0();
		double[] aveMinions_p1 = hsModel_.getGameStats().getAveNumMinions_p1();
		for(int indx = 0; indx < 50; ++indx) {
			plot_aveMinions.addPoint(0, (double)indx, aveMinions_p0[indx], true);
			plot_aveMinions.addPoint(1, (double)indx, aveMinions_p1[indx], true);
		}
		double[] aveCards_p0 = hsModel_.getGameStats().getAveNumCards_p0();
		double[] aveCards_p1 = hsModel_.getGameStats().getAveNumCards_p1();
		for(int indx = 0; indx < 50; ++indx) {
			plot_aveCards.addPoint(0, (double)indx, aveCards_p0[indx], true);
			plot_aveCards.addPoint(1, (double)indx, aveCards_p1[indx], true);
		}
		plot_aveMinions.repaint();
		plot_aveCards.repaint();
	}
	
	@Override
	public void simulationStarted() {
		// TODO Auto-generated method stub
		btnRun.setBackground(ERROR_BUTTON_COLOR);
		btnRun.setText("Stop");
	}

	@Override
	public void simulationFinished() {
		// TODO Auto-generated method stub
		btnRun.setBackground(SUCCESS_BUTTON_COLOR);
		btnRun.setText("Run");
	}
}