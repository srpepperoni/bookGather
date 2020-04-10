package com.project.books.bookGather.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;

public class BookUtils {
	
	private static final Logger log = LoggerFactory.getLogger(BookUtils.class);

	public Rectangle getPageSize(String imagesPath) {
		try {
			String input = imagesPath + "PA1.jpg";
			Image img = Image.getInstance(input);
			Rectangle pagesize = new Rectangle(img.getScaledWidth(), img.getScaledHeight());
			return pagesize;
		} catch (IOException | BadElementException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String createDestinationFilePath(String destinationPath, String url) {
		String path = destinationPath + url.split("&pg=")[1].split("&")[0] + ".jpg";
		return path;
	}
	
	public void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
		log.info("Image of Page " + destinationFile + " saved");
	}

	public JSONArray getEntries(JSONObject fullFile) {
		JSONObject logObject = (JSONObject) fullFile.get("log");
		return (JSONArray) logObject.get("entries");
	}

	public void packUrls(JSONObject entry, String id, List<String> urls) {
		String verb = (String) ((JSONObject) ((JSONObject) entry).get("request")).get("method");
		String url = (String) ((JSONObject) ((JSONObject) entry).get("request")).get("url");

		if ("GET".equals(verb) && !urls.contains(url) && url.contains(Constants.endpoint_part_one + id + Constants.endpoint_part_two) && !url.contains("&w=685")) {
			urls.add(url);
		}
	}
	
	public String getIdentification(JSONArray entries) {
		
		for (Object entry : entries) {
			String verb = (String) ((JSONObject) ((JSONObject) entry).get("request")).get("method");
			String url = (String) ((JSONObject) ((JSONObject) entry).get("request")).get("url");
			if ("GET".equals(verb) && url.contains(Constants.endpoint_part_one)) {
				return url.split("id=")[1].split("&")[0];
			}
		}
		
		return null;
	}

}
