package com.imd.mongodbproject.controller;

import com.imd.mongodbproject.service.DocumentService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{collection}")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<?> createDocument(
            @PathVariable String collection,
            @RequestBody Document doc
    ) {
        try {
            Document saved = documentService.create(collection, doc);
            return ResponseEntity.status(201).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating document: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDocuments(
            @PathVariable String collection,
            @RequestParam(value = "query", required = false) String queryParam,
            @RequestParam(value = "fields", required = false) String fieldsParam,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        try {
            Document queryDoc = new Document();
            if (queryParam != null && !queryParam.isEmpty()) {
                queryDoc = Document.parse(queryParam);
            }

            if (fieldsParam == null || fieldsParam.trim().isEmpty()) {
                List<Document> all = documentService.findAll(collection, queryDoc, null, page, size);
                return ResponseEntity.ok(all);
            }

            String[] fieldsArray = fieldsParam.split(",");

            boolean isInclude = false;
            boolean isExclude = false;

            for (String fieldItem : fieldsArray) {
                fieldItem = fieldItem.trim();
                if (fieldItem.startsWith("-")) {
                    isExclude = true;
                } else {
                    isInclude = true;
                }
            }

            if (isInclude && isExclude) {
                return ResponseEntity.badRequest().body("It is not allowed to mix inclusion and exclusion of fields in the same request (except _id).");
            }

            Document fieldsDoc = new Document();

            if (isInclude) {
                for (String fieldItem : fieldsArray) {
                    fieldItem = fieldItem.trim();
                    if (fieldItem.startsWith("-")) {
                        return ResponseEntity.badRequest().body("Mixing of include/exclude not supported.");
                    }
                    fieldsDoc.put(fieldItem, 1);
                }
                if (!fieldsDoc.containsKey("_id")) {
                    fieldsDoc.put("_id", 0);
                }

            } else {
                for (String fieldItem : fieldsArray) {
                    fieldItem = fieldItem.trim();
                    if (!fieldItem.startsWith("-")) {
                        return ResponseEntity.badRequest().body("Mixing of include/exclude not supported.");
                    }
                    String fieldName = fieldItem.substring(1);
                    fieldsDoc.put(fieldName, 0);
                }
            }

            List<Document> documents = documentService.findAll(collection, queryDoc, fieldsDoc, page, size);

            return ResponseEntity.ok(documents);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error listing documents: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(
            @PathVariable String collection,
            @PathVariable String id
    ) {
        try {
            long numericId = Long.parseLong(id);
            Document doc = documentService.findById(collection, numericId);
            if (doc == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(doc);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching document: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(
            @PathVariable String collection,
            @PathVariable String id,
            @RequestBody Document updateDoc
    ) {
        try {
            long numericId = Long.parseLong(id);
            Document updated = documentService.update(collection, numericId, updateDoc);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating document: " + e.getMessage());
        }
    }
}

