
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


public class Shop extends Entity
{
	private boolean shopOpened;
	private ArrayList<Item> allItems,shopItems;
	
	public Shop(String shopName, ArrayList<Item> allItems, BufferedImage entityImage)
	{
		super(shopName,entityImage);
		this.allItems = allItems;
		shopItems = new ArrayList<Item>();
		genShopItems();

	}

	public void update()
	{
		// TODO Auto-generated method stub
		
	}
	public void printInfo()
	{
		Game.history.append("\nThere is a "+entityName+" in this room!");
	}
	public void drawEntity(Graphics g)
	{
		g.drawImage(entityImage,500,140,200,200,null);
	}
	
	public void genShopItems()
	{
		Random rand = new Random();
		Item tempItem;
		for(int i = 0; i <= 4;i++)
		{
			if(allItems.isEmpty())
				break;
			tempItem = allItems.get(rand.nextInt(allItems.size()));
			if(shopItems.contains(tempItem))
				i--;
			else
			{
				shopItems.add(tempItem);
				allItems.remove(tempItem);
			}
		}
	}
	
	
	public void printShopItems()
	{
		if(shopItems.isEmpty())
			Game.history.append("\nThe shop is out of items!");
		else
		{
			Game.history.append("\nShopkeeper: \"I have these items for sale:\"");
			for(Item item : shopItems)
				Game.history.append("\n"+item.getName());
		}
	}
	
	public void makePurchase(String wantedItem)
	{
		for(Item item : shopItems)
			if(item.getName().toLowerCase().equals(wantedItem))
			{
				Player.inventory.add(item);
				shopItems.remove(item);
				Player.setMoney((Player.getMoney() - item.getValue()));
				Game.history.append("\nYou purchased the "+item.getName()+"!");
				printShopItems();
				return;
			}
		Game.history.append("\nThe shop doesn't have that item!");	
	}
	public void makeSale(String sellItem) 
	{
		for(Item item : Player.inventory)
			if(item.getName().toLowerCase().equals(sellItem))
			{
				Player.inventory.remove(item);
				shopItems.add(item);
				Player.setMoney((int)(Player.getMoney() + (item.getValue()*.8)));//loses 20% of value
				Game.history.append("\nYou sold the "+item.getName()+"!");
				printShopItems();
				return;
			}
		Game.history.append("\nYou don't have that item!");	
	}

	public boolean isShopOpened() {return shopOpened;}
	
}