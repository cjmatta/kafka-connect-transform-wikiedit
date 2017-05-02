package com.github.cjmatta.kafka.connect.transform.wikiedit; /**
 * Copyright © 2017 Christopher Matta (chris.matta@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WikiEditTransformation<R extends ConnectRecord<R>> implements Transformation<R> {
    private static final Logger log = LoggerFactory.getLogger(WikiEditTransformation.class);
    WikiEditTransformationConfig config;

    @Override
    public ConnectRecord apply(ConnectRecord record) {
        if (null == record.valueSchema() || Schema.Type.STRUCT != record.valueSchema().type()) {
            log.trace("record.valueSchema() is null or record.valueSchema() is not a struct.");
            return record;
        }

        Struct inputRecord = (Struct) record.value();

// Try parsing this message, if it fails, send it to the config.deadLetterTopic
        try {

            Struct returnStruct = this.parseMessage(inputRecord.getString(this.config.fieldMessage));
            returnStruct.put(Constants.CREATEDAT, inputRecord.get("createdat"));
            returnStruct.put(Constants.CHANNEL, inputRecord.get("channel"));
            return record.newRecord(
                record.topic(),
                record.kafkaPartition(),
                record.keySchema(),
                record.key(),
                returnStruct.schema(),
                returnStruct,
                record.timestamp()
            );
//            This message failed parsing
        } catch (IllegalStateException e) {

            if (this.config.saveUnparseableMessages) {
                return record.newRecord(
                    config.deadLetterTopic,
                    null,
                    record.keySchema(),
                    record.key(),
                    record.valueSchema(),
                    record.value(),
                    record.timestamp()
                );
            } else {
                return null;
            }
        }

    }

    private Struct parseMessage (String message) throws IllegalStateException {
        String pattern ="\\[\\[(.*)\\]\\]\\s(.*)\\s(.*)\\s\\*\\s(.*)\\s\\*\\s\\(([\\+|\\-].\\d*)\\)\\s?(.*)?$";
        Pattern wikiPattern = Pattern.compile(pattern);
        Matcher matcher = wikiPattern.matcher(message);

        matcher.matches();

        Struct outputRecord = new Struct(Constants.SCHEMA);
        outputRecord.put(Constants.WIKIPAGE, matcher.group(1));
        outputRecord.put(Constants.DIFFURL, matcher.group(3));
        outputRecord.put(Constants.USERNAME, matcher.group(4));
        outputRecord.put(Constants.BYTECHANGE, Integer.parseInt(matcher.group(5)));
        outputRecord.put(Constants.COMMITMESSAGE, matcher.group(6));

//        Set Flags
        outputRecord.put(Constants.ISNEW, matcher.group(2).contains("N"));
        outputRecord.put(Constants.ISMINOR, matcher.group(2).contains("M"));
        outputRecord.put(Constants.ISUNPATROLLED, matcher.group(2).contains("!"));
        outputRecord.put(Constants.ISBOT, matcher.group(2).contains("B"));

        return outputRecord;

    }

    @Override
    public ConfigDef config() {
        return WikiEditTransformationConfig.config();
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {
        this.config = new WikiEditTransformationConfig(map);
    }
}
