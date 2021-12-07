package com.ma.hmcapp.servlet;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ma.appcommon.AuthComponent;
import com.ma.appcommon.ThreadLocalRequest;
import com.ma.appcommon.WorkingDir;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.common.shared.Severity;

@RestController
public class FirmwareController {

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private WorkingDir workingDir;

	@Autowired
	private AuthComponent authComponent;

	@Autowired
	private ThreadLocalRequest threadLocalRequest;

	@ResponseBody
	@PostMapping("/api/firmware")
	public String handleFileUpload(HttpServletRequest req, HttpServletResponse resp, @RequestParam("line") String line)
			throws IOException {
		threadLocalRequest.setRequest(req);
		if (!authComponent.getUser().hasPermission(UserData.PERMISSION_SETTINGS))
			return "";

		File dir = new File(getFirmwareDir(), line);
		dir.mkdirs();
		FileUtils.cleanDirectory(dir);

		try {
			FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
			if (iter.hasNext()) {
				FileItemStream item = iter.next();
				FileUtils.copyInputStreamToFile(item.openStream(), new File(dir, item.getName()));
			}
		} catch (Exception e) {
			msgLogger.add(null, Severity.ERROR, e);
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		}

		return "";
	}

	@ResponseBody
	@GetMapping(value = "/api/firmware/version")
	public String handleVersion(@RequestParam("line") String line) throws IOException {
		File dir = new File(getFirmwareDir(), line);
		String[] files = dir.list();
		return files[0];
	}

	@ResponseBody
	@GetMapping(value = "/api/firmware", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> handleFileDownload(@RequestParam("line") String line)
			throws IOException {

		File dir = new File(getFirmwareDir(), line);
		String[] files = dir.list();

		File file = new File(dir, files[0]);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				// Content-Type
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				// Contet-Length
				.contentLength(file.length()) //
				.body(resource);
	}

	private File getFirmwareDir() {
		return new File(workingDir.getWorkingDir(), "firmware");
	}

	public Map<String, String> getFirmwareList() {

		File[] dirs = getFirmwareDir().listFiles((FileFilter) pathname -> pathname.isDirectory());

		Map<String, String> res = new LinkedHashMap<>();

		if (dirs != null) {
			for (File dir : dirs) {
				String[] files = dir.list();
				if (files != null && files.length > 0)
					res.put(dir.getName(), files[0]);
			}
		}

		// for (HmcType line : HmcType.values()) {
		// File dir = new File(getFirmwareDir(), line.name());
		// String[] files = dir.list();
		// if (files != null && files.length > 0)
		// res.put(line.name(), files[0]);
		// }
		return res;
	}

	public void removeFirmware(String type) throws IOException {
		FileUtils.deleteDirectory(new File(getFirmwareDir(), type));
	}
}
