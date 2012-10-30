package UserInterface;

public class DBTableColumn
{
    private String name;
    private String dataType;

    public DBTableColumn(String name, String type)
    {
        this.name = name;
        this.dataType = type;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
