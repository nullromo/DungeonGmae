import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.plaf.synth.*;
import javax.swing.text.*;

public class Game extends KeyAdapter
{
	//public static final BufferedImage HITSPLAT = utilitySheet.getSubimage(3*32,3*32,32,32);
	private static JFrame mainGameFrame;
	private static JPanel textPanel, helpPanel, inventoryPanel, topPanel;
	public  static GraphicsDrawer graphicsPanel;
	public  static MapDrawer mapPanel;
	public  static JTextArea help, history;
	public  static JTextField inputLine;
	public  static JScrollPane historyScrollPane, helpScrollPane;
	private static String difficulty;
	public  enum   Limiter{SHOP,PUZZLE,MONSTER,NPC,CHEST,DIFFICULTY};
	public  static Limiter limit;
	private static BufferedImage graphicsBuffer = new BufferedImage(800,350,BufferedImage.TYPE_INT_RGB),
								mapBuffer = new BufferedImage(350,350,BufferedImage.TYPE_INT_RGB),
								inventoryBuffer = new BufferedImage(350,350,BufferedImage.TYPE_INT_RGB),
								gameLogo;
	
	private static boolean difficultySelected = false;
	public  static Dungeon dungeon;

	
	public Game()
	{
		//create
		SynthLookAndFeel dungeonLAF = new SynthLookAndFeel();
		try
		{
			dungeonLAF.load(Game.class.getResource("/DungeonUI.xml"));
			UIManager.setLookAndFeel(dungeonLAF);
			gameLogo = ImageIO.read(Game.class.getResource("/DungeonLogo.png"));
		}catch(Exception e){e.printStackTrace();history.append("\nRuntime error: XML loading error");}
		graphicsPanel = new GraphicsDrawer();
		mapPanel = new MapDrawer();
		inventoryPanel = new InventoryDrawer();
		topPanel = new JPanel();
		mainGameFrame = new JFrame("DUNGEON GMAE");
		textPanel = new JPanel();
		helpPanel = new JPanel();
		history = new JTextArea("Select Difficulty.\nEnter 'easy' 'medium' or 'hard'");
		help = new JTextArea();
		historyScrollPane = new JScrollPane(history);
		helpScrollPane = new JScrollPane(help);
		inputLine = new JTextField()
						{
							private static final long serialVersionUID = 4L;
							public void addNotify()
							{
								super.addNotify();
								requestFocus();
							}
						};
		
		
		//set sizes
		helpScrollPane.setPreferredSize(new Dimension(215,350));
		inventoryPanel.setPreferredSize(new Dimension(350,350));
		mainGameFrame.setPreferredSize(new Dimension(1150, 740));
		graphicsPanel.setPreferredSize(new Dimension(800,350));
		mapPanel.setPreferredSize(new Dimension(350,350));
		
		//set attributes
		mainGameFrame.setResizable(false);
		
		history.setEditable(false);
		history.setLineWrap(true);
		help.setEditable(false);
		textPanel.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());
		mainGameFrame.setLayout(new BorderLayout());
		historyScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		helpScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		historyScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		helpScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DefaultCaret caret = (DefaultCaret)inputLine.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		inputLine.addKeyListener(this);
		
		//add
		helpPanel.add(helpScrollPane);
		topPanel.add(graphicsPanel, BorderLayout.WEST);
		topPanel.add(mapPanel,BorderLayout.EAST);
		textPanel.add(historyScrollPane, BorderLayout.CENTER);
		textPanel.add(inputLine, BorderLayout.SOUTH);
		
		mainGameFrame.add(inventoryPanel, BorderLayout.EAST);
		mainGameFrame.add(topPanel,BorderLayout.NORTH);
		mainGameFrame.add(helpPanel, BorderLayout.WEST);
		mainGameFrame.add(textPanel, BorderLayout.CENTER);
		mainGameFrame.pack();
		mainGameFrame.setLocationRelativeTo(null);
		mainGameFrame.setVisible(true);
		
