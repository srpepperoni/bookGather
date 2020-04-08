package com.project.books.bookGather.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.books.bookGather.service.BookService;

@RestController
public class BookController {
	
	private static final Logger log = LoggerFactory.getLogger(BookController.class);

	@Autowired
	BookService bookService;

	@GetMapping(path = { "/test" })
	public String test() {
		return "Hola Mundo";
	}

	/**
	 * @param origin It is where JSON file (HAR file) is located
	 * @param destiny It is where images and pdf file will be created
	 * @param identification It is the identification of request we wanna extract
	 * @param bookName Name file for pdf
	 * @param pages Number of pages of the book
	 */
	@PostMapping(path = { "/generateFile" })
	public void generateFileFromHAR(@RequestParam String origin, @RequestParam String destiny,
			@RequestParam String identification, @RequestParam String bookName, @RequestParam int pages) {
		log.info("Entering generateFileFromHAR method");
		List<String> urls = new ArrayList<String>();

		JSONArray entries = bookService.createEntries(origin);
		bookService.getRequestInfo(identification, entries, urls);
		bookService.saveImages(urls, destiny);
		bookService.createPDF(destiny, pages, bookName);
		
		log.info("Exiting generateFileFromHAR method");
	}

}
