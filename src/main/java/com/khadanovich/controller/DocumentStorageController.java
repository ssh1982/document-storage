package com.khadanovich.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khadanovich.domain.DocumentMetaData;
import com.khadanovich.repository.DocumentMetaDataRepository;

import org.apache.commons.lang3.RandomStringUtils;

@RestController
public class DocumentStorageController {
	private Map<String, byte[]> storage = new HashMap<>();

	@Autowired
	private DocumentMetaDataRepository documentMetaDataRepository;

	@PostMapping(value = "/storage/documents", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<String> create(HttpServletRequest request) throws IOException {
		String docId = RandomStringUtils.randomAlphanumeric(20);
		byte[] content = IOUtils.toByteArray(request.getInputStream());
		storage.put(docId, content);
		DocumentMetaData documentMetaData = new DocumentMetaData();
		documentMetaData.setDocId(docId);
		documentMetaData.setFileName(docId);
		documentMetaData.setSize(content.length);
		documentMetaDataRepository.save(documentMetaData);
		return new ResponseEntity<String>(docId, HttpStatus.CREATED);
	}

	@GetMapping(value = "/storage/documents/{docId}", produces = MediaType.ALL_VALUE)
	public ResponseEntity<byte[]> read(@PathVariable String docId) {
		byte[] document = storage.get(docId);
		if (document != null) {
			return new ResponseEntity<byte[]>(document, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}

	}

	@PutMapping(value = "/storage/documents/{docId}", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<String> update(HttpServletRequest request, @PathVariable String docId) throws IOException {
		if (storage.get(docId) != null) {
			byte[] content = IOUtils.toByteArray(request.getInputStream());
			storage.put(docId, content);
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(value = "/storage/documents/{docId}")
	public ResponseEntity<String> delete(@PathVariable String docId) throws IOException {
		if (storage.get(docId) != null) {
			storage.remove(docId);
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}

}
