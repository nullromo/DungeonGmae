
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class DamagePotion extends Item
{
	private int damage;
	
	public DamagePotion(Type t, String name, int cost, int dam, BufferedImage itemImage)
	{
		super(dam,t,name,cost,itemImage);
		damage = dam;
	}
	
	public int getDamage() {return damage;}

	public void drawItem(Graphics g)
	{
		g.drawImage(itemImage,160,200,50,50,null);
	}
	public void drawItem(Graphics g, int x, int y)
	{
		g.drawImage(itemImage,x,y,50,50,null);
	}
}