package com.project.books.bookGather.service;

import java.util.List;

import org.json.simple.JSONArray;

public interface BookService {
	
	public JSONArray createEntries(String path);
	
	public void getRequestInfo(JSONArray entries, List<String> urls);
	
	public void saveImages(List<String> urls, String destinationPath);
	
	public void createPDF(String imagesPath, int elements, String bookName);

}
