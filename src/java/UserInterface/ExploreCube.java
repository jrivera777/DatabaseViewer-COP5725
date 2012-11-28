/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import DBDataStructures.Cube;
import DBDataStructures.Dimension;
import DBDataStructures.Measure;
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String dbName = (String) request.getSession().getAttribute("dbname");
        String dbAddr = (String) request.getSession().getAttribute("addr");
        String dbUser = (String) request.getSession().getAttribute("user");
        String dbPW = (String) request.getSession().getAttribute("pw");
        String cubeName = request.getParameter("cubeName");
        String measure = request.getParameter("measure");

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

                conn.close();
                conn = DriverManager.getConnection(userConnect);

                StringBuilder table = new StringBuilder();
                for(Dimension dime : cube.getDimensions())
                {

                    String header = String.format("<table id=\"%s-table\""
                            + "class=\"table table-striped table-hover\">"
                            + "<thead><tr><th>%s</th><th>Measure</th></tr>"
                            + "</thead>", dime.getName(), dime.getName());
                    table.append(header);
                    table.append("<tbody>");
                    String row = buildRowForDimension(dime, measure, cube.getTable(), conn);
                    table.append(row);
                    table.append("</tbody></table>");
                }


                out.println(table.toString());
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

    private String buildRowForDimension(Dimension dime, String measure, String table, Connection conn)
    {
        StringBuilder sb = new StringBuilder();
        String rowStart = "<tr id=\"node-%s\">";
        String childRow = "<tr id=\"node-%s\" class=\"child-of-node-%s\">";
        try
        {
            sb.append(String.format(rowStart, dime.getName()));
            java.sql.PreparedStatement statement = conn.prepareStatement(String.format("SELECT %s FROM %s;", measure, table));
            ResultSet rs = statement.executeQuery();

            double mesResult = -1;
            while(rs.next())
                mesResult = rs.getDouble(1);
            sb.append(String.format("<td>ALL</td><td>%.1f</td>", mesResult));
            sb.append("</tr>");
            statement.close();

            ArrayList<String> parenAns = new ArrayList<String>();

            String magic = solveQuery(conn, measure, table, dime.getName(), 0, dime.getGranules(), parenAns);
            sb.append(magic);

        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public String solveQuery(Connection conn, String measure, String table, String myParent, int currGran, ArrayList<String> grans, ArrayList<String> parentAnswers) throws SQLException
    {
        if(currGran >= grans.size())
            return "";

        String result = "";
        String childRow = "<tr id=\"node-%s\" class=\"child-of-node-%s\">";
        String query = "SELECT %s, %s FROM %s";

        for(int i = 0; i < currGran; i++)
        {
            if(i == 0)
                query += " WHERE ";
            else
                query += " and ";


            Class<?> type = null;
            try
            {
                Integer.parseInt(parentAnswers.get(i));
                type = int.class;
            }
            catch(NumberFormatException ex)
            {
                try
                {
                    Double.parseDouble(parentAnswers.get(i));
                    type = double.class;
                }
                catch(NumberFormatException dblEx)
                {
                    type = String.class;
                }
            }
            if(type == int.class)
                query += String.format(" %s = %d", grans.get(i), Integer.parseInt(parentAnswers.get(i)));
            else if(type == double.class)
                query += String.format(" %s = %f", grans.get(i), Double.parseDouble(parentAnswers.get(i)));
            else
                query += String.format(" %s = \'%s\'", grans.get(i), parentAnswers.get(i));
        }

        query += " GROUP BY %s;";

        String granule = grans.get(currGran);
        query = String.format(query, granule, measure, table, granule);
        System.out.println(query);
        java.sql.PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();


        int uniqueCounter = 0;
        while(rs.next())
        {
            StringBuilder sb = new StringBuilder();
            ++uniqueCounter;
            String granResult = rs.getString(1);
            double mRes = rs.getDouble(2);
            sb.append(String.format(childRow, granule + uniqueCounter, myParent));
            sb.append(String.format("<td>%s=%s</td><td>%.1f</td>", granule.toUpperCase(), granResult, mRes));
            sb.append("</tr>");
            parentAnswers.add(granResult);
            String tmp = sb.toString() + solveQuery(conn, measure, table, granule + uniqueCounter, ++currGran, grans, parentAnswers);
            System.out.println(tmp);
            result += tmp;
            currGran--;
            parentAnswers.remove(parentAnswers.size() - 1);
        }
        return result;
    }
}
