package com.ma.hmcrfidserver.server;

import java.io.File;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.ma.appcommon.WorkingDir;
import com.ma.appcommon.upload.FileUploadServlet;

@SuppressWarnings("serial")
@WebServlet("/uploadSoftware")
public class SoftwareUploadServlet extends FileUploadServlet {

	@Autowired
	private WorkingDir workingDir;

	@Override
	protected File getOutputFile(HttpServletRequest req) {
		return new File(workingDir.getWorkingDir(), "software.bin");
	}
}
