/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import DBDataStructures.Cube;
import DBDataStructures.Dimension;
import DBDataStructures.Measure;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Joseph
 */
public class ExploreCube extends HttpServlet
{
    protected final String ERROR_MESSAGE = "<div class=\"alert alert-error\">"
            + "<button type=\"button\" class=\"close\" "
            + "data-dismiss=\"alert\">×</button>%s</div>";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String dbName = (String) request.getSession().getAttribute("dbname");
        String dbAddr = (String) request.getSession().getAttribute("addr");
        String dbUser = (String) request.getSession().getAttribute("user");
        String dbPW = (String) request.getSession().getAttribute("pw");
        String cubeName = request.getParameter("cubeName");

        Connection conn = null;
        try
        {

            String userConnect = "jdbc:mysql://"
                    + dbAddr + "/" + dbName + "?user=" + dbUser
                    + "&password=" + dbPW;
            //String cop5725Connect = "jdbc:mysql://localhost/cop5725?user=test&password=test";
            String cop5725Connect = "jdbc:mysql://172.23.19.231/cop5725?user=user&password=password";

            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(cop5725Connect);

                Cube cube = getCubeByName(cubeName, dbName, conn);

                String jsonCube = new Gson().toJson(cube);
                System.out.println(jsonCube);
                out.println(jsonCube);
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
                ex.printStackTrace();;
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
                        out.println("<div class=\"alert alert-error\">" + ex.getMessage() + "</div>");
                        ex.printStackTrace();
                    }
                }
            }
        }
        finally
        {
            out.close();
        }
    }

    private Cube getCubeByName(String cubeName, String dbName, Connection conn) throws SQLException
    {
        Cube cube = null;
        java.sql.PreparedStatement statement = conn.prepareStatement("SELECT * FROM cube where dbname=? and name=?");
        statement.setString(1, dbName);
        statement.setString(2, cubeName);

        ResultSet rs = statement.executeQuery();
        int cubeID = -1;
        //get cube info
        while(rs.next())
        {
            cubeID = rs.getInt(1);
            if(cubeID < 0)
                throw new SQLException("Failed to retrieve cube!");

            cube = new Cube();
            cube.id = cubeID;
            cube.setDbName(dbName);
            cube.setName(rs.getString(2));
            cube.setTable(rs.getString(4));
        }

        //get all cube data
        statement = conn.prepareStatement("SELECT iddimension, name FROM dimension where cube_idcube=?");
        statement.clearParameters();
        statement.setInt(1, cube.id);

        rs = statement.executeQuery();
        int dimeID = -1;
        ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
        while(rs.next())
        {
            dimeID = rs.getInt(1);
            if(dimeID < 0)
                throw new SQLException("Failed to retrieve dimension!");
            Dimension dime = new Dimension();
            dime.id = dimeID;
            dime.setName(rs.getString(2));

            ArrayList<String> granules = new ArrayList<String>();
            java.sql.PreparedStatement grans = conn.prepareStatement("SELECT name from granularity where dimension_iddimension=?");
            grans.setInt(1, dime.id);
            ResultSet dimeGrans = grans.executeQuery();
            while(dimeGrans.next())
                granules.add(dimeGrans.getString(1));
            dime.setGranules(granules);
            dimensions.add(dime);
        }
        cube.setDimensions(dimensions);

        ArrayList<Measure> measures = new ArrayList<Measure>();
        java.sql.PreparedStatement getMeasures = conn.prepareStatement("SELECT type, columnname from measure where cube_idcube=?");
        getMeasures.setInt(1, cube.id);
        ResultSet cubeMs = getMeasures.executeQuery();
        while(cubeMs.next())
        {
            Measure m = new Measure();
            m.setType(cubeMs.getString(1));
            m.setColumnName(cubeMs.getString(2));
            measures.add(m);
        }
        cube.setMeasures(measures);

        return cube;
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
