import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class Dungeon
{
	public static ArrayList<String> lore;
	public ArrayList<Room> listOfRooms = new ArrayList<Room>();
	public ArrayList<Entity> listOfEntities = new ArrayList<Entity>();
	public ArrayList<Entity> listOfMonsters,listOfChests,listOfNPCs,listOfShops,listOfPuzzles;
	public ArrayList<Item> allWeapons, allHealthPotions, allDamagePotions, allArmor;
	public ArrayList<ArrayList<Item>> allItems;
	public boolean roomsGenerated = false;
	public int dungeonWidth, dungeonHeight, numberOfMonsters,numberOfChests,numberOfNPCs,
				numberOfShops,numberOfPuzzles,numberOfNulls;
	private static BufferedImage monsterSheet, chestSheet, shopSheet, NPCSheet, weaponSheet,
								armorSheet, combatPotionSheet, healthPotionSheet,utilitySheet;
	static
	{
		try
		{
			 monsterSheet      = ImageIO.read(Dungeon.class.getResource("sheets/monsterSheet.png"));
			 chestSheet        = ImageIO.read(Dungeon.class.getResource("sheets/chestSheet.png"));
			 shopSheet         = ImageIO.read(Dungeon.class.getResource("sheets/shopSheet.png"));
			 NPCSheet          = ImageIO.read(Dungeon.class.getResource("sheets/NPCSheet.png"));
			 weaponSheet       = ImageIO.read(Dungeon.class.getResource("sheets/weaponSheet.png"));
			 armorSheet        = ImageIO.read(Dungeon.class.getResource("sheets/armorSheet.png"));
			 combatPotionSheet = ImageIO.read(Dungeon.class.getResource("sheets/combatPotionSheet.png"));
			 healthPotionSheet = ImageIO.read(Dungeon.class.getResource("sheets/healthPotionSheet.png"));
			 utilitySheet = ImageIO.read(Dungeon.class.getResource("sheets/utilitySheet.png"));
		}catch(IOException e){e.printStackTrace();
		}
	}
	
	public Dungeon(int width, int height)
	{
		dungeonWidth = width;
		dungeonHeight = height;
		initDungeon();
		genRooms();
	}
	
	public Dungeon(int width, int height, int numMonsters, int numChests, int numNPCs,
			int numShops, int numPuzzles) throws Exception
	{
		if((numMonsters + numChests + numNPCs + numShops + numPuzzles) > width * height) 
			throw new Exception("Too many rooms!");
		dungeonWidth = width;
		dungeonHeight = height;
		numberOfMonsters = numMonsters;
		numberOfChests = numChests;
		numberOfNPCs = numNPCs;
		numberOfShops = numShops;
		numberOfPuzzles = numPuzzles;
		numberOfNulls = (width*height) - numMonsters - numChests - numNPCs - numShops - numPuzzles;
		initDungeon();
		selectEntities();
		genRooms();
	}
	
	private void initDungeon()
	{
		genAllShit();
		for(Entity e: listOfMonsters)
			listOfEntities.add(e);
		for(Entity e: listOfChests)
			listOfEntities.add(e);
		for(Entity e: listOfNPCs)
			listOfEntities.add(e);
		for(Entity e: listOfShops)
			listOfEntities.add(e);
		for(Entity e: listOfPuzzles)
			listOfEntities.add(e);
	}
	
	public void selectEntities()
	{
		Random rand = new Random();
		while(listOfMonsters.size()>numberOfMonsters)
			listOfMonsters.remove(rand.nextInt(listOfMonsters.size()));
		while(listOfChests.size()>numberOfChests)
			listOfChests.remove(rand.nextInt(listOfChests.size()));
		while(listOfNPCs.size()>numberOfNPCs)
			listOfNPCs.remove(rand.nextInt(listOfNPCs.size()));
		while(listOfShops.size()>numberOfShops)
			listOfShops.remove(rand.nextInt(listOfShops.size()));
		while(listOfPuzzles.size()>numberOfPuzzles)
			listOfPuzzles.remove(rand.nextInt(listOfPuzzles.size()));
	}
	
	private void genAllShit()
	{
		//create lore array
		lore = new ArrayList<String>();
		
		//populate lore array
		lore.add("\"I am holy.\nSatan is good and holy. Praise Satan.\"");
		lore.add("\"...\"");
		lore.add("\"You soy estoy un professor.\"");
		lore.add("\"Have you seen any monsters around here?\"");
		lore.add("\"Gee mister, this dungeon sure is swell!\"");
		lore.add("\"Hi there!\"");
		lore.add("\"Have you seen any littleBoys around here anywhere?\"");
		lore.add("\"Meow.\"");
		lore.add("\"BOO!\"");
		lore.add("\"How you hamd?\"");
		lore.add("\"I'M A WIZARD\"");
		
		//create item arrays
		allWeapons = new ArrayList<Item>();
		allDamagePotions = new ArrayList<Item>();
		allHealthPotions = new ArrayList<Item>();
		allArmor = new ArrayList<Item>();
		allItems = new ArrayList<ArrayList<Item>>();
		
		//populate item arrays
		allWeapons.add(new Weapon(Item.Type.WEAPON,"AX"        ,  1, 64,  2,weaponSheet.getSubimage(0*32,0*32,32,32)));//row 1
		allWeapons.add(new Weapon(Item.Type.WEAPON,"BATTLEAXE" ,  1, 25,  2,weaponSheet.getSubimage(1*32,0*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"BROADSWORD",  1, 45,  2,weaponSheet.getSubimage(2*32,0*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"DAGGER"    ,  1, 12,  2,weaponSheet.getSubimage(3*32,0*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"FLAIL"     ,  1, 12,  2,weaponSheet.getSubimage(4*32,0*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"HALBERD"   ,  1,100,  2,weaponSheet.getSubimage(0*32,1*32,32,32)));//row 2
		allWeapons.add(new Weapon(Item.Type.WEAPON,"KNIFE"     ,  1, 15,  4,weaponSheet.getSubimage(1*32,1*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"LONGSWORD" ,  1, 30,  2,weaponSheet.getSubimage(2*32,1*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"MACE"      ,  1, 47,  2,weaponSheet.getSubimage(3*32,1*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"MAUL"      ,  1, 45,  2,weaponSheet.getSubimage(4*32,1*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"PIKE"      ,  1, 60,  2,weaponSheet.getSubimage(0*32,2*32,32,32)));//row 3
		allWeapons.add(new Weapon(Item.Type.WEAPON,"RAPIER"    ,  1, 23,  2,weaponSheet.getSubimage(1*32,2*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"SCIMITAR"  ,  1, 86,  2,weaponSheet.getSubimage(2*32,2*32,32,32)));
		allWeapons.add(new Weapon(Item.Type.WEAPON,"WARHAMMER" ,  1, 94,  2,weaponSheet.getSubimage(3*32,2*32,32,32)));
		
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"ACID POTION"            ,  1, 56,combatPotionSheet.getSubimage(0*32,0*32,32,32)));//row 1
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"DARK POTION"            ,  1, 80,combatPotionSheet.getSubimage(1*32,0*32,32,32)));
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"FLAME POTION"           ,  1, 67,combatPotionSheet.getSubimage(2*32,0*32,32,32)));
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"FREEZE POTION"          ,  1, 34,combatPotionSheet.getSubimage(3*32,0*32,32,32)));
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"POTION OF INSTANT DEATH",  1,99999,combatPotionSheet.getSubimage(4*32,0*32,32,32)));
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"POTION OF LIGHT"        ,  1,  4,combatPotionSheet.getSubimage(0*32,1*32,32,32)));//row 2
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"POTION OF PAIN"         ,  1,100,combatPotionSheet.getSubimage(1*32,1*32,32,32)));
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"SHOCK POTION"           ,  1, 96,combatPotionSheet.getSubimage(2*32,1*32,32,32)));
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"SLIME POTION"           ,  1, 23,combatPotionSheet.getSubimage(3*32,1*32,32,32)));
		allDamagePotions.add(new DamagePotion(Item.Type.DMGPOTION,"SMOKE POTION"           ,  1, 76,combatPotionSheet.getSubimage(4*32,1*32,32,32)));
		
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"BEER"                ,  1,-100,healthPotionSheet.getSubimage(0*32,0*32,32,32)));//row 1
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"GIGA HEALTH POTION"  ,  1,1500,healthPotionSheet.getSubimage(1*32,0*32,32,32)));
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"HEALTH POTION"       ,  1,  25,healthPotionSheet.getSubimage(2*32,0*32,32,32)));
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"MEGA HEALTH POTION"  ,  1,1000,healthPotionSheet.getSubimage(3*32,0*32,32,32)));
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"POTION OF HEALING"   ,  1,  75,healthPotionSheet.getSubimage(4*32,0*32,32,32)));
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"POTION OF LIFE"      ,  1, 500,healthPotionSheet.getSubimage(0*32,1*32,32,32)));//row 2
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"WEAK HEALTH POTION"  ,  1,  10,healthPotionSheet.getSubimage(1*32,1*32,32,32)));
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"STRONG HEALTH POTION",  1,  50,healthPotionSheet.getSubimage(2*32,1*32,32,32)));
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"SUPER HEALTH POTION" ,  1, 500,healthPotionSheet.getSubimage(3*32,1*32,32,32)));
		allHealthPotions.add(new HealthPotion(Item.Type.HEALPOTION,"ULTRA HEALTH POTION" ,  1, 100,healthPotionSheet.getSubimage(4*32,1*32,32,32)));
		
		allArmor.add(new Armor(Item.Type.ARMOR,"AMULET II"         ,1,1,6,armorSheet.getSubimage(0*32,0*32,32,32)));//row 1
		allArmor.add(new Armor(Item.Type.ARMOR,"AMULET"            ,1,1,6,armorSheet.getSubimage(1*32,0*32,32,32)));
		allArmor.add(new Armor(Item.Type.ARMOR,"BOOTS"             ,1,1,3,armorSheet.getSubimage(2*32,0*32,32,32)));
		allArmor.add(new Armor(Item.Type.ARMOR,"BUCKLER"           ,1,1,5,armorSheet.getSubimage(3*32,0*32,32,32)));
		allArmor.add(new Armor(Item.Type.ARMOR,"CHAINBODY"         ,1,1,0,armorSheet.getSubimage(4*32,0*32,32,32)));
		allArmor.add(new Armor(Item.Type.ARMOR,"GAUNTLETS"         ,1,1,1,armorSheet.getSubimage(0*32,1*32,32,32)));//row 2
		allArmor.add(new Armor(Item.Type.ARMOR,"HELMET"            ,1,1,6,armorSheet.getSubimage(1*32,1*32,32,32)));
		allArmor.add(new Armor(Item.Type.ARMOR,"KITESHIELD"        ,1,1,5,armorSheet.getSubimage(2*32,1*32,32,32)));
		allArmor.add(new Armor(Item.Type.ARMOR,"PLATELEGS"         ,1,1,3,armorSheet.getSubimage(3*32,1*32,32,32)));
		allArmor.add(new Armor(Item.Type.ARMOR,"RING OF PROTECTION",1,1,6,armorSheet.getSubimage(4*32,1*32,32,32)));
		allArmor.add(new Armor(Item.Type.ARMOR,"SQUARE SHIELD"     ,1,1,5,armorSheet.getSubimage(0*32,2*32,32,32)));//row 3
		
		//add item arrays to master item array
		allItems.add(allArmor);
		allItems.add(allDamagePotions);
		allItems.add(allHealthPotions);
		allItems.add(allWeapons);
		
		//create the entity arrays
		listOfMonsters = new ArrayList<Entity>();
		listOfChests = new ArrayList<Entity>();
		listOfNPCs = new ArrayList<Entity>();
		listOfShops = new ArrayList<Entity>();
		listOfPuzzles = new ArrayList<Entity>();
		
		//populate master entity list
		listOfMonsters.add(new Monster("BASILISK", 500, 50 , 600 , 450, 70 , 250, 250, monsterSheet.getSubimage(0*32,0*32,32,32)));//row 1
		listOfMonsters.add(new Monster("BLOB",     500, 2  , 200 , 480, 180, 130, 130, monsterSheet.getSubimage(1*32,0*32,32,32)));
		listOfMonsters.add(new Monster("CHIMERA", 1200, 60 , 1000, 320, 50 , 330, 330, monsterSheet.getSubimage(2*32,0*32,32,32)));
		listOfMonsters.add(new Monster("CYCLOPS",  600, 45 , 900 , 440, 130, 170, 170, monsterSheet.getSubimage(3*32,0*32,32,32)));
		listOfMonsters.add(new Monster("DRAGON",  2000, 100, 3000, 320, 20 , 310, 310, monsterSheet.getSubimage(4*32,0*32,32,32)));
		listOfMonsters.add(new Monster("ELF",      100, 100, 800 , 520, 150, 140, 140, monsterSheet.getSubimage(0*32,1*32,32,32)));//row 2
		listOfMonsters.add(new Monster("ENT",     3000, 25 , 400 , 320, 50 , 330, 330, monsterSheet.getSubimage(1*32,1*32,32,32)));
		listOfMonsters.add(new Monster("FROG",      20, 1  , 100 , 360, 30 , 300, 300, monsterSheet.getSubimage(2*32,1*32,32,32)));
		listOfMonsters.add(new Monster("HOBGOBLIN",700, 20 , 700 , 520, 150, 140, 140, monsterSheet.getSubimage(3*32,1*32,32,32)));
		listOfMonsters.add(new Monster("HYDRA",    900, 50 , 900 , 320, 50 , 330, 330, monsterSheet.getSubimage(4*32,1*32,32,32)));
		listOfMonsters.add(new Monster("IMP",      400, 4  , 450 , 320, 50 , 330, 330, monsterSheet.getSubimage(0*32,2*32,32,32)));//row 3
		listOfMonsters.add(new Monster("MINOTAUR", 850, 55 , 850 , 400, 120, 200, 200, monsterSheet.getSubimage(1*32,2*32,32,32)));
		listOfMonsters.add(new Monster("ORC",      650, 20 , 650 , 320, 50 , 330, 330, monsterSheet.getSubimage(2*32,2*32,32,32)));
		listOfMonsters.add(new Monster("SORCERER", 600, 65 , 800 , 320, 50 , 330, 330, monsterSheet.getSubimage(3*32,2*32,32,32)));
		listOfMonsters.add(new Monster("TROLL",   2500, 15 , 1150, 400, 80 , 210, 210, monsterSheet.getSubimage(4*32,2*32,32,32)));
		listOfMonsters.add(new Monster("UNICORN",  400, 37 , 800 , 390, 105, 200, 200, monsterSheet.getSubimage(0*32,3*32,32,32)));//row 4
		listOfMonsters.add(new Monster("VAMPIRE", 1200, 50 , 1000, 400, 120, 200, 200, monsterSheet.getSubimage(1*32,3*32,32,32)));
		listOfMonsters.add(new Monster("WYVERN",   700, 20 , 700 , 320, 50 , 330, 330, monsterSheet.getSubimage(2*32,3*32,32,32)));
		listOfMonsters.add(new Monster("YETI",    1300, 30 , 1600, 400, 50 , 250, 250, monsterSheet.getSubimage(3*32,3*32,32,32)));
		listOfMonsters.add(new Monster("ZOMBIE",   500, 20 , 300 , 320, 50 , 330, 330, monsterSheet.getSubimage(4*32,3*32,32,32)));
		
		listOfChests.add(new Chest("ADAMANT"    ,allItems, chestSheet.getSubimage(0*32,0*32,32,32)));//row 1
		listOfChests.add(new Chest("BIRCH"      ,allItems, chestSheet.getSubimage(1*32,0*32,32,32)));
		listOfChests.add(new Chest("CEDAR"      ,allItems, chestSheet.getSubimage(2*32,0*32,32,32)));
		listOfChests.add(new Chest("CHERRY"     ,allItems, chestSheet.getSubimage(3*32,0*32,32,32)));
		listOfChests.add(new Chest("COBALT"     ,allItems, chestSheet.getSubimage(4*32,0*32,32,32)));
		listOfChests.add(new Chest("CORK"       ,allItems, chestSheet.getSubimage(0*32,1*32,32,32)));//row 2
		listOfChests.add(new Chest("DIAMOND"    ,allItems, chestSheet.getSubimage(1*32,1*32,32,32)));
		listOfChests.add(new Chest("DRAGONSCALE",allItems, chestSheet.getSubimage(2*32,1*32,32,32)));
		listOfChests.add(new Chest("DRIFTWOOD"  ,allItems, chestSheet.getSubimage(3*32,1*32,32,32)));
		listOfChests.add(new Chest("EBONY"      ,allItems, chestSheet.getSubimage(4*32,1*32,32,32)));
		listOfChests.add(new Chest("EMERALD"    ,allItems, chestSheet.getSubimage(0*32,2*32,32,32)));//row 3
		listOfChests.add(new Chest("IRON"       ,allItems, chestSheet.getSubimage(1*32,2*32,32,32)));
		listOfChests.add(new Chest("IVORY"      ,allItems, chestSheet.getSubimage(2*32,2*32,32,32)));
		listOfChests.add(new Chest("MAHOGANY"   ,allItems, chestSheet.getSubimage(3*32,2*32,32,32)));
		listOfChests.add(new Chest("MAPLE"      ,allItems, chestSheet.getSubimage(4*32,2*32,32,32)));
		listOfChests.add(new Chest("MYTHRIL"    ,allItems, chestSheet.getSubimage(0*32,3*32,32,32)));//row 4
		listOfChests.add(new Chest("OAK"        ,allItems, chestSheet.getSubimage(1*32,3*32,32,32)));
		listOfChests.add(new Chest("PEARL"      ,allItems, chestSheet.getSubimage(2*32,3*32,32,32)));
		listOfChests.add(new Chest("REDWOOD"    ,allItems, chestSheet.getSubimage(3*32,3*32,32,32)));
		listOfChests.add(new Chest("RUBY"       ,allItems, chestSheet.getSubimage(4*32,3*32,32,32)));
		listOfChests.add(new Chest("SAPPHIRE"   ,allItems, chestSheet.getSubimage(0*32,4*32,32,32)));//row 5
		listOfChests.add(new Chest("SILVER"     ,allItems, chestSheet.getSubimage(1*32,3*32,32,32)));
		listOfChests.add(new Chest("SPIRIT"     ,allItems, chestSheet.getSubimage(2*32,4*32,32,32)));
		listOfChests.add(new Chest("WALNUT"     ,allItems, chestSheet.getSubimage(3*32,4*32,32,32)));
		listOfChests.add(new Chest("YEW"        ,allItems, chestSheet.getSubimage(4*32,4*32,32,32)));
		
		listOfShops.add(new Shop("ARMOR SHOP"        , allArmor        , shopSheet.getSubimage(0*32,0*32,32,32)));//row 1
		listOfShops.add(new Shop("COMBAT POTION SHOP", allDamagePotions, shopSheet.getSubimage(1*32,0*32,32,32)));
		listOfShops.add(new Shop("HEALTH POTION SHOP", allHealthPotions, shopSheet.getSubimage(2*32,0*32,32,32)));
		listOfShops.add(new Shop("WEAPON SHOP"       , allWeapons      , shopSheet.getSubimage(3*32,0*32,32,32)));
		
		listOfNPCs.add(new NPC(lore.get(0),"CLERIC"    , NPCSheet.getSubimage(0*32,0*32,32,32)));//row 1
		listOfNPCs.add(new NPC(lore.get(1),"DEAD JOE"  , NPCSheet.getSubimage(1*32,0*32,32,32)));
		listOfNPCs.add(new NPC(lore.get(5),"EXPLORER"  , NPCSheet.getSubimage(2*32,0*32,32,32)));
		listOfNPCs.add(new NPC(lore.get(8),"GHOST"     , NPCSheet.getSubimage(3*32,0*32,32,32)));
		listOfNPCs.add(new NPC(lore.get(6),"HERMIT"    , NPCSheet.getSubimage(4*32,0*32,32,32)));
		listOfNPCs.add(new NPC(lore.get(3),"HUNTER"    , NPCSheet.getSubimage(0*32,1*32,32,32)));//row 2
		listOfNPCs.add(new NPC(lore.get(4),"LITTLE BOY", NPCSheet.getSubimage(1*32,1*32,32,32)));
		listOfNPCs.add(new NPC(lore.get(7),"LOST CAT"  , NPCSheet.getSubimage(2*32,1*32,32,32)));
		listOfNPCs.add(new NPC(lore.get(2),"PROFESSOR" , NPCSheet.getSubimage(3*32,1*32,32,32)));
		listOfNPCs.add(new NPC(lore.get(9),"THIEF"     , NPCSheet.getSubimage(4*32,1*32,32,32)));
		
		//screw puzzles
		listOfMonsters.add(new Monster("DUP___BASILISK", 500, 50 , 600 , 450, 70 , 250, 250,monsterSheet.getSubimage(0*32,0*32,32,32)));//row 1
		listOfMonsters.add(new Monster("DUP___BLOB",     500, 2  , 200 , 480, 180, 130, 130,monsterSheet.getSubimage(1*32,0*32,32,32)));
		listOfMonsters.add(new Monster("DUP___CHIMERA", 1200, 60 , 1000, 320, 50 , 330, 330,monsterSheet.getSubimage(2*32,0*32,32,32)));
		listOfMonsters.add(new Monster("DUP___CYCLOPS",  600, 45 , 900 , 440, 130, 170, 170,monsterSheet.getSubimage(3*32,0*32,32,32)));
		listOfMonsters.add(new Monster("DUP___DRAGON",  2000, 100, 3000, 320, 20 , 310, 310,monsterSheet.getSubimage(4*32,0*32,32,32)));
		listOfMonsters.add(new Monster("DUP___ELF",      100, 100, 800 , 520, 150, 140, 140,monsterSheet.getSubimage(0*32,1*32,32,32)));//row 2
		listOfMonsters.add(new Monster("DUP___ENT",     3000, 25 , 400 , 320, 50 , 330, 330,monsterSheet.getSubimage(1*32,1*32,32,32)));
		listOfMonsters.add(new Monster("DUP___FROG",      20, 1  , 100 , 360, 30 , 300, 300,monsterSheet.getSubimage(2*32,1*32,32,32)));
		listOfMonsters.add(new Monster("DUP___HOBGOBLIN",700, 20 , 700 , 520, 150, 140, 140,monsterSheet.getSubimage(3*32,1*32,32,32)));
		listOfMonsters.add(new Monster("DUP___HYDRA",    900, 50 , 900 , 320, 50 , 330, 330,monsterSheet.getSubimage(4*32,1*32,32,32)));
		listOfMonsters.add(new Monster("DUP___IMP",      400, 4  , 450 , 320, 50 , 330, 330,monsterSheet.getSubimage(0*32,2*32,32,32)));//row 3
		listOfMonsters.add(new Monster("DUP___MINOTAUR", 850, 55 , 850 , 400, 120, 200, 200,monsterSheet.getSubimage(1*32,2*32,32,32)));
		listOfMonsters.add(new Monster("DUP___ORC",      650, 20 , 650 , 320, 50 , 330, 330,monsterSheet.getSubimage(2*32,2*32,32,32)));
		listOfMonsters.add(new Monster("DUP___SORCERER", 600, 65 , 800 , 320, 50 , 330, 330,monsterSheet.getSubimage(3*32,2*32,32,32)));
		listOfMonsters.add(new Monster("DUP___TROLL",   2500, 15 , 1150, 400, 80 , 210, 210,monsterSheet.getSubimage(4*32,2*32,32,32)));
		listOfMonsters.add(new Monster("DUP___UNICORN",  400, 37 , 800 , 390, 105, 200, 200,monsterSheet.getSubimage(0*32,3*32,32,32)));//row 4
		listOfMonsters.add(new Monster("DUP___VAMPIRE", 1200, 50 , 1000, 400, 120, 200, 200,monsterSheet.getSubimage(1*32,3*32,32,32)));
		listOfMonsters.add(new Monster("DUP___WYVERN",   700, 20 , 700 , 320, 50 , 330, 330,monsterSheet.getSubimage(2*32,3*32,32,32)));
		listOfMonsters.add(new Monster("DUP___YETI",    1300, 30 , 1600, 400, 50 , 250, 250,monsterSheet.getSubimage(3*32,3*32,32,32)));
		listOfMonsters.add(new Monster("DUP___ZOMBIE",   500, 20 , 300 , 320, 50 , 330, 330,monsterSheet.getSubimage(4*32,3*32,32,32)));
		listOfMonsters.add(new Monster("DUP___TROLL",   2500, 15 , 1150, 400, 80 , 210, 210,monsterSheet.getSubimage(0*32,4*32,32,32)));//row 5
		listOfMonsters.add(new Monster("DUP___UNICORN",  400, 37 , 800 , 390, 105, 200, 200,monsterSheet.getSubimage(1*32,4*32,32,32)));
		listOfMonsters.add(new Monster("DUP___VAMPIRE", 1200, 50 , 1000, 400, 120, 200, 200,monsterSheet.getSubimage(2*32,4*32,32,32)));
		listOfMonsters.add(new Monster("DUP___WYVERN",   700, 20 , 700 , 320, 50 , 330, 330,monsterSheet.getSubimage(3*32,4*32,32,32)));
		listOfMonsters.add(new Monster("DUP___BLACK UNICORN",   700, 20 , 700 , 320, 50 , 330, 330,monsterSheet.getSubimage(0*32,4*32,32,32)));
	//	listOfMonsters.add(new Monster("YETI",    1300, 30 , 1600, 400, 50 , 250, 250));
	//	listOfMonsters.add(new Monster("ZOMBIE",   500, 20 , 300 , 320, 50 , 330, 330));
		
		listOfPuzzles.add(new Puzzle ("4 * 4 = ?","16", utilitySheet.getSubimage(0*32,4*32,32,32)));
		listOfPuzzles.add(new Puzzle ("type 'answer dog'","dog",utilitySheet.getSubimage(0*32,4*32,32,32)));
	}
	
	private void genRooms()
	{
		Random rand = new Random();
		listOfRooms.add(new Room(1,1,new NPC(lore.get(10),"WIZARD",NPCSheet.getSubimage(0*32,2*32,32,32))));
		roomGen:
		for(int x = 1; x <= dungeonWidth; x++)
		{
			for(int y = 1; y <= dungeonHeight; y++)
			{
				if(x == 1 && y == 1)
					continue;
				if(listOfEntities.size() <= 0)
					break roomGen;
				Entity entity = listOfEntities.get(rand.nextInt(listOfEntities.size()));
				listOfRooms.add(new Room(x,y,entity));
				listOfEntities.remove(entity);
			}
		}
		roomsGenerated = true;	
	}
}