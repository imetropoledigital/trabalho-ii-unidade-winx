package com.imd.mongodbproject.service;

import com.imd.mongodbproject.model.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public long getNextSequenceId(String collectionName) {
        String seqName = collectionName + "_sequence";

        Query query = new Query(where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)
                .upsert(true);

        Sequence sequence = mongoTemplate.findAndModify(query, update, options, Sequence.class);
        return (sequence != null) ? sequence.getSeq() : 1;
    }
}

