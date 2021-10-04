package com.ma.hmcrfidserver.server;

import java.awt.Point;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test3 {
	public static void main(String[] args) throws Exception {
		
//		String requestPost = NetUtils.requestPost(new URL("http://localhost:8888/app/ttt"), "666");
//		System.out.println(requestPost);
		

		HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8888/app/zzz").openConnection();

		try {
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application");
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);

			try (ObjectOutputStream outputStream = new ObjectOutputStream(con.getOutputStream())) {
				outputStream.writeObject(new Point(11,22));
			}

			try (ObjectInputStream in = new ObjectInputStream(con.getInputStream())) {
				Point resp = (Point) in.readObject();
				System.out.println(resp);
			}
		} catch (Exception e) {
			System.err.println(e);
			throw e;
		} finally {
			if (con != null)
				con.disconnect();
		}
		
		
		
	}

}
