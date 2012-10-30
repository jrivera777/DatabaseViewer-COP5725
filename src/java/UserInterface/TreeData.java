package UserInterface;

import com.google.gson.Gson;
import java.util.ArrayList;

public class TreeData
{
   // private static int ID_COUNT = 1;
    private String label;
    //private int id;
    private ArrayList<TreeData> children;

    public TreeData(String lbl)
    {
        //id = ID_COUNT++;
        this.label = lbl;
        children = null;
    }

    public void addChild(TreeData td)
    {
        if(children == null)
            children = new ArrayList<TreeData>();

        children.add(td);
    }


    public static void main(String[] args)
    {
        TreeData root = new TreeData("Saurischia");
        TreeData child = new TreeData("Herrerasaurians");
        root.addChild(child);
        String json = new Gson().toJson(root);
        System.out.println(json);
    }

    public ArrayList<TreeData> getChildren()
    {
        return children;
    }

    public void setChildren(ArrayList<TreeData> children)
    {
        this.children = children;
    }

//    public int getId()
//    {
//        return id;
//    }
//
//    public void setId(int id)
//    {
//        this.id = id;
//    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }
}
