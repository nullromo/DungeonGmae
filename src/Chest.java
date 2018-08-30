import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class Chest extends Entity
{
	private Item item;
	private boolean isOpen;
	ArrayList<ArrayList<Item>> allItems;
	public static int chestAnimationStep = 0;
	public boolean opening, firstStep = true;
	private int x = 310, y = 120;
	
	public Chest(String chestType, ArrayList<ArrayList<Item>> allItems,BufferedImage entityImage)
	{
		super(chestType,entityImage);
		this.allItems = allItems;
		genChestItem();
	}
	
	@Override
	public void update()
	{
		//if they leave the room, stop opening
		if(!Room.getCurrentEntity().equals(this))
			opening = false;
	}
	
	public void drawEntity(Graphics g)
	{
		g.drawImage(entityImage,310,120,180,180,null);
		if(opening)
			chestAnimation(g);
	}
	
	public void chestAnimation(Graphics g)
	{			
		if(y > 10 && firstStep)
			item.drawItem(g, x, y-=2);
		else if(x > 150 && y < 300)
		{
			firstStep = false;
			item.drawItem(g,x-=2,y+=2);
		}
	}
	
	public void genChestItem()
	{
		Random rand = new Random();
		int listSelector = rand.nextInt(4);
		int index;
		if(allItems.get(listSelector).size() != 0)
		{
			index = rand.nextInt(allItems.get(listSelector).size());
			switch(listSelector)
			{
			case 0:
				item = allItems.get(listSelector).get(index);
				allItems.get(listSelector).remove(index);
				break;
			case 1:
				item = allItems.get(listSelector).get(index);
				allItems.get(listSelector).remove(index);
				break;
			case 2:
				item = allItems.get(listSelector).get(index);
				allItems.get(listSelector).remove(index);
				break;
			case 3:
				item = allItems.get(listSelector).get(index);
				allItems.get(listSelector).remove(index);
				break;
			default:
				Game.history.append("\nchest item selection error.");
				break;
			}
		}
		else
			return;
	}

	public void openChest()
	{
		if (isOpen)
			Game.history.append("\nThe chest in this room is already open!");
		else
			chestAction();
	}
	
	public void printInfo()
	{
		Game.history.append("\nThere is a");
		if("aeiouy".contains(entityName.toLowerCase().substring(0,1)))
			Game.history.append("n ");
		else
			Game.history.append(" ");
		Game.history.append(entityName+"-chest in this room!");
	}
	
	private void chestAction()
	{
		double u = Math.random();
		if (u > .8)
		{
			Player.setHP(Player.getHP()-50);
			Game.history.append("\nThe chest was booby-trapped! You took 50 damage!");
		}
		else
		{
			Game.history.append("\nYou opened the chest! ");
			if(item!=null)
			{
				opening = true;
				Player.inventory.add(item);
				Game.history.append("\nThe " + item.getName() +" got added to your inventory!");
				isOpen = true;
			}
			else
				Game.history.append("\nThere doesn't seem to be an item in this chest!");
		}
	}

}