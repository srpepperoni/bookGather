package com.project.books.bookGather.runnable;

import java.io.IOException;
import java.util.List;

import com.project.books.bookGather.utils.BookUtils;

public class DowloadImage implements Runnable {
	private int start;
	private int end;
	private List<String> urls;
	private String destinationPath;
	private int id;
	private int pages;
	private int batch;

	BookUtils bookUtils = new BookUtils();

	public DowloadImage(List<String> urls, String destinationPath, int id, int pages, int batch) {
		this.urls = urls;
		this.destinationPath = destinationPath;
		this.id = id;
		this.pages = pages;
		this.batch = batch;
	}

	@Override
	public void run() {
		this.start = this.id * this.batch;
		this.end = ((this.id * this.batch) + this.batch) > this.pages ? this.pages : ((this.id * this.batch) + this.batch);
		
		try {
			for (int i = start; i < end; i++) {
				bookUtils.saveImage(this.urls.get(i),
						bookUtils.createDestinationFilePath(destinationPath, this.urls.get(i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
