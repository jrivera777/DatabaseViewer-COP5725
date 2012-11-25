/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import DBDataStructures.Cube;
import DBDataStructures.Dimension;
import java.sql.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
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

            String dimensions = request.getParameter("dimes");
            JSONArray jsonarr = (JSONArray) JSONValue.parse(dimensions);
            ArrayList<Dimension> dimes = new ArrayList<Dimension>();
            for(int i = 0; i < jsonarr.size(); i++)
            {
                Dimension newDimension = new Dimension();
                JSONObject jObj = (JSONObject) jsonarr.get(i);
                newDimension.setName((String) jObj.get("name"));
                JSONObject table = (JSONObject) jObj.get("data");
                newDimension.setTable((String) table.get("name"));
                JSONArray grans = (JSONArray) table.get("data");
                ArrayList<String> dimeGrans = new ArrayList<String>();
                for(int j = 0; j < grans.size(); j++)
                    dimeGrans.add((String) grans.get(j));
                newDimension.setGranules(dimeGrans);
                dimes.add(newDimension);
            }
            newCube.setDimensions(dimes);

            System.out.println(newCube.toString());
            String userConnect = "jdbc:mysql://localhost/cop5725?user=test&password=test";
            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(userConnect);

                
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
                        out.printf(ERROR_MESSAGE, ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            out.println("<div class=\"alert alert-error\">" + ex.getMessage() + "</div>");
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