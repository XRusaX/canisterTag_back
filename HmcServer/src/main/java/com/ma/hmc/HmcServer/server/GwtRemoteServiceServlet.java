package com.ma.hmc.HmcServer.server;

import javax.servlet.annotation.WebServlet;

import com.ma.common.gwtapp.server.SpringGwtRemoteServiceServlet;

@SuppressWarnings("serial")
@WebServlet("/hmcrfidserver/gwtservice/*")
public class GwtRemoteServiceServlet extends SpringGwtRemoteServiceServlet {
}