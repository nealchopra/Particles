import java.awt.Color;

public class Steam extends Particle
{
    private int age;
    public Steam()
    {
        super(ParticlesProgram.STEAM, new Color (247, 255, 254));
        age = 0;
    }
    
    public void increment()
    {
        age++;
    }
    
    public boolean hasEvaporated()
    {
        return age>= 250;
    }
}