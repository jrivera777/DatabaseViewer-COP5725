package DBDataStructures;

public class Measure
{
    private String type;
    private String columnName;

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "Measure{" + "type=" + type + ", columnName=" + columnName + '}';
    }
}
