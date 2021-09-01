package ru.aoit.hmcapp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import ru.aoit.appcommon.SpringServlet;

//@WebServlet("/hmcrfidserver/test")
@SuppressWarnings("serial")
public class RuslanTestServlet extends SpringServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().print("TEST SERVLET GET");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		writer.println("TEST SERVLET POST");
		BufferedReader reader = req.getReader();
		IOUtils.copy(reader, writer);
	}

}
