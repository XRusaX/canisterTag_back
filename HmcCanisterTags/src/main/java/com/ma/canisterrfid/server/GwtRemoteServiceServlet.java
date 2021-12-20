package com.ma.canisterrfid.server;


import javax.servlet.annotation.WebServlet;

import com.ma.common.gwtapp.server.SpringGwtRemoteServiceServlet;

@SuppressWarnings("serial")
@WebServlet("/canisterrfid/gwtservice/*")
public class GwtRemoteServiceServlet extends SpringGwtRemoteServiceServlet {
}