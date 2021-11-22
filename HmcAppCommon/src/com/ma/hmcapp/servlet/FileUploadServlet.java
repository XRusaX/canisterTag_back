package com.ma.hmcapp.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ma.appcommon.SpringServlet;
import com.ma.appcommon.WorkingDir;

@SuppressWarnings("serial")
@WebServlet("/fileUpload")
public class FileUploadServlet extends SpringServlet {

	@Autowired
	private WorkingDir workingDir;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getParameter("fileName");
		String type = req.getParameter("type");
		OutputStream output = null;

		System.out.println("FileUploadServlet.doPost() : type=" + type);

		if ("png".equalsIgnoreCase(type)) {
			output = new FileOutputStream(new File(workingDir.getWorkingDir(), fileName + ".png"));
		} else {
			super.doPost(req, resp);
		}

		if (output != null) {
			try {
				ServletFileUpload upload = new ServletFileUpload();
				FileItemIterator iter = upload.getItemIterator(req);
				if (iter.hasNext()) {
					FileItemStream item = iter.next();

					System.out.println("FileUploadServlet.doPost() : upload file \"" + item.getFieldName() + "\"");
					InputStream stream = item.openStream();
					byte[] content = IOUtils.toByteArray(stream);

					output.write(content);
					output.flush();
					output.close();
				} else {
					System.out.println("FileUploadServlet.doPost() : no files");
				}
			} catch (FileUploadException e) {
				throw new IOException(e);
			}
		}

	}
}
