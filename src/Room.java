import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class Room
{
	protected int xCoord, yCoord;
	private  static BufferedImage backgroundImage;
	private Entity entityInRoom;
	private boolean isExplored;
	public static final int WEST = 0, NORTH = 1, EAST = 2, SOUTH = 3;
	
	public Room(int x, int y, Entity entity)
	{
		isExplored = false;
		xCoord = x;
		yCoord = y;
		entityInRoom = entity;
		try{
			backgroundImage = ImageIO.read(Room.class.getResource("ROOM.png"));
		}catch(Exception e){e.printStackTrace(); Game.history.append("\n\nRuntime error: couldn't load ROOM.png");
		}
	}


	public void printInfo()
	{
		entityInRoom.printInfo();
	}
	
	public void removeEntity()
	{
		entityInRoom = new AbsentEntity("EMPTY ROOM", null);
	}
	
	public static void drawRoom(Graphics g) 
	{
		g.drawImage(backgroundImage,0,0,null);
	}
	
	public static Entity getCurrentEntity()
	{
		for(Room room: Game.dungeon.listOfRooms)
			if (room.isPlayerPresent())
				return room.getEntity();
		return null;
	}
	
	public static Room getCurrentRoom()
	{
		for(Room room : Game.dungeon.listOfRooms)
			if (room.isPlayerPresent())
				return room;
		return null;
	}

	/*
	public static Room getCurrentRoom(Entity ent)
	{
		for(Room room : Game.dungeon.listOfRooms)
			if (room.getEntity().equals(ent))
				return room;
		return null;
	}*/
	
	public boolean isPlayerPresent()
	{
		if (Player.getXPosition() == xCoord && Player.getYPosition() == yCoord)
			return true;
		return false;
	}
	
	public boolean hasNeighbor(int direction)
	{
		//check if on edge
		switch(direction)
		{
		case 0://west
			if(getxcoord() == 1)
				return false;
		//if(roomAt(getxcoord()-1) instanceof NullRoom)
		//	return false;
			break;
		case 1://north
			if(getycoord() == Game.dungeon.dungeonHeight)
				return false;
		//if(roomAt(getycoord()+1) instanceof NullRoom)
		//	return false;
			break;
		case 2://east
			if(getxcoord() ==  Game.dungeon.dungeonHeight)
				return false;
		//if(roomAt(getxcoord()+1) instanceof NullRoom)
		//	return false;
			break;
		case 3://south
			if(getycoord() == 1)
				return false;
		//if(roomAt(getycoord()-1) instanceof NullRoom)
		//	return false;
			break;
		}
		return true;
	}
	
	//gets and sets
	public int getxcoord() {return xCoord;}
	public int getycoord() {return yCoord;}
	public Entity getEntity(){return entityInRoom;}
	public boolean isExplored(){return isExplored;}
	public void setExplored(){isExplored = true;}
}