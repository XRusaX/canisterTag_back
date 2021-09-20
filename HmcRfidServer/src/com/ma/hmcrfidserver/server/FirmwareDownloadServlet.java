package com.ma.hmcrfidserver.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.SpringUtils;
import com.ma.appcommon.WorkingDir;
import com.ma.appcommon.upload.FileDownloadServlet;

@SuppressWarnings("serial")
@WebServlet("/api/firmwareXXX/download/*")
@Component
public class FirmwareDownloadServlet extends FileDownloadServlet {

	@Autowired
	private WorkingDir workingDir;

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		SpringUtils.autowire(arg0, this);
		super.service(arg0, arg1);
	}
	
	@Override
	protected void sendDataToOutput(HttpServletRequest request, OutputStream stream) throws IOException {
		FileUtils.copyFile(getFile(request), stream);
	}

	@Override
	protected String getDownloadedFileName(HttpServletRequest req) {
		return getFile(req).getName();
	}

	private File getFile(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String fileName = uri.substring(uri.lastIndexOf("/") + 1);
		File file = new File(workingDir.getWorkingDir(), "firmware/" + fileName);
		return file;
	}

}
