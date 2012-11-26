package DBDataStructures;

import java.util.ArrayList;

public class Dimension
{
    private String name;
    private ArrayList<String> granules;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<String> getGranules()
    {
        return granules;
    }

    public void setGranules(ArrayList<String> granules)
    {
        this.granules = granules;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Dimension Name: " + this.getName()+ " [ ");
        for(String gran : this.getGranules())
        {
            sb.append(gran + " ");
        }
        sb.append("]");

        return sb.toString();
    }
}
