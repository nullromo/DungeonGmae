import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class NPC extends Entity
{
	private String lore;

	public NPC(String lore, String name, BufferedImage entityImage)
	{
		super(name,entityImage);
		this.lore = lore;
	}
	@Override
	public void update()
	{
		// TODO Auto-generated method stub
		
	}
	public void drawEntity(Graphics g)
	{
		g.drawImage(entityImage, 500, Player.PLAYER_IMAGE_Y, Player.PLAYER_IMAGE_SIZE, Player.PLAYER_IMAGE_SIZE, null);
	}
	public void printInfo()
	{
		Game.history.append("\nThere is a "+entityName+" NPC in this room!");
	}
	
	//make this suck less in future.
	public void sayLore()
	{
		Game.history.append("\n"+entityName+": " + lore);
	}
	
}