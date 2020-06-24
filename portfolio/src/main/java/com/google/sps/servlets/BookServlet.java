package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Returns book titles and reviews as a JSON array, e.g. [{"title": Othello, "reviews": {"Nice", "Bad"}}] */
@WebServlet("/book-data")
public class RestaurantDataServlet extends HttpServlet {


}