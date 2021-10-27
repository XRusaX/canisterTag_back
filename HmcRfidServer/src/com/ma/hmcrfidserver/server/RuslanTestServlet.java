package com.ma.hmcrfidserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.ma.appcommon.SpringServlet;

@WebServlet("/test")
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
