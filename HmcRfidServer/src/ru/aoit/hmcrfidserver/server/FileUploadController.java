package ru.aoit.hmcrfidserver.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ru.aoit.appcommon.AuthImpl;
import ru.aoit.appcommon.MsgLogger;
import ru.aoit.appcommon.ThreadLocalRequest;
import ru.aoit.appcommon.WorkingDir;
import ru.aoit.appcommon.shared.auth.UserData;
import ru.nppcrts.common.shared.Severity;

@RestController
public class FileUploadController {

	@Autowired
	private MsgLogger msgLogger;

	@Autowired
	private WorkingDir workingDir;

	@Autowired
	private AuthImpl authComponent;

	@Autowired
	private ThreadLocalRequest threadLocalRequest;

	@ResponseBody
	@PostMapping("/firmware")
	public String handleFileUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		threadLocalRequest.setRequest(req);
		if (!authComponent.getUser().hasPermission(UserData.PERMISSION_SETTINGS))
			return "failure";

		try {
			FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
			if (iter.hasNext()) {
				FileItemStream item = iter.next();
				FileUtils.copyInputStreamToFile(item.openStream(), new File(getOutputDir(), item.getName()));
			}
		} catch (Exception e) {
			msgLogger.add(null, Severity.ERROR, e);
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
		}

		return "Success";
	}

	@ResponseBody
	@GetMapping(value = "/firmware/{file_name:.+}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> handleFileDownload(@PathVariable("file_name") String fileName)
			throws IOException {
		File file = new File(workingDir.getWorkingDir(), "firmware/" + fileName);
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

	protected File getOutputDir() {
		return new File(workingDir.getWorkingDir(), "firmware");
	}
}
