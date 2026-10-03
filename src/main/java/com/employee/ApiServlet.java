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

@WebServlet(name = "HelloWorld", description = "sample servlet", urlPatterns = { "/api"})

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

        // No id: list every employee
        if (idParam == null) {
            List<EmployeePojo> list;
            try {
                list = api.selectAll();
            } catch (SQLException e) {
                throw new ServletException("Error retrieving employee list", e);
            }
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<h1>All Employees</h1>");
            for (EmployeePojo p : list) {
                print(out, p);
            }
            return;
        }

        int id = Integer.valueOf(idParam);
        EmployeePojo p;
        try {
             p = api.select(id);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving single employee", e);
        }
        if (p == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No employee with id " + id);
            return;
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1>Employee Details</h1>");
        print(out, p);
    }

   
   //Creating
   @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String id = req.getParameter("id");
        String age = req.getParameter("age");

        EmployeePojo p = new EmployeePojo();
        p.setAge(Integer.valueOf(age));
        p.setId(Integer.valueOf(id));
        p.setName(name);
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
        String name = req.getParameter("name");
        String id = req.getParameter("id");
        String age = req.getParameter("age");

        EmployeePojo p = new EmployeePojo();
        p.setAge(Integer.valueOf(age));
        p.setId(Integer.valueOf(id));
        p.setName(name);
        try {
              api.update(p.getId(), p);
        } catch (SQLException e) {
            throw new ServletException("Error updating object", e);
        }
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

    private void print(PrintWriter out, EmployeePojo p) {
        out.println("<br>");
        out.println("Name: " + p.getName());
        out.println("<br>");
        out.println("Age: " + p.getAge());
        out.println("<br>");
        out.println("Id: " + p.getId());

    }
}