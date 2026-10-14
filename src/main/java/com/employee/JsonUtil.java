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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    // One shared mapper: ObjectMapper is thread-safe and costly to create
    private static final ObjectMapper MAPPER = new ObjectMapper();

public static String toJson(Object value) throws ServletException {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ServletException("Error converting object to JSON", e);
        }
    }

    // JSON request body such as {"id":7,"name":"Ravi","age":30} -> EmployeePojo
public static EmployeePojo fromJson(HttpServletRequest req) throws ServletException, IOException {
        try {
            return MAPPER.readValue(req.getInputStream(), EmployeePojo.class);
        } catch (JsonProcessingException e) {
            throw new ServletException("Error converting JSON to object", e);
        }
    }
}