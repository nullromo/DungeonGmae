import java.awt.*;
import java.awt.image.*;

public class Monster extends Entity
{
	private int health, damage, drop, maxHealth;
	//private boolean isSlain;
	private int imageX, imageY, imageWidth, imageHeight, hitsplatX =0, hitsplatY =0;
	public  enum MonsterState{NORMAL,SPLAT};
	public  static MonsterState monsterState;
	private static final int healthBarLength = 270;
	
	public Monster(String name, int hp, int dmg, int drp, int imageX, int imageY, int imageWidth,int imageHeight, BufferedImage entityImage)
	{
		super(name,entityImage);
		health = hp;
		maxHealth = hp;
		damage = dmg;
		drop = drp;
		//isSlain = false;
		this.imageX = imageX;
		this.imageY = imageY;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		monsterState = MonsterState.NORMAL;
	}
	
	@Override
	public void update()
	{
		checkDeath();
		switch(Monster.monsterState)
		{
		case SPLAT:
			if(System.currentTimeMillis() > Player.timeOfAttack + Player.HITSPLAT_TIME)
			{
				Monster.monsterState = Monster.MonsterState.NORMAL;
				Player.playerState = Player.PlayerState.SPLAT;
				Sound.playerHurt.play();
				if(Room.getCurrentEntity() instanceof Monster)
					((Monster)Room.getCurrentEntity()).checkDeath();
			}
			break;
		default://NORMAL
			break;
		}
	}
	
	public void drawEntity(Graphics g)
	{
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New",Font.BOLD,32));
		drawCombatHUD(g);
		switch(monsterState)
		{
		case NORMAL:
			g.drawImage(entityImage,imageX,imageY,imageWidth,imageHeight,null);
			break;
		case SPLAT:
			g.drawImage(entityImage,imageX,imageY,imageWidth,imageHeight,null);
			g.drawImage(Player.hitsplat, hitsplatX+imageX, hitsplatY+imageY, 100, 100, null);
			g.setColor(Color.white);
			g.setFont(new Font("Courier New", Font.BOLD, 32));
			g.drawString(String.valueOf(Player.getDamageDealt()), hitsplatX+29+imageX, hitsplatY+60+imageY);
			break;
		}
	}
	
	private void drawCombatHUD(Graphics g)
	{
		int healthBarFill = health*healthBarLength/maxHealth;
		g.setColor(Color.BLACK);
		g.fillRect(410, 0, 400, 60);
		
		g.setColor(Color.RED);
		g.fillRect(790-healthBarFill,10,healthBarFill,15);
	
		g.setColor(Color.BLACK);
		int healthChunkSize = healthBarLength/10;
		for(int i=0; i <= healthBarFill / healthChunkSize; i++)
			g.drawLine(790-(healthChunkSize*i), 10, 790-(healthChunkSize*i), 25);
		
		g.setColor(Color.GREEN);
		g.fillRect(390,0,20,60);
		g.setFont(new Font("Courier New",Font.BOLD,18));
		g.drawString("HP:"+String.valueOf(health),708-healthBarLength,23);
	}
	
	public void checkDeath()
	{
		if (health <= 0)
		{
			
			Game.history.append("\nThe " + entityName + " in this room has been slain!");
			Player.setMoney(Player.getMoney() + drop);
			Game.history.append("\nThe monster dropped " + drop + " money!");
			Game.history.setCaretPosition(Game.history.getDocument().getLength());
			Game.limit = Game.Limiter.NPC;
			Room.getCurrentRoom().removeEntity();
		}
	}
	
	public void monsterTurn()
	{
		Player.placePlayerHitsplat();
		Player.damageBlocked = (int)((Player.getArmor()+Player.getEquippedDefence())*(1 + Math.random()));
		Player.damageTaken = (int)(damage*(1 + Math.random()));
		if (Player.damageBlocked < Player.damageTaken)
		{
			Player.setHP(Player.getHP() + Player.damageBlocked);
			Player.setHP(Player.getHP() - Player.damageTaken);
			Game.history.append("\nThe "+ entityName + " inflicted " + Player.damageTaken + " damage on you!");
			Game.history.append("\nYou blocked " + Player.damageBlocked + " of it!");
		}
		else
			Game.history.append("\nYou blocked the " + entityName + "'s attack!");
	}
	
	public void printInfo()
	{
		Game.history.append("\nThere is a " + entityName + " in this room! You must fight it!");
	}
	
	public void placeMonsterHitsplat()
	{
		hitsplatX = (int)(Math.random() * imageX/2);
		hitsplatY = (int)(Math.random() * imageY/2);
	}
	
	//gets and sets
	public int getMonsterDrop(){return drop;}
	//public boolean isSlain(){return isSlain;}
	//public void setIsSlain(boolean b){isSlain = b;}
	public int getMonsterDamage(){return damage;}
	public int getHealth(){return health;}
	public void setHealth(int k){health = k;}
	public void setMaxHealth(int m){maxHealth = m;}
	public int getMaxHealth(){return maxHealth;}

}