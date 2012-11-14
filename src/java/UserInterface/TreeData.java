package UserInterface;

import com.google.gson.Gson;
import java.util.ArrayList;

public class TreeData
{
   // private static int ID_COUNT = 1;
    private String label;
    private String dataName;
    private String type;
    private ArrayList<TreeData> children;

    public TreeData(String nm, String ty)
    {
        this.type = ty;
        this.dataName = nm;
        this.label = ty.equals("") || ty == null ? nm : nm + ": " + type;
        children = null;
    }

    public void addChild(TreeData td)
    {
        if(children == null)
            children = new ArrayList<TreeData>();

        children.add(td);
    }

    public ArrayList<TreeData> getChildren()
    {
        return children;
    }

    public void setChildren(ArrayList<TreeData> children)
    {
        this.children = children;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
