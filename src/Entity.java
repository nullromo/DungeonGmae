import java.awt.*;
import java.awt.image.*;

public abstract class Entity
{

	protected BufferedImage entityImage;
	protected  static BufferedImage backgroundImage;
	protected String entityName;
	
	public Entity(String entityName,BufferedImage entityImage)
	{
		this.entityName = entityName;
		this.entityImage = entityImage;
	}

	public abstract void printInfo();
	
	public abstract void update();
	//make abstract eventually when we have multiple room images
	//override in subclasses
	public abstract void drawEntity(Graphics g);

	public static Room getCurrentRoom()
	{
		for(Room room : Game.dungeon.listOfRooms)
			if (room.isPlayerPresent())
				return room;
		return null;
	}

	public String getEntityName(){return entityName;}
}