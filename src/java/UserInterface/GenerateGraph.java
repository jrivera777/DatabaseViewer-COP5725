/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import DBDataStructures.Cube;
import DBDataStructures.Dimension;
import com.google.gson.Gson;
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

/**
 *
 * @author Joseph
 */
public class GenerateGraph extends HttpServlet
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
        String dimension = request.getParameter("dimension");
        String measure = request.getParameter("measure");
        String lookup = request.getParameter("lookup");
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

                Cube cube = ExploreCube.getCubeByName(cubeName, dbName, conn);
                Dimension dime = null;
                for(Dimension d : cube.getDimensions())
                    if(d.getName().equals(dimension))
                        dime = d;

                conn.close();
                conn = DriverManager.getConnection(userConnect);

                String query = "";
                if(dime.getGranules().size() > 1)
                {
                    query = String.format("SELECT %s, %s FROM %s WHERE %s=? GROUP BY %s;",
                            dime.getGranules().get(1), measure, cube.getTable(),
                            dime.getGranules().get(0), dime.getGranules().get(1));
                }
                else
                {
                    query = String.format("SELECT %s, %s FROM %s GROUP BY %s;",
                            dime.getGranules().get(0), measure, cube.getTable(),
                            dime.getGranules().get(0));
                }


                java.sql.PreparedStatement statement = conn.prepareStatement(query);

                Class<?> type = null;
                try
                {
                    Integer.parseInt(lookup);
                    type = int.class;
                }
                catch(NumberFormatException ex)
                {
                    try
                    {
                        Double.parseDouble(lookup);
                        type = double.class;
                    }
                    catch(NumberFormatException dblEx)
                    {
                        type = String.class;
                    }
                }
                if(type == int.class)
                    statement.setInt(1, Integer.parseInt(lookup));
                else if(type == double.class)
                    statement.setDouble(1, Double.parseDouble(lookup));
                else
                    statement.setString(1, lookup);

                ResultSet rs = statement.executeQuery();
                ArrayList<String> x = new ArrayList<String>();
                ArrayList<String> y = new ArrayList<String>();
                while(rs.next())
                {
                    x.add(rs.getString(1));
                    y.add(rs.getString(2));
                }

                ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
                results.add(x);
                results.add(y);

                String json = new Gson().toJson(results);
                System.out.println(json);

                out.println(json);
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
