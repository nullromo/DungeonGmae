import java.awt.*;
import java.awt.image.*;

public class AbsentEntity extends Entity
{
	public AbsentEntity(String entityName, BufferedImage entityImage)
	{
		super(entityName, entityImage);
	}
	
	@Override
	public void printInfo()
	{
		Game.history.append("\nThere is nothing in this room!");
	}
	
	@Override
	public void update()
	{	//nothing to update
	}
	
	@Override
	public void drawEntity(Graphics g)
	{	//nothing to draw
	}
}