package com.imd.mongodbproject.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class DocumentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public Document create(String collectionName, Document doc) {
        long nextId = sequenceGeneratorService.getNextSequenceId(collectionName);
        doc.put("_id", nextId);

        return mongoTemplate.save(doc, collectionName);
    }

    public List<Document> findAll(
            String collectionName,
            Document queryDoc,
            Document fieldsDoc,
            Integer page,
            Integer size
    ) {
        if (queryDoc == null) queryDoc = new Document();
        if (fieldsDoc == null) fieldsDoc = new Document();

        BasicQuery basicQuery = new BasicQuery(queryDoc, fieldsDoc);

        if (page != null && size != null && page >= 0 && size > 0) {
            basicQuery.skip((long) page * size);
            basicQuery.limit(size);
        }

        return mongoTemplate.find(basicQuery, Document.class, collectionName);
    }

    public Document findById(String collectionName, long id) {
        Query query = new Query(where("_id").is(id));
        return mongoTemplate.findOne(query, Document.class, collectionName);
    }

    public Document update(String collectionName, long id, Document updateDoc) {
        Document existing = findById(collectionName, id);
        if (existing == null) {
            return null;
        }

        existing.putAll(updateDoc);

        existing.put("_id", id);

        return mongoTemplate.save(existing, collectionName);
    }
}

