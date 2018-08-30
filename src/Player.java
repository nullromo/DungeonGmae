import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class Player
{
	private static int xposition = 1, yposition = 1;
	private static int maxHP, HP, money, strength, armor, mana, maxMana;
	private static int equippedPower, equippedDefence;
	public static ArrayList<Item> inventory = new ArrayList<Item>();
	public static ArrayList<Item> equippedItems = new ArrayList<Item>();
	public static BufferedImage playerImage, hitsplat, block;
	public static ArrayList<String> genericCommands = new ArrayList<String>();
	public static final int PLAYER_IMAGE_X = 110, PLAYER_IMAGE_Y = 160, PLAYER_IMAGE_SIZE = 140; 
	public enum PlayerState{NORMAL,SPLAT};
	public static PlayerState playerState;
	private static int damageDealt, hitsplatX, hitsplatY;
	public static int damageTaken, damageBlocked;
	private static boolean norm = true;
	private static final int HPBarLength = 270, manaBarLength = 270;
	public static long timeOfAttack;
	public static final int HITSPLAT_TIME = 650;
	
	static
	{
		playerState = PlayerState.NORMAL;
		genericCommands.add("north"); genericCommands.add("west");genericCommands.add("s");
		genericCommands.add("south"); genericCommands.add("east");genericCommands.add("e");
		genericCommands.add("unequip"); genericCommands.add("w");genericCommands.add("n");
		genericCommands.add("equip"); genericCommands.add("tp");genericCommands.add("stats");
		genericCommands.add("drink");genericCommands.add("changepicture");genericCommands.add("mute");
		try{
			playerImage = ImageIO.read(Player.class.getResource("/CHARACTER.png"));
			hitsplat = ImageIO.read(Player.class.getResource("/hitsplat.png"));
			block = ImageIO.read(Player.class.getResource("/green.png"));
		}catch(IOException e){e.printStackTrace();
		}
	}

	public static void update()
	{
		switch(Player.playerState)
		{
		case SPLAT:
			if(System.currentTimeMillis() > timeOfAttack + HITSPLAT_TIME + HITSPLAT_TIME)
				Player.playerState = Player.PlayerState.NORMAL;
			break;
		default://NORMAL
			break;
		}
	}
	
	public static void drawPlayer(Graphics g)
	{
		if(Room.getCurrentEntity() instanceof Monster)
			drawCombatHUD(g);
		switch(playerState)
		{
		case NORMAL:
			g.drawImage(playerImage, PLAYER_IMAGE_X, PLAYER_IMAGE_Y, PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE, null);
			break;
		case SPLAT:
			g.drawImage(playerImage, PLAYER_IMAGE_X, PLAYER_IMAGE_Y, PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE, null);
			if(Room.getCurrentEntity() instanceof Monster)
			{	
				g.setColor(Color.WHITE);
				g.setFont(new Font("Courier New", Font.BOLD, 16));
				if(damageTaken-damageBlocked > 0)
				{
					g.drawImage(hitsplat, hitsplatX + PLAYER_IMAGE_X, hitsplatY + PLAYER_IMAGE_Y, 50, 50, null);
					g.drawString(String.valueOf(damageTaken-damageBlocked), hitsplatX+15+PLAYER_IMAGE_X, hitsplatY+30+PLAYER_IMAGE_Y);
				}
				else 
				{
					g.drawImage(block, hitsplatX + PLAYER_IMAGE_X, hitsplatY + PLAYER_IMAGE_Y, 50, 50, null);
					g.drawString(String.valueOf(0), hitsplatX+15+PLAYER_IMAGE_X, hitsplatY+30+PLAYER_IMAGE_Y);
				}
			}
			break;
		}
	}
	
	private static void drawCombatHUD(Graphics g)
	{
		int HPBarFill = HP*HPBarLength/maxHP;
		int manaBarFill = mana*manaBarLength/maxMana;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 400, 60);
		
		g.setColor(Color.RED);
		g.fillRect(10,10,HPBarFill,15);
		g.setColor(new Color(0x1BD3E0));//mana-blue
		g.fillRect(10,35,manaBarFill,15);
		
		g.setColor(Color.BLACK);
		int healthChunkSize = HPBarLength/10;
		int manaChunkSize = manaBarLength/10;
		for(int i=0; i <= HPBarFill / healthChunkSize; i++)
			g.drawLine((healthChunkSize*i)+10, 10, (healthChunkSize*i)+10, 25);
		for(int i=0; i <= manaBarFill / manaChunkSize; i++)
			g.drawLine((manaChunkSize*i)+10, 35, (manaChunkSize*i)+10, 50);
		
		g.setColor(Color.GREEN);
		g.fillRect(390,0,20,60);
		g.setFont(new Font("Courier New",Font.BOLD,18));
		g.drawString("HP:"+String.valueOf(HP),HPBarLength+17,23);
		g.drawString("MP:"+String.valueOf(mana),manaBarLength+17,48);
	}
	
	public static void action(String command)
	{
		String firstWord = command;
		if(command.contains(" "))
			firstWord = command.substring(0,command.indexOf(' '));
		switch (Game.limit)
		{
		case DIFFICULTY:
			if(command.equals("hard") || command.equals("medium") ||command.equals("easy") ||
							command.equals("h") || command.equals("m") || command.equals("e"))
			{
				Game.limit = Game.Limiter.NPC;
				Game.selectDifficulty(command);
			}
			else
				Game.history.append("\nThat isn't a valid difficulty!");
			break;
		case MONSTER:
			if(genericCommands.contains(firstWord))
				genericAction(command);
			else if(command.contains("use"))
				usePotion(command.substring(command.indexOf(' ') + 1));
			else if(command.equals("attack"))
			{
				if (Room.getCurrentEntity() instanceof Monster)
				{
					damageDealt = (int)((strength + equippedPower)*(1 + Math.random()));
					damageMonster(damageDealt);
					((Monster)Room.getCurrentEntity()).monsterTurn();
					timeOfAttack = System.currentTimeMillis();
				}
			}
			else if (command.equals("kill"))
			{
				if (Room.getCurrentEntity() instanceof Monster)
				{
					damageDealt = 999999999;
					damageMonster(damageDealt);
					timeOfAttack = System.currentTimeMillis();
				}
			}
			else if (command.contains("cast"))
				castSpell(command.substring(command.indexOf(' ') + 1));
			else
				Game.history.append("\nYou can't do that.");
			break;
		case SHOP:
			if(genericCommands.contains(firstWord))
				genericAction(command);
			else if (command.contains("purchase"))
				((Shop)Room.getCurrentEntity()).makePurchase(command.substring(command.indexOf(' ') + 1));
			else if (command.contains("sell"))
				((Shop)Room.getCurrentEntity()).makeSale(command.substring(command.indexOf(' ') + 1));
			else
				Game.history.append("\nYou can't do that.");
			break;
		case PUZZLE:
			if(genericCommands.contains(firstWord) && ((Puzzle)Room.getCurrentEntity()).getIsSolved())
				genericAction(command);
			else if (command.contains("answer"))
			{
				if(!((Puzzle)(Room.getCurrentEntity())).getIsSolved())
				{
					if (command.substring(command.indexOf(' ') + 1).equals(((Puzzle)Room.getCurrentEntity()).getAnswer()))
					{
						((Puzzle)Room.getCurrentEntity()).setIsSolved(true);
						Game.history.append("\nThe puzzle in this room has been solved!");
						Game.printHelp();
					}
					else
						Puzzle.puzzleDamager();
				}
			}
			else
				Game.history.append("\nYou can't do that.");
			break;
		case NPC:
			if(genericCommands.contains(firstWord))
				genericAction(command);
			else
				Game.history.append("\nYou can't do that.");
			break;
		case CHEST:
			if(genericCommands.contains(firstWord))
				genericAction(command);
			else if (command.equals("open"))
				((Chest)Room.getCurrentEntity()).openChest();
			else 
				Game.history.append("\nYou can't do that.");
		}
	}
	
	public static void genericAction(String command)
	{
		if ((command.equals("n") || command.equals("north")) && yposition<Game.dungeon.dungeonHeight)//north
		{
			yposition++;
			Game.history.append("\nYou move north.");
			examine();
		}
		
		else if ((command.equals("s") || command.equals("south")) && yposition>1)//south
		{
			yposition--;
			Game.history.append("\nYou move south.");
			examine();
		}
		else if ((command.equals("w") || command.equals("west")) && xposition>1)//west
		{
			xposition--;
			Game.history.append("\nYou move west.");
			examine();
		}
		else if ((command.equals("e") || command.equals("east")) && xposition<Game.dungeon.dungeonWidth)//east
		{
			xposition++;
			Game.history.append("\nYou move east.");
			examine();
		}
		else if (command.contains("tp"))//teleport
		{
			teleport(command.substring(command.indexOf(' ') + 1));
			examine();
		}
		else if (command.contains("changepicture"))
		{
			if(norm)
				try
				{
					Player.playerImage = ImageIO.read(Player.class.getResource("/player2.png"));
					norm = false;
				} catch (IOException e){e.printStackTrace();}
			else
				try
				{
					Player.playerImage = ImageIO.read(Player.class.getResource("/CHARACTER.png"));
					norm = true;
				} catch(IOException e){e.printStackTrace();}
		}
		else if(command.equals("mute"))
		{//empty brackets
			//within the confinement of an else if statement which resides in his stinter establishment which stays in one place, does not move, and stays put at one location in a finite space in the world
				//yolo/     O|O	   A	  8===================D   C===================3	     A	  O|O
//					~~~//##\ | /*\\|//X42Ox42OX--_B_._L_._A_._Z_._E_._G_._O_._D_--X42Ox42OX\\|//*\ | /##\\~~~
//		                     |	   |	  8===================D	  C===================3		 |	   |
//   						 U	  O|O	                                                    O|O    U
		
		}
		else if (command.contains("drink"))
			drinkPotion(command.substring(command.indexOf(' ') + 1));
		else if (command.contains("unequip"))
			unequipItem(command.substring(command.indexOf(' ') + 1));
		else if (command.contains("equip"))
			equipItem(command.substring(command.indexOf(' ') + 1));
		else if (command.equals("stats"))
			printStats();
		else
			Game.history.append("\nYou can't do that.");
	}

	
	public static void examine()
	{
		Room.getCurrentRoom().setExplored();
		Game.history.append("\n----" + Room.getCurrentEntity().getEntityName() + "----");
		if(Room.getCurrentEntity() instanceof Monster)
		{
			Game.limit = Game.Limiter.MONSTER;
			if (Room.getCurrentEntity() instanceof Monster)
				Room.getCurrentEntity().printInfo();
		}
		else if(Room.getCurrentEntity() instanceof NPC)
		{
			Game.limit = Game.Limiter.NPC;
			Room.getCurrentEntity().printInfo();
			((NPC)Room.getCurrentEntity()).sayLore();
		}
		else if(Room.getCurrentEntity() instanceof Chest)
		{
			Game.limit = Game.Limiter.CHEST;
			Room.getCurrentEntity().printInfo();
		}
		else if(Room.getCurrentEntity() instanceof Puzzle)
		{
			Game.limit = Game.Limiter.PUZZLE;
			if (((Puzzle)Room.getCurrentEntity()).getIsSolved() == false)
				Room.getCurrentEntity().printInfo();
			else
				Game.history.append("\nThe puzzle in this room has already been solved!");
		}
		else if(Room.getCurrentEntity() instanceof Shop)
		{
			Game.limit = Game.Limiter.SHOP;
			Room.getCurrentEntity().printInfo();
			((Shop)Room.getCurrentEntity()).printShopItems();
		}
	}

	public static void teleport(String a)
	{
		for (Room room : Game.dungeon.listOfRooms)
			if (a.toUpperCase().equals(room.getEntity().getEntityName()))
			{
				Game.history.append("\nTeleported!");
				xposition = room.getxcoord();
				yposition = room.getycoord();
				return;
			}
		Game.history.append("\nSorry that isnt a valid teleport!");
	}
	
	public static void printStats()
	{
		Game.history.append("\n\n|----------STATS----------|" +
							"\n HP: " + HP + 
							"\n Money: " + money +
							"\n Strength: " + strength +
							"\n Equipped Power: " + equippedPower +
							"\n Armor: " + armor +
							"\n Equipped Defence: " + equippedDefence +
							"\n Position: " + xposition + ", " + yposition +
							"\n|-------------------------|" +
							"\n");
	}

	public static void equipItem(String desiredItem)
	{
		for (Item item : inventory)
		{
			if (item.getName().toLowerCase().equals(desiredItem) && !equippedItems.contains(item))
			{
				if(!equippedItems.isEmpty())
				{
					for (int i = 0; i < equippedItems.size(); i++)
					{
						if (equippedItems.get(i).getLayer() >= item.getLayer())
						{
							equippedItems.add(i, item);
							break;
						}
						else if(i==equippedItems.size()-1)
						{
							equippedItems.add(item);
							break;
						}
					}
				}
				else
					equippedItems.add(item);
				
				//inventory.remove(item);
				Game.history.append("\nYou equipped the " + item.getName() + "!");
				if(item instanceof Weapon)
					equippedPower += item.getStat();
				if(item instanceof Armor)
					equippedDefence += item.getStat();
				return;
			}
		}
		Game.history.append("\nYou can't equip " + desiredItem + "!");
	}
	
	public static void unequipItem(String desiredItem)
	{
		if(desiredItem.equals("all"))
		{
			for(int p=0; p<equippedItems.size();p++)
			{
				//inventory.add(equippedItems.get(p));
				equippedItems.remove(p);
				p--;
			}
			equippedPower = 0;
			Game.history.append("\nYou unequipped everything!");
			return;
		}
		else
		{
			for(Item item : equippedItems)
				if(item.getName().toLowerCase().equals(desiredItem))
				{
					equippedItems.remove(item);
					//inventory.add(item);
					Game.history.append("\nYou unequipped the "+item.getName()+"!");
					if(item instanceof Weapon)
						equippedPower -= item.getStat();
					if(item instanceof Armor)
						equippedDefence -= item.getStat();
					return;
				}
		}
		Game.history.append("\nYou can't unequip " + desiredItem + "!");
	}
	public static void drinkPotion(String potion)
	{
		potion = potion.toUpperCase();
		for(Item item:inventory)
		{
			if(item.getName().toUpperCase().equals(potion))
			{
				if(item instanceof HealthPotion)
				{
					HP += item.getStat();
					inventory.remove(item);
					Game.history.append("\nYou drank the " + potion + "!" +
											"\nIt restored " + item.getStat() + " hitpoints!");
					return;
				}
				else
				{
					Game.history.append("\n" + potion + " is not drinkable!");
					return;
				}
			}
		}
		Game.history.append("\nYou can't drink " + potion + "!");
	}
	
	public static void damageMonster(double dam)
	{
		Monster m = (Monster)(Room.getCurrentEntity());
		int damage = (int)dam;
		m.setHealth(m.getHealth()-damage);
		m.placeMonsterHitsplat();
		Monster.monsterState = Monster.MonsterState.SPLAT;
		Game.history.append("\nYou inflicted " + damage + " damage on the " + m.entityName + "!");
	}
	public static void placePlayerHitsplat()
	{
		setHitsplatX((int)(Math.random() * Player.PLAYER_IMAGE_X/2));
		setHitsplatY((int)(Math.random() * Player.PLAYER_IMAGE_Y/2));
	}
	public static void usePotion(String potion)
	{
		Monster m = (Monster)(Room.getCurrentEntity());
		potion = potion.toUpperCase();
		for(Item item: Player.inventory)
		{
			if(item.getName().toUpperCase().equals(potion))
			{
				if(item instanceof DamagePotion)
				{
					damageMonster(item.getStat());
					inventory.remove(item);
					Game.history.append("\nYou used the " + potion + " on the " + m.entityName + "!" +
											"\nIt dealt " + item.getStat() + " damage!");
					return;
				}
				else
				{
					Game.history.append("\n" + potion + " is not useable!");
					return;
				}
			}
		}
		Game.history.append("\nYou can't use " + potion);
	}
	
	private static void castSpell(String spell)
	{
		//right now just dumps mana
		mana -= Integer.parseInt(spell);
		Game.history.append("\nYou dumped " + Integer.parseInt(spell) + " mana!");
	}

	//sets and gets
	public static int getXPosition() {return xposition;}
	public static int getYPosition() {return yposition;}
	public static void setHP(int h) {HP = h;}
	public static void setMoney(int m) {money = m;}
	public static void setStrength(int s) {strength = s;}
	public static void setArmor(int a) {armor = a;}
	public static int getHP() {return HP;}
	public static int getArmor() {return armor;}
	public static int getStrength() {return strength;}
	public static int getMoney() {return money;}
	public static int getEquippedPower() {return equippedPower;}
	public static int getEquippedDefence() {return equippedDefence;}
	public static void setEquippedDefence(int def) {equippedDefence = def;}
	public static void setEquippedPower(int pow) {equippedPower = pow;}
	public static void setHitsplatX(int x){hitsplatX = x;}
	public static void setHitsplatY(int y){hitsplatY = y;}
	public static int getDamageDealt(){return damageDealt;}
	public static void setMana(int m){mana = m;}
	public static int getMana(){return mana;}
	public static int getMaxHP(){return maxHP;}
	public static void setMaxHP(int m){maxHP = m;}
	public static int getMaxMana(){return maxMana;}
	public static void setMaxMana(int m){maxMana = m;}
}