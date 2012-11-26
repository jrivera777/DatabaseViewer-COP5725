/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import DBDataStructures.Cube;
import DBDataStructures.Dimension;
import DBDataStructures.Measure;
import java.sql.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Joseph
 */
public class SaveCube extends HttpServlet
{
    protected final String ERROR_MESSAGE = "<div class=\"alert alert-error\">"
            + "<button type=\"button\" class=\"close\" "
            + "data-dismiss=\"alert\">×</button>%s</div>";
    protected final String SUCCESS_MESSAGE = "<div class=\"alert alert-success\">"
            + "<button type=\"button\" class=\"close\" "
            + "data-dismiss=\"alert\">×</button>%s</div>";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection conn = null;
        try
        {
            Cube newCube = new Cube();

            newCube.setName(request.getParameter("name"));
            newCube.setTable(request.getParameter("tableName"));
            newCube.setDbName((String) request.getSession().getAttribute("dbname"));

            String dimensions = request.getParameter("dimes");
            String measures = request.getParameter("msrs");
            JSONArray jsonDimensions = (JSONArray) JSONValue.parse(dimensions);
            JSONArray jsonMeasures = (JSONArray) JSONValue.parse(measures);

            ArrayList<Dimension> dimes = new ArrayList<Dimension>();
            for(int i = 0; i < jsonDimensions.size(); i++)
            {
                Dimension newDimension = new Dimension();

                JSONObject jObj = (JSONObject) jsonDimensions.get(i);
                newDimension.setName((String) jObj.get("name"));
                JSONObject table = (JSONObject) jObj.get("data");
                JSONArray grans = (JSONArray) table.get("data");

                ArrayList<String> dimeGrans = new ArrayList<String>();
                for(int j = 0; j < grans.size(); j++)
                    dimeGrans.add((String) grans.get(j));

                newDimension.setGranules(dimeGrans);
                dimes.add(newDimension);
            }
            newCube.setDimensions(dimes);

            ArrayList<Measure> msrs = new ArrayList<Measure>();
            for(int i = 0; i < jsonMeasures.size(); i++)
            {
                Measure newMeasure = new Measure();
                JSONObject jObj = (JSONObject) jsonMeasures.get(i);
                newMeasure.setType((String) jObj.get("type"));
                newMeasure.setColumnName((String) jObj.get("columnname"));
                msrs.add(newMeasure);
            }
            newCube.setMeasures(msrs);

            System.out.println(newCube.toString());

            String userConnect = "jdbc:mysql://localhost/cop5725?user=test&password=test";
            //String userConnect = "jdbc:mysql://172.23.19.231:8080/cop5725?user=root&password=control";
            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(userConnect);
                this.saveCube(newCube, conn);

                out.printf(SUCCESS_MESSAGE, "Successfully saved cube");
            }
            catch(SQLException ex)
            {
                System.out.println(ex.getMessage());
                out.printf(ERROR_MESSAGE, ex.getMessage());
                ex.printStackTrace();
            }
            catch(ClassNotFoundException ex)
            {
                System.out.println(ex.getMessage());
                out.printf(ERROR_MESSAGE, ex.getMessage());
                ex.printStackTrace();
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
                out.printf(ERROR_MESSAGE, ex.getMessage());
                ex.printStackTrace();
            }
            finally
            {
                if(conn != null)
                {
                    try
                    {
                        conn.close();
                    }
                    catch(Exception ex)
                    {
                        System.out.println(ex.getMessage());
                        out.printf(ERROR_MESSAGE, ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            out.printf(ERROR_MESSAGE, ex.getMessage());
            ex.printStackTrace();
        }
        finally
        {
            out.close();
        }
    }

    public void saveCube(Cube cube, Connection conn) throws SQLException
    {
        int cubeID = -1;
        boolean isNew = true;
        try
        {
            java.sql.PreparedStatement statement = conn.prepareStatement("SELECT idcube FROM cube WHERE name=? AND dbname=?");
            statement.setString(1, cube.getName());
            statement.setString(2, cube.getDbName());
            ResultSet rs = statement.executeQuery();

            if(rs.next())
            {
                cubeID = rs.getInt("idcube");
                isNew = false;
            }
            System.out.printf("CUBE-ID:%d\n", cubeID);
            rs.close();

            //cube does not exist in db
            if(cubeID < 0)
            {
                //insert cube
                statement = conn.prepareStatement("INSERT INTO cube (name, dbname, tablename) values (?, ?, ?)");
                statement.setString(1, cube.getName());
                statement.setString(2, cube.getDbName());
                statement.setString(3, cube.getTable());
                statement.execute();

                rs = statement.executeQuery("SELECT last_insert_id()");
                if(!rs.next())
                    throw new SQLException("Failed to get existing cube id!");

                cubeID = rs.getInt(1);
                System.out.printf("CUBE ID = %d\n", cubeID);
                rs.close();

                //insert dimensions
                statement = conn.prepareStatement("INSERT INTO dimension (name, cube_idcube) values (?, ?)");
                for(Dimension dime : cube.getDimensions())
                {
                    int dimeID = -1;
                    statement.clearParameters();
                    statement.setString(1, dime.getName());
                    statement.setInt(2, cubeID);
                    statement.execute();
                    java.sql.PreparedStatement getDimeID = conn.prepareStatement("SELECT last_insert_id()");
                    rs = getDimeID.executeQuery();
                    if(!rs.next())
                        throw new SQLException("Failed to get existing dimension id!");
                    dimeID = rs.getInt(1);
                    System.out.printf("DIME ID = %d\n", dimeID);
                    java.sql.PreparedStatement addGranularity = conn.prepareStatement("INSERT INTO granularity (name, dimension_iddimension, dimension_cube_idcube) values (?,?,?)");
                    for(String gran : dime.getGranules())
                    {
                        addGranularity.clearParameters();
                        addGranularity.setString(1, gran);
                        addGranularity.setInt(2, dimeID);
                        addGranularity.setInt(3, cubeID);
                        addGranularity.execute();
                    }
                }

                //insert measures
                statement = conn.prepareStatement("INSERT INTO measure (type, cube_idcube, columnname) values (?,?,?)");
                for(Measure ms : cube.getMeasures())
                {
                    statement.clearParameters();
                    statement.setString(1, ms.getType());
                    statement.setInt(2, cubeID);
                    statement.setString(3, ms.getColumnName());
                    statement.execute();
                }
            }
            else
                throw new SQLException("Cube already exists in database.");
        }
        catch(SQLException ex)
        {
            if(isNew)
            {
                java.sql.PreparedStatement destroyCube = conn.prepareStatement("DELETE FROM cube WHERE idcube=?");
                destroyCube.setInt(1, cubeID);
                destroyCube.execute();
            }
            throw ex;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}