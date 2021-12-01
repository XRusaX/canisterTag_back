package com.ma.hmcapp.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ma.appcommon.WorkingDir;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.common.shared.Severity;

@RestController
public class ImageUploadController {

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private WorkingDir workingDir;

//	@Autowired
//	private AuthComponent authComponent;

//	@Autowired
//	private ThreadLocalRequest threadLocalRequest;

	@ResponseBody
	@PostMapping("/api/images")
	public String handleFileUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		threadLocalRequest.setRequest(req);
//		if (!authComponent.getUser().hasPermission(UserData.PERMISSION_SETTINGS))
//			return "";

		File dir = getUploadingDir();
		dir.mkdirs();

		try {
			FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
			FileItemStream item = iter.next();
			String suffix = item.getName().substring(item.getName().length() - 3);
			File tempFile = File.createTempFile("img", suffix, dir);
			FileUtils.copyInputStreamToFile(item.openStream(), tempFile);
			return "api/images/?filename=" + tempFile.getName();
		} catch (Exception e) {
			msgLogger.add(null, Severity.ERROR, e);
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return "";
		}
	}

	private File getUploadingDir() {
		return new File(workingDir.getWorkingDir(), "images");
	}

	@ResponseBody
	@GetMapping(value = "/api/images", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] handleFileDownload(@RequestParam("filename") String fileName) throws IOException {
		File dir = getUploadingDir();
		File file = new File(dir, fileName);
		InputStream in = new FileInputStream(file);
		return IOUtils.toByteArray(in);

	}
}
