package com.ma.hmccanistertags.server;


import javax.servlet.annotation.WebServlet;

import com.ma.common.gwtapp.server.SpringGwtRemoteServiceServlet;

@SuppressWarnings("serial")
@WebServlet("/hmcserver/gwtservice/*")
public class GwtRemoteServiceServlet extends SpringGwtRemoteServiceServlet {
}