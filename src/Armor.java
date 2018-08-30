
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Armor extends Item
{
	private int defense;
	
	public Armor(Type t, String name, int cost, int def, int lay, BufferedImage itemImage)
	{
		super(def,t,name,cost,itemImage);
		defense = def;
		layer = lay;
	}

	public void drawItem(Graphics g)
	{
		g.drawImage(itemImage,110,160,140,140,null);
	}
	public void drawItem(Graphics g, int x, int y)
	{
		g.drawImage(itemImage,x,y,140,140,null);
	}
	public int getDefense() {return defense;}
	public int getLayer() {return layer;}
}