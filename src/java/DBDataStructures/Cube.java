package DBDataStructures;
import java.util.ArrayList;

public class Cube
{
    private String name;
    private String dbName;
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

    public String getDbName()
    {
        return dbName;
    }

    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Cube Name: " + this.getName() + "\n");
        sb.append("Dimensions: \n{\n");
        for(Dimension dime : this.getDimensions())
        {
           sb.append("" + dime.toString() + "\n");
        }
        sb.append("}");

        return sb.toString();
    }
}
