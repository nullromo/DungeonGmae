
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class HealthPotion extends Item
{
	private int potency;
	
	public HealthPotion(Type t, String name, int cost, int pot, BufferedImage itemImage)
	{
		super(pot,t,name,cost,itemImage);
		potency = pot;
	}
	
	public int getPotency() {return potency;}

	public void drawItem(Graphics g)
	{
		g.drawImage(itemImage,160,200,50,50,null);
	}
	public void drawItem(Graphics g, int x, int y)
	{
		g.drawImage(itemImage,x,y,50,50,null);
	}
}