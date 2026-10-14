package com.employee;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Api Servlet", description = "api servlet", urlPatterns = { "/api"})

public class ApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static EmployeeHibernateApi api;

    public ApiServlet () {
        super();
        api = new EmployeeHibernateApi();
    }

    @Override
    //Retrieving
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // HTML forms can only send GET or POST, so the delete form uses GET with action=delete
        String action = req.getParameter("action");
        if (action != null && action.contentEquals("delete")) {
            doDelete(req, resp);
            return;
        }

        String idParam = req.getParameter("id");
        // Holds either the whole list or one employee; both are turned into JSON the same way
        Object response;
        try {
            if (idParam == null) {
                // No id: list every employee
                List<EmployeePojo> list = api.selectAll();
                response = list;
            } else {
                int id = Integer.valueOf(idParam);
                EmployeePojo p = api.select(id);
                if (p == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No employee with id " + id);
                    return;
                }
                response = p;
            }
        } catch (SQLException e) {
            throw new ServletException("Error retrieving employee data", e);
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.write(JsonUtil.toJson(response));
    }

   
   //Creating
   @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Body is JSON such as {"id":7,"name":"Ravi","age":30}; Jackson fills in all three fields
        EmployeePojo p = JsonUtil.fromJson(req);
        try {
              api.insert(p);
        } catch (SQLException e) {
            throw new ServletException("Error saving object", e);
        }

        PrintWriter out = resp.getWriter();
        out.println("Employee created successfully");
    }

    //Updating
    @Override 
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Body is JSON such as {"id":7,"name":"Ravi","age":31}; the id picks which employee to change
        EmployeePojo p = JsonUtil.fromJson(req);
        boolean updated;
        try {
              updated = api.update(p.getId(), p);
        } catch (SQLException e) {
            throw new ServletException("Error updating object", e);
        }
        if (!updated) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No employee with id " + p.getId());
            return;
        }
        PrintWriter out = resp.getWriter();
        out.println("Employee updated successfully");
    }
    
    //Deleting
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.valueOf(req.getParameter("id"));
        boolean deleted;
        try {
             deleted = api.delete(id);
        } catch (SQLException e) {
            throw new ServletException("Error deleting object", e);
        }
        if (!deleted) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No employee with id " + id);
            return;
        }
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1>Employee Deleted</h1>");
    }

}