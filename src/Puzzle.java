
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Puzzle extends Entity
{
	private String puzzle, answer;
	private boolean isSolved;
	
	public Puzzle(String p, String a, BufferedImage entityImage)
	{
		super("puzzle",entityImage);
		puzzle = p;
		answer = a;
	}
	@Override
	public void update()
	{
		// TODO Auto-generated method stub
		
	}
	public void drawEntity(Graphics g)
	{
		if(!isSolved)
			g.drawImage(entityImage,400,50,250,250,null);
	}
	
	public static void puzzleDamager()
	{
		Random moneyOrHitpoints = new Random();
		int moneyOrHP;
		moneyOrHP = moneyOrHitpoints.nextInt(3);
		if (moneyOrHP == 0)
		{
			Player.setHP(Player.getHP() - 10);
			Game.history.append("\nThat answer is wrong! -10 hitpoints. Try again.");
		}
		else if (moneyOrHP == 1)
		{
			Player.setMoney(Player.getMoney() - 10);
			Game.history.append("\nThat answer is wrong! -10 money. Try again.");
		}
		else if (moneyOrHP == 2)
		{
			Player.setHP(Player.getHP() - 10);
			Player.setMoney(Player.getMoney() -10);
			Game.history.append("\nThat answer is wrong! -10 hitpoints. -10 money. Try again.");
		}
	}
	
	public void printInfo()
	{
		Game.history.append("\nThere is a puzzle in this room!" + 
				"\nYou must answer the question correctly or \nlose HP and/or money!" +
				"\nQuestion: " + puzzle);
	}
	public String getPuzzle()
	{
		return puzzle;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setIsSolved(boolean b)
	{
		isSolved = b;
	}
	public boolean getIsSolved()
	{
		return isSolved;
	}
	
}