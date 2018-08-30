
import java.awt.*;
import java.awt.image.*;

public abstract class Item
{
	private int stat;
	public enum Type{WEAPON,DMGPOTION,HEALPOTION,ARMOR};
	public Type itemType;
	protected String itemName;
	protected int xCoord, yCoord;
	protected int itemCost, layer;
	protected boolean itemEquipped = false;
	protected BufferedImage itemImage;
	
	public Item(int st, Type t, String name, int cost, BufferedImage itemImage)
	{
		stat = st;
		itemType = t;
		itemName = name;
		itemCost = cost;
		this.itemImage = itemImage;
	}
	
	public abstract void drawItem(Graphics g);
	public abstract void drawItem(Graphics g,int x,int y);
	public int getLayer() {return layer;}
	public String getName() {return itemName;}
	public int getValue() {return itemCost;}
	public int getStat() {return stat;}
}