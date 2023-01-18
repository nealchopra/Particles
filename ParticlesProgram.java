import java.awt.*;
import java.util.*;
import acm.program.*;

public class ParticlesProgram extends Program
{
    //main particles
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;
    public static final int ICE = 4;
    public static final int ACID = 5;
    public static final int LAVA = 6;
    public static final int GLASS = 7;
    public static final int WOOD = 8;
    public static final int SEEDS = 9;
    public static final int GRASS = 10;
    
    //sub-particles
    public static final int WET_ACID = 11;
    public static final int STEAM = 12;
    public static final int SMOKE = 13;
    public static final int OBSIDIAN = 14;
    public static final int STEM = 15;
    public static final int PETALS = 16;

    //do not add any more private instance variables
    private Particle[][] grid;
    private ParticlesDisplay display;

    public void init()
    {
        initVariables(120, 80);
    }    

    public void initVariables(int numRows, int numCols)
    {
        String[] names;
        names = new String[11];
        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[SAND] = "Sand";
        names[WATER] = "Water";
        names[ICE] = "Ice";
        names[ACID] = "Acid";
        names[LAVA] = "Lava";
        names[GLASS] = "Glass";
        names[WOOD] = "Wood";
        names[SEEDS] = "Seeds";
        names[GRASS] = "Grass";
        
        display = new ParticlesDisplay("Particles Game", 
            numRows, numCols, names);
        // initialize the grid here (task 0.1)
        grid = new Particle[numRows][numCols];
        for(int r = 0; r < numRows; r++)
        {
            for (int c = 0; c < numCols; c++)
            {
                grid[r][c] = new Empty();
            }
        }
    }

    //called when the user clicks on a location using the given particleType
    private void locationClicked(int row, int col, int particleType)
    {
        // finish this cascading if (task 0.2)
        if (particleType == EMPTY)
            grid[row][col] = new Empty();
        else if (particleType == METAL)
            grid[row][col] = new Metal();          
        else if (particleType == SAND)
            grid[row][col] = new Sand();     
        else if (particleType == WATER)
            grid[row][col] = new Water();  
        else if (particleType == ICE)
            grid[row][col] = new Ice(); 
        else if (particleType == ACID)
            grid[row][col] = new Acid();
        else if (particleType == LAVA)
            grid[row][col] = new Lava();
        else if (particleType == GLASS)
            grid[row][col] = new Glass();
        else if (particleType == WOOD)
            grid[row][col] = new Wood();
        else if (particleType == SEEDS)
            grid[row][col] = new Seeds();
        else if (particleType == GRASS)
            grid[row][col] = new Grass();
}

    //called repeatedly.
    //causes one random particle to maybe do something.
    public void step()
    {
        int row = (int)(Math.random() * grid.length);
        int col = (int)(Math.random() * grid[0].length);
        Particle particle = grid[row][col];
        if (particle.getType() == EMPTY || particle.getType() == METAL)
            return;
        else if (particle.getType() == SAND)
        {
            tryToMoveDown(row, col, true);
        }
        else if (particle.getType() == WATER)
        {
            waterBehavior(row, col);
        }
        else if (particle.getType() == ICE)
        {
            Ice ice = (Ice)(particle);
            ice.increment();
            if (ice.hasMelted())
                grid[row][col] = new Water();
        }
        else if (particle.getType() == ACID)
        {
            if (Math.random() < 0.4)
                acidBehavior(row, col);
        }
        else if (particle.getType() == STEAM)
        {
            if (Math.random() < 0.2)
            {
            steamBehavior(row, col);
            Steam steam = (Steam)(particle);
            steam.increment();
            if (steam.hasEvaporated())
                grid[row][col] = new Empty();
            }
        }
        else if (particle.getType() == LAVA)
        {
            if (Math.random() < 0.1)
                lavaBehavior(row, col);
        }
        else if (particle.getType() == OBSIDIAN)
            return;
        else if (particle.getType() == GLASS)
            return;
        else if (particle.getType() == WOOD)
            return;
        else if (particle.getType() == PETALS)
            return;
        else if (particle.getType() == STEM)
            return;
        else if (particle.getType() == GRASS)
            return;
        else if (particle.getType() == SEEDS)
        {
            seedBehavior(row, col);
        }
    }

    //move methods
    public void tryToMoveUp(int row, int col)
    {
        if (row != 0 && (grid[row-1][col].getType() == EMPTY ||
            grid[row-1][col].getType() == WATER))
        {
            Particle save = grid[row][col];
            grid[row][col] = grid[row-1][col];
            grid[row-1][col] = save;
        }  
    }
    
