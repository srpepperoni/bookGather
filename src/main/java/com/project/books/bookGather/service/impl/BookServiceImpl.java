package com.project.books.bookGather.service.impl;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.books.bookGather.runnable.DowloadImage;
import com.project.books.bookGather.service.BookService;
import com.project.books.bookGather.utils.BookUtils;
import com.project.books.bookGather.utils.Constants;

@Service
public class BookServiceImpl implements BookService {

	private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

	BookUtils bookUtils = new BookUtils();

	@Override
	public JSONArray createEntries(String path) {
		JSONParser jsonParser = new JSONParser();
		JSONArray entries = null;

		try (FileReader reader = new FileReader(path)) {
			Object obj = jsonParser.parse(reader);
			JSONObject harFile = (JSONObject) obj;
			entries = bookUtils.getEntries(harFile);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return entries;
	}

	@Override
	public void getRequestInfo(String id, JSONArray entries, List<String> urls) {
		entries.forEach(emp -> bookUtils.packUrls((JSONObject) emp, id, urls));
	}
	
	@Override
	public void saveImages(List<String> urls, String destinationPath, int pages, int downloadBatch) {
		
		try {

			ExecutorService es = Executors.newCachedThreadPool();
			int hilos = Math.floorDiv(pages, downloadBatch) + 1;

			for (int j = 0; j < hilos; j++) {
				DowloadImage task = new DowloadImage(urls, destinationPath, j, pages, downloadBatch);
				es.execute(task);
			}

			es.shutdown();
			boolean finished;

			finished = es.awaitTermination(3, TimeUnit.MINUTES);
			if (finished) {
				log.info("Dowload COMPLETE");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void createPDF(String imagesPath, int elements, String bookName) {
		log.info("Entering createPDF method");

		try {
			Document document = new Document(bookUtils.getPageSize(imagesPath));
			String output = imagesPath + bookName + ".pdf";
			String input = "";
			FileOutputStream fos = new FileOutputStream(output);
			PdfWriter writer = PdfWriter.getInstance(document, fos);
			writer.open();
			document.open();
			for (int i = 1; i < elements + 1; i++) {
				input = imagesPath + "PA" + i + ".jpg";
				Image img = Image.getInstance(input);
				img.setAbsolutePosition(0, 0);
				document.add(img);
				document.newPage();
				log.info("Page " + input + " added to " + bookName + ".pdf file");
			}
			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("Exiting createPDF method");
	}

}
