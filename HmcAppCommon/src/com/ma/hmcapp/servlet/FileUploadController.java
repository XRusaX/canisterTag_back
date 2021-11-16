package com.ma.hmcapp.servlet;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

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

import com.ma.appcommon.AuthComponent;
import com.ma.appcommon.ThreadLocalRequest;
import com.ma.appcommon.WorkingDir;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.common.shared.Severity;
import com.ma.hmcapp.datasource.OperatorDataSource;

@RestController
public class FileUploadController {

	@Autowired
	private OperatorDataSource operator;

	@Autowired
	private Database2 db;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private WorkingDir workingDir;

	@Autowired
	private AuthComponent authComponent;

	@Autowired
	private ThreadLocalRequest threadLocalRequest;

	@ResponseBody
	@PostMapping("/myController")
	public String handleFileUpload(HttpServletRequest req, HttpServletResponse resp, @RequestParam("id") long fileName)
			throws IOException {
		threadLocalRequest.setRequest(req);
		if (!authComponent.getUser().hasPermission(UserData.PERMISSION_SETTINGS))
			return "failure";

		File dir = getUploadingDir();
		dir.mkdirs();
//		FileUtils.cleanDirectory(dir);
		try {
			FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
			if (iter.hasNext()) {
				FileItemStream item = iter.next();
				FileUtils.copyInputStreamToFile(item.openStream(), new File(dir, fileName + ".png"));
			}
		} catch (Exception e) {
			msgLogger.add(null, Severity.ERROR, e);
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		}

		return "api/myController?id=" + fileName;
	}

	private File getUploadingDir() {
		return new File(workingDir.getWorkingDir(), "uploaded");
	}

	public Map<String, String> getFilesList() {

		File[] dirs = getUploadingDir().listFiles((FileFilter) pathname -> pathname.isDirectory());

		Map<String, String> res = new LinkedHashMap<>();
		for (File dir : dirs) {
			String[] files = dir.list();
			if (files != null && files.length > 0)
				res.put(dir.getName(), files[0]);
		}

		// for (HmcType line : HmcType.values()) {
		// File dir = new File(getFirmwareDir(), line.name());
		// String[] files = dir.list();
		// if (files != null && files.length > 0)
		// res.put(line.name(), files[0]);
		// }
		return res;
	}

	@ResponseBody
	@GetMapping(value = "/myController", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] handleFileShow(@RequestParam("id") String fileName) throws IOException {

		File dir = getUploadingDir();
//		String[] files = dir.list();

		File file = new File(dir, fileName + ".png");
//		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//		return ResponseEntity.ok()
//				// Content-Disposition
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
//				// Content-Type
//				.contentType(MediaType.IMAGE_PNG)
//				// Contet-Length
//				.contentLength(file.length()) //
//				.body(resource);

		InputStream in = new FileInputStream(file);
		return IOUtils.toByteArray(in);

	}

	public Map<String, String> getUploadedList() {

		File[] dirs = getUploadingDir().listFiles((FileFilter) pathname -> pathname.isDirectory());

		Map<String, String> res = new LinkedHashMap<>();
		for (File dir : dirs) {
			String[] files = dir.list();
			if (files != null && files.length > 0)
				res.put(dir.getName(), files[0]);
		}

		// for (HmcType line : HmcType.values()) {
		// File dir = new File(getFirmwareDir(), line.name());
		// String[] files = dir.list();
		// if (files != null && files.length > 0)
		// res.put(line.name(), files[0]);
		// }
		return res;
	}

}
