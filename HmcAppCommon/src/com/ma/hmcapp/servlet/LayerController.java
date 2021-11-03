package com.ma.hmcapp.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
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
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.common.shared.Severity;
import com.ma.hmcapp.datasource.CompanyDataSource;
import com.ma.hmcapp.datasource.RoomLayerDataSource;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.RoomLayer;

@RestController
public class LayerController {

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private AuthComponent authComponent;

	@Autowired
	private ThreadLocalRequest threadLocalRequest;

	@Autowired
	private RoomLayerDataSource roomLayerDataSource;

	@Autowired
	private CompanyDataSource companyDataSource;

	@Autowired
	private Database2 database;

	@ResponseBody
	@PostMapping("/uploadlayer")
	public String handleFileUpload(HttpServletRequest req, HttpServletResponse resp, @RequestParam("id") Long companyId)
			throws IOException {
		threadLocalRequest.setRequest(req);

		database.execVoid(em -> {
			Company company = companyDataSource.load(em, companyId);
			RoomLayer roomLayer = roomLayerDataSource.loadByCompany(em, company);
			if (roomLayer == null)
				roomLayer = new RoomLayer("xxx", company, null);

			try {
				FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
				if (iter.hasNext()) {
					FileItemStream item = iter.next();
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					IOUtils.copy(item.openStream(), byteArrayOutputStream);
					byte[] bs = byteArrayOutputStream.toByteArray();
					roomLayer.image2 = bs;
				}

				roomLayerDataSource.store(em, roomLayer);
			} catch (Exception e) {
				msgLogger.add(null, Severity.ERROR, e);
				try {
					resp.sendError(HttpServletResponse.SC_FORBIDDEN);
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
			}
		});

		return "Success";
	}

	@ResponseBody
	@GetMapping(value = "/uploadlayer", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> handleFileDownload(@RequestParam("id") Long companyId)
			throws IOException {
		return database.exec(em -> {
			Company company = companyDataSource.load(em, companyId);
			RoomLayer roomLayer = roomLayerDataSource.loadByCompany(em, company);

			if (roomLayer == null)
				return ResponseEntity.noContent().build();

			InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(roomLayer.image2));

			return ResponseEntity.ok()
					// Content-Disposition
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "zz.jpg")
					// Content-Type
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					// Contet-Length
					.contentLength(roomLayer.image2.length) //
					.body(resource);
		});
	}

}
