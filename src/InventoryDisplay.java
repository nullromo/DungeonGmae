import java.awt.*;

public class InventoryDisplay
{
	private static int itemsPerRow = 4;
	private static int itemSize = 350 / itemsPerRow;
	
	public static void drawInventory(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0,0,350,350);
		if(Player.inventory.isEmpty())
		{
			//why dis no work?
//			g.setFont(new Font("Ariel",Font.BOLD,50));
//			g.drawString("INVENTORY", 0 , 1 * 50);
//			g.drawString("EMPTY!", 0 , 2 * 50);
			return;
		}
		
		int row = 0;
		int col = 0;
		int index = 0;
		
		for(Item item : Player.inventory)
		{
			if(index % 4 == 0 && index != 0)
			{
				col = 0;
				row++;
			}
			g.drawImage(item.itemImage,col * itemSize,row * itemSize,itemSize,itemSize,null);
			g.setFont(new Font("Ariel",Font.BOLD,5));
			g.setColor(Color.WHITE);
			g.drawString(item.itemName,col * itemSize, (row * itemSize) + itemSize);
			if(Player.equippedItems.contains(item))
			{					
				g.setColor(Color.GREEN);
				g.drawRect(col * itemSize, row * itemSize, itemSize, itemSize);
			}
			col++;
			index++;
		}
	}
}