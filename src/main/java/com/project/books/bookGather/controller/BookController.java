package com.project.books.bookGather.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * @param origin         It is where JSON file (HAR file) is located
	 * @param destiny        It is where images and pdf file will be created
	 * @param bookName       Name file for pdf
	 */
	@PostMapping(path = { "/generateFile" })
	public Map<String, Object> generateFileFromHAR(@RequestParam String origin, @RequestParam String destiny,
			@RequestParam String bookName) {
		
		log.info("Entering generateFileFromHAR method");
		List<String> urls = new ArrayList<String>();

		JSONArray entries = bookService.createEntries(origin);
		bookService.getRequestInfo(entries, urls);
		bookService.saveImages(urls, destiny);
		bookService.createPDF(destiny, urls.size(), bookName);

		log.info("Exiting generateFileFromHAR method");
		
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 200);
        map.put("reason", "process complete");
		return map;
	}

}
