import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Weapon extends Item
{
	private int power;
	
	public Weapon(Type t, String name, int cost, int pow, int lay, BufferedImage itemImage)
	{
		super(pow,t,name,cost,itemImage);
		power = pow;
		layer = lay;
	}
	
	public int getPower() {return power;}

	public void drawItem(Graphics g)
	{
		g.drawImage(itemImage,110,160,140,140,null);
	}
	public void drawItem(Graphics g, int x, int y)
	{
		g.drawImage(itemImage,x,y,140,140,null);
	}
	
	public int getLayer() {return layer;}
}