package DBDataStructures;

import java.util.ArrayList;

public class Cube
{
    private String name;
    private String dbName;
    private String table;
    private ArrayList<Dimension> dimensions;
    private ArrayList<Measure> measures;
    public int id;

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

    public String getTable()
    {
        return table;
    }

    public void setTable(String table)
    {
        this.table = table;
    }

    public ArrayList<Measure> getMeasures()
    {
        return measures;
    }

    public void setMeasures(ArrayList<Measure> measures)
    {
        this.measures = measures;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Cube Name: " + this.getName() + " on Table <" + this.getTable() + ">\n");

        sb.append("Dimensions: \n{\n");
        for(Dimension dime : this.getDimensions())
        {
            sb.append("" + dime.toString() + "\n");
        }
        sb.append("}");
        
        sb.append("Measures: \n{\n");
        for(Measure me : this.getMeasures())
        {
            sb.append("" + me.toString() + "\n");
        }
        sb.append("}");

        return sb.toString();
    }
}
