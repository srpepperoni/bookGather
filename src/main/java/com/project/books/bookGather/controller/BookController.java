package com.project.books.bookGather.controller;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.books.bookGather.service.BookService;

@RestController
public class BookController {

	@Autowired
	BookService bookService;

	@GetMapping(path = { "/test" })
	public String test() {
		return "Hola Mundo";
	}

	@PostMapping(path = { "/generateFile" })
	public void generateFileFromHAR(@RequestParam String origin, @RequestParam String destiny,
			@RequestParam String identification) {
		List<String> urls = new ArrayList<String>();
		System.out.println("------------------------");
		System.out.println("origin " + origin);
		System.out.println("destiny " + destiny);

		// Saco todas las entries del fichero
		JSONArray entries = bookService.createEntries(origin);

		// Guardo la info de las entries con Request tipo GET en urls
		bookService.getRequestInfo(identification, entries, urls);

		bookService.saveImages(urls, destiny);
	}

	@PostMapping(path = { "/createPDF" })
	public void createPDF(@RequestParam String imagesPath, @RequestParam int elements, @RequestParam String bookName) {
		try {
			Document document = new Document(PageSize.A4);
			String output = imagesPath + bookName + ".pdf";
			String input = "";
			FileOutputStream fos = new FileOutputStream(output);
			PdfWriter writer = PdfWriter.getInstance(document, fos);
			writer.open();
			document.open();
			for (int i = 1; i < elements + 1; i++) {
				input = imagesPath + "PA" + i + ".jpg";
				document.add(Image.getInstance(input));
			}
			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
