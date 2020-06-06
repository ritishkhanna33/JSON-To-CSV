package com.json_to_csv.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.json_to_csv.api.manager.JsonToCsvManager;

@RestController
@RequestMapping("Json/ToCSV")
public class JsonToCsvController {

	@Autowired
	private JsonToCsvManager manager;
	
	@GetMapping("download")
	public ResponseEntity<byte[]> downloadCsvData() throws Exception{
		String filename = String.valueOf(System.currentTimeMillis()) + ".json";
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.body(manager.downloadJson());
	}
	
}