    public void tryToMoveDown(int row, int col, boolean canFallThroughWater)
    {
        if (row != grid.length-1 && (grid[row+1][col].getType() == EMPTY
            || canFallThroughWater && grid[row+1][col].getType() == WATER))
        {
            Particle save = grid[row][col];
            grid[row][col] = grid[row+1][col];
            grid[row+1][col] = save;
        }  
    }

    public void tryToMoveLeft(int row, int col)
    {
        if (col != 0 && grid[row][col-1].getType() == EMPTY) //why is the row below "row+1"
        {
            Particle save = grid[row][col];
            grid[row][col] = grid[row][col-1];
            grid[row][col-1] = save;
        }  
    }

    public void tryToMoveRight(int row, int col)
    {
        if (col != grid[0].length-1 && grid[row][col+1].getType() == EMPTY) //why is the row below "row+1"
        {
            Particle save = grid[row][col];
            grid[row][col] = grid[row][col+1];
            grid[row][col+1] = save;
        }  
    }

    //particle behaviors
    public void waterBehavior (int row, int col)
    {
        int direction = (int)(Math.random() * 3);
        if (row != grid.length-1 && grid[row+1][col].getType() == LAVA)
        {
            grid[row][col] = new Obsidian();
        }
        if (direction == 0)
            tryToMoveDown(row, col, false);
        else if (direction == 1)
            tryToMoveLeft(row, col);
        else
            tryToMoveRight(row, col);
    }
    
    public void steamBehavior (int row, int col)
    {
        int direction = (int)(Math.random() * 3);
        if (direction == 0)
            tryToMoveUp(row, col);
        else if (direction == 1)
            tryToMoveLeft(row, col);
        else
            tryToMoveRight(row, col);
    }
    
    public void acidBehavior (int row, int col)
    {
        int direction = (int)(Math.random() * 3);
        if (row != grid.length-1 && grid[row+1][col].getType() == METAL)
        {
            grid[row+1][col] = new Acid();
        }
        if (row != grid.length-1 && grid[row+1][col].getType() == LAVA)
        {
            grid[row][col] = new Lava();
        }
        if (row != grid.length-1 && row != 0 && col != grid[0].length-1 && col != 0 &&
        grid[row+1][col].getType() == WATER && grid[row-1][col].getType() == WATER && 
        grid[row][col+1].getType() == WATER && grid[row][col-1].getType() == WATER)
        {
            grid[row][col] = new WetAcid();
            grid[row-1][col] = new Steam();
        }
        if (direction == 0)
            tryToMoveDown(row, col, true);
        else if (direction == 1)
            tryToMoveLeft(row, col);
        else
            tryToMoveRight(row, col);
    }
    
    public void lavaBehavior (int row, int col)
    {
        int direction = (int)(Math.random() * 3);
        if (row != grid.length-1 && grid[row+1][col].getType() == GLASS)
        {
            grid[row+1][col] = new Lava();
        }
        if (row != grid.length-1 && grid[row+1][col].getType() == WOOD)
        {
            grid[row+1][col] = new Lava();
            grid[row-1][col] = new Steam();
        }
        if (row != grid.length-1 && row != 0 && col != grid[0].length-1 && col != 0 &&
        grid[row+1][col].getType() == WATER && grid[row-1][col].getType() == WATER && 
        grid[row][col+1].getType() == WATER && grid[row][col-1].getType() == WATER)
        {
            grid[row][col] = new Obsidian();
            grid[row-1][col] = new Steam();
        }
        if (direction == 0)
            tryToMoveDown(row, col, true);
        else if (direction == 1)
            tryToMoveLeft(row, col);
        else
            tryToMoveRight(row, col);
    }
    
    public void seedBehavior(int row, int col)
    {
        int stemLength = (int)(Math.random() * 50);
        int stemRow = stemLength; 
        int petalLength = 11;
        int halfPetalLength = 5;
        if (row > stemRow && 
            col > 4 && col<grid[0].length-4 && 
            (grid[row-1][col].getType() == WATER) && 
            (grid[row+1][col].getType() == GRASS)) 
        {
        for (int  r=1; r < stemLength; r++)
             grid[row-r][col] = new Stem();
        for (int r=0; r<petalLength; r++)
            grid[row-stemLength-r][col] = new Petals();
        for (int c=0; c<petalLength; c++)
            grid[row+1-stemLength-halfPetalLength][col-halfPetalLength+c] = new Petals();
        }
    }
    
    //copies each element of grid into the display (don't modify this)
    public void updateDisplay()
    {
        for (int r=0; r<grid.length; r++)
            for (int c=0; c<grid[0].length; c++)
                display.setColor(r, c, grid[r][c].getColor());
    }

    // repeatedly calls step and updates the display
    // (don't modify this)
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < display.getSpeed(); i++)
                step();
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
}