		graphicsBuffer.getGraphics().drawImage(gameLogo,0,0,800,350,null);
	}
	
	public static void main(String[] args)
	{
		try
		{
			dungeon = new Dungeon(9,9);
		}catch (Exception e){e.printStackTrace();
		}
		new Game();
		limit = Limiter.DIFFICULTY;
		printHelp();
		//Sound.playSound(Sound.backgroundMusic);
		new Thread("GRAPHICS")
		{
			public void run()
			{
				while(true)
				{
					//main panel
					Graphics g = graphicsBuffer.getGraphics();
					if(difficultySelected)
					{
						Room.drawRoom(g);
						if(Room.getCurrentRoom().getEntity() != null)
							Room.getCurrentRoom().getEntity().drawEntity(g);
						Player.drawPlayer(g);
						
						for(Item item: Player.equippedItems)
							item.drawItem(g);
						graphicsPanel.repaint();
						
						//map
						g = mapBuffer.getGraphics();
						Map.drawMap(g);
						mapPanel.repaint();
						
						//inventory
						g = inventoryBuffer.getGraphics();
						InventoryDisplay.drawInventory(g);
						inventoryPanel.repaint();
						
						g.dispose();
							
						try{
							Thread.sleep(17);
						}catch(InterruptedException e){e.printStackTrace();}
					}
				}
			}
		}.start();
		
		new Thread("UPDATE")
		{
			public void run()
			{
				while(true)
				{
					Player.update();
					Room.getCurrentRoom().getEntity().update();
					for(Entity chest: dungeon.listOfChests)
						chest.update();
					
					try
					{
						Thread.sleep(17);
					}catch(InterruptedException e){e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public class GraphicsDrawer extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		public void paint(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(graphicsBuffer,0,0,null);	
		}
	}
	
	public class MapDrawer extends JPanel
	{
		private static final long serialVersionUID = 2L;
		
		public void paint(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(mapBuffer,0,0,null);
		}
	}
	public class InventoryDrawer extends JPanel
	{
		private static final long serialVersionUID = 3L;
		
		public void paint(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(inventoryBuffer,0,0,null);
		}
	}
	
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_BACK_SPACE)
			Sound.keyBackSpace.play();
		else if(key == KeyEvent.VK_ENTER)
		{
			Sound.keyReturn.play();
			Player.action(inputLine.getText().toLowerCase());
			history.append("\n");
			inputLine.setText("");
			printHelp();
			help.setCaretPosition(0);
			history.setCaretPosition(history.getDocument().getLength());
		}
		else
			Sound.keyClick.play();
	}
	
	public static void printHelp()
	{
		switch (limit)
		{
		case DIFFICULTY:
			help.setText("COMMANDS:" +
					"\n|----------|------------|" +
					"\n| 'easy'   |   select   |" +
					"\n|  'e'     |    easy    |" +
					"\n|----------|------------|" +
					"\n| 'medium' |   select   |" +
					"\n|   'm'    |   medium   |" +
					"\n|----------|------------|" +
					"\n| 'hard'   |   select   |" +
					"\n|  'h'     |    hard    |" +
					"\n|----------|------------|" );
			return;
		case MONSTER:
			help.setText("COMMANDS:" +
					"\n|----------|------------|" +
					"\n|          | attack the |" +
					"\n| 'attack' | monster    |" +
					"\n|          | normally   |" +
					"\n|----------|------------|" +
					"\n|          | magically  |" +
					"\n| 'kill'   | kill the   |" +
					"\n|          | monster    |" +
					"\n|----------|------------|" +
					"\n|          | check      |" +
					"\n| 'stats'  | stats      |" +
					"\n|          |            |" +
					"\n|----------|------------|" +
					"\n| 'equip'  | equip an   |" +
					"\n|  +  '_'  | item       |" +
					"\n|----------|------------|" +
					"\n| 'unequip'| unequip an |" +
					"\n|  +  '_'  | item       |" +
					"\n|----------|------------|" +
					"\n| 'use' +  | use an atk |" +
					"\n|  '_'     | potion     |" +
					"\n|----------|------------|" +
					"\n| 'drink'+ |  drink an  |" +
					"\n|  '_'     |  HP potion |" +
					"\n|----------|------------|" );
			return;
		case PUZZLE:
			help.setText("COMMANDS:" +
					"\n|----------|------------|" +
					"\n|'answer _'|  attepmt   |" +
					"\n|          |  an answer |" +
					"\n|----------|------------|" +
					"\n| 'stats'  |  check     |" +
					"\n|          |  stats     |" +
					"\n|----------|------------|" +
					"\n| 'equip'  |  equip an  |" +
					"\n|  +  '_'  |  item      |" +
					"\n|----------|------------|" +
					"\n| 'unequip'|  unequip an|" +
					"\n|  +  '_'  |  item      |" +
					"\n|----------|------------|" +
					"\n| 'drink'+ |  drink an  |" +
					"\n|  '_'     |  HP potion |" +
					"\n|----------|------------|" );
			return;
		case SHOP:
			help.setText("COMMANDS:" +
					"\n|----------|------------|" +
					"\n|'purchase'|  make a    |" +
					"\n|   +  '_' |  purchase  |" +
					"\n|----------|------------|" );
			break;
		case CHEST:
			help.setText("COMMANDS:" +
					"\n|----------|------------|" +
					"\n|  'open'  |  attempt   |" +
					"\n|          |  an open   |" +
					"\n|----------|------------|" );
			break;
		case NPC:
			help.setText("COMMANDS:" +
					"\n|----------|------------|" );
			break;
		}
		help.append("\n| 'stats'  |  check     |" +
					"\n|          |  stats     |" +
					"\n|----------|------------|" +
					"\n| 'equip'  |  equip an  |" +
					"\n|  +  '_'  |  item      |" +
					"\n|----------|------------|" +
					"\n| 'unequip'|  unequip an|" +
					"\n|  +  '_'  |  item      |" +
					"\n|----------|------------|" +
					"\n| 'north'  |  move      |" +
					"\n|          |  north     |" +
					"\n|----------|------------|" +
					"\n| 'south'  |  move      |" +
					"\n|          |  south     |" +
					"\n|----------|------------|" +
					"\n| 'east'   |  move      |" +
					"\n|          |  east      |" +
					"\n|----------|------------|" +
					"\n| 'west'   |  move      |" +
					"\n|          |  west      |" +
					"\n|----------|------------|" +
					"\n|  'tp _'  |  teleport  |" +
					"\n|          |            |" +
					"\n|----------|------------|" +
					"\n| 'drink'+ |  drink an  |" +
					"\n|  '_'     |  HP potion |" +
					"\n|----------|------------|" +
					"\n| 'use  '+ |   use a    |" +
					"\n|  '_'     |CMBT potion |" +
					"\n|----------|------------|" );
	}

	public static void selectDifficulty(String command)
	{
		
		if(command.substring(0,1).toUpperCase().equals("E"))
		{
			difficulty = "easy";
			Player.setHP(10000);
			Player.setMoney(2000);
			Player.setArmor(20);
			Player.setStrength(70);
			Player.setMaxHP(10000);
			Player.setMaxMana(1000);
			Player.setMana(1000);
		}
		else if (command.substring(0,1).toUpperCase().equals("M"))
		{
			difficulty = "medium";
			Player.setHP(8000);
			Player.setMoney(1000);
			Player.setArmor(10);
			Player.setStrength(40);
			Player.setMaxHP(8000);
			Player.setMaxMana(800);
			Player.setMana(800);
		}
		else if (command.substring(0,1).toUpperCase().equals("H"))
		{
			difficulty = "hard";
			Player.setHP(5000);
			Player.setMoney(0);
			Player.setArmor(0);
			Player.setStrength(20);
			Player.setMaxHP(5000);
			Player.setMaxMana(500);
			Player.setMana(500);
		}
		difficultySelected = true;
		history.setText("");
		history.append("Your game difficulty has been set to " + difficulty + ".");
		history.append("\nYou have been sent by the town to eradicate "+
				"all 20 of the ferocious  monsters in this "+
				"81-room dungeon."+
				" The rooms are set up in a 9x9 grid."+
				"Good luck; your epic quest begins now...");
		history.append("\n");
		Player.examine();
	}
}

/*	monsterAmplifiers = new double[]
			{
				3,1,.9,3,1.2,
				2,2,1.5,1,1.5,
				1.1,1,1.3,7,2,
				1.7,1.2,4,1.5,600
			};*/