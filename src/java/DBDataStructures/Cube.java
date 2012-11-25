package DBDataStructures;
import java.util.ArrayList;

public class Cube
{
    private String name;
    private ArrayList<Dimension> dimensions;

    public ArrayList<Dimension> getDimensions()
    {
        return dimensions;
    }

    public void setDimensions(ArrayList<Dimension> dimensions)
    {
        this.dimensions = dimensions;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Cube Name: " + this.getName() + "\n");
        sb.append("\tDimensions: \n\t{\n");
        for(Dimension dime : this.getDimensions())
        {
           sb.append("\t\t" + dime.toString() + "\n");
        }
        sb.append("\t}");

        return sb.toString();
    }
}
