package com.ma.hmccanistertags.server;


import javax.servlet.annotation.WebServlet;

import com.ma.common.gwtapp.server.SpringGwtRemoteServiceServlet;

@SuppressWarnings("serial")
@WebServlet("/canister-rfid/gwtservice/*")
public class GwtRemoteServiceServlet extends SpringGwtRemoteServiceServlet {
}