package com.ma.hmcrfidserver.server;

import java.awt.Point;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ttt")
public class Controller2 {

	@GetMapping
	public Point getBook() {
		return new Point(10, 30);
	}

//	@PostMapping("/ttt")
//	public Point getBook(@RequestBody String x) {
//		return new Point(10, Integer.parseInt(x));
//	}
}
