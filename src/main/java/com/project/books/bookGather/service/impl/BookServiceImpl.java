package com.project.books.bookGather.service.impl;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.project.books.bookGather.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private static final String endpoint_part_one = "https://books.google.es/books/content?id=";
	private static final String endpoint_part_two = "&hl=es&authuser=0&pg=P";

	@Override
	public JSONArray createEntries(String path) {
		JSONParser jsonParser = new JSONParser();
		JSONArray entries = null;

		try (FileReader reader = new FileReader(path)) {
			Object obj = jsonParser.parse(reader);
			JSONObject harFile = (JSONObject) obj;
			entries = getEntries(harFile);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return entries;
	}

	@Override
	public void getRequestInfo(String id, JSONArray entries, List<String> urls) {
		entries.forEach(emp -> packUrls((JSONObject) emp, id, urls));
	}

	@Override
	public void saveImages(List<String> urls, String destinationPath) {
		for (String url : urls) {
			try {
				saveImage(url, createDestinationFilePath(destinationPath, url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String createDestinationFilePath(String destinationPath, String url) {
		String path = destinationPath + url.split("&pg=")[1].split("&")[0] + ".jpg";
		return path;
	}
	
	private static void saveImage(String imageUrl, String destinationFile) throws IOException {
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
	}

	private static JSONArray getEntries(JSONObject fullFile) {
		JSONObject logObject = (JSONObject) fullFile.get("log");
		return (JSONArray) logObject.get("entries");
	}

	private void packUrls(JSONObject entry, String id, List<String> urls) {
		String verb = (String) ((JSONObject) ((JSONObject) entry).get("request")).get("method");
		String url = (String) ((JSONObject) ((JSONObject) entry).get("request")).get("url");

		if ("GET".equals(verb) && url.contains(endpoint_part_one + id + endpoint_part_two) && !url.contains("&w=685")) {
			urls.add(url);
		}
	}

}
