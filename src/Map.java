import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class Map
{
	private static BufferedImage utilitySheet;
	private static HashMap<String,BufferedImage> rooms = new HashMap<String,BufferedImage>();
	static
	{
		try
		{
			utilitySheet = ImageIO.read(Map.class.getResource("sheets/utilitySheet.png"));
		}catch(IOException e){e.printStackTrace();
		}
		rooms.put(""    ,utilitySheet.getSubimage(0*32,0*32,32,32));
		rooms.put("B"   ,utilitySheet.getSubimage(1*32,0*32,32,32));
		rooms.put("L"   ,utilitySheet.getSubimage(2*32,0*32,32,32));
		rooms.put("LB"  ,utilitySheet.getSubimage(3*32,0*32,32,32));
		rooms.put("LR"  ,utilitySheet.getSubimage(4*32,0*32,32,32));
		rooms.put("LRB" ,utilitySheet.getSubimage(0*32,1*32,32,32));
		rooms.put("LT"  ,utilitySheet.getSubimage(1*32,1*32,32,32));
		rooms.put("LTB" ,utilitySheet.getSubimage(2*32,1*32,32,32));
		rooms.put("LTR" ,utilitySheet.getSubimage(3*32,1*32,32,32));
		rooms.put("LTRB",utilitySheet.getSubimage(4*32,1*32,32,32));
		rooms.put("R"   ,utilitySheet.getSubimage(0*32,2*32,32,32));
		rooms.put("RB"  ,utilitySheet.getSubimage(1*32,2*32,32,32));
		rooms.put("T"   ,utilitySheet.getSubimage(2*32,2*32,32,32));
		rooms.put("TB"  ,utilitySheet.getSubimage(3*32,2*32,32,32));
		rooms.put("TR"  ,utilitySheet.getSubimage(4*32,2*32,32,32));
		rooms.put("TRB" ,utilitySheet.getSubimage(0*32,3*32,32,32));
	}
	public static final BufferedImage CHARACTER= utilitySheet.getSubimage(2*32,3*32,32,32);
	
	public static void drawMap(Graphics g)
	{
		int playerY =  Game.dungeon.dungeonHeight - Player.getYPosition();
		int roomY;
		int roomSize = 350/Game.dungeon.dungeonHeight;
		
		for(Room room : Game.dungeon.listOfRooms)
		{
			roomY = 9 - room.getycoord();
			if (room.isExplored())
				drawTile(room,g,(room.getxcoord()-1)*roomSize, (roomY)*roomSize+1, roomSize-1, roomSize-1);
			g.drawImage(CHARACTER,(Player.getXPosition()-1) * roomSize+6, (playerY) * roomSize+7, roomSize-13, roomSize-13,null);
			drawToken(room,g,(room.getxcoord()-1)*roomSize+13, (roomY)*roomSize+(roomSize/2) + 5, roomSize-27, roomSize-27);
		}
	}
	
	private static void drawTile(Room r, Graphics g, int x, int y, int w, int h)
	{
		String tileType = "";
		if(r.hasNeighbor(Room.WEST))
			tileType += "L";
		if(r.hasNeighbor(Room.NORTH))
			tileType += "T";
		if(r.hasNeighbor(Room.EAST))
			tileType += "R";
		if(r.hasNeighbor(Room.SOUTH))
			tileType += "B";
		g.drawImage(rooms.get(tileType),x,y,w,h,null);
	}
	
	private static void drawToken(Room r, Graphics g, int x, int y, int w, int h)
	{
		if(r.isPlayerPresent() || !r.isExplored())
			return;
		Entity ent = r.getEntity();
		String s = "";
		if(ent instanceof Monster)
		{
			g.setColor(Color.BLUE);
			s = "M";
		}
		else if(ent instanceof Puzzle)
		{
			g.setColor(Color.RED);
			s = "P";
		}
		else if(ent instanceof NPC)
		{
			g.setColor(Color.DARK_GRAY);
			s = "N";
		}
		else if(ent instanceof Shop)
		{
			g.setColor(Color.GREEN);
			s = "S";
		}
		else if(ent instanceof Chest)
		{
			g.setColor(Color.MAGENTA);
			s = "C";
		}
		g.drawString(s, x, y);
	}
}