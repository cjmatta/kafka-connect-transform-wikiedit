package com.github.cjmatta.kafka.connect.transform.wikiedit;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

/**
 * Created by chris on 5/1/17.
 */
public class WikiEditTransformationConfig extends AbstractConfig {
    public static final String FIELD_MESSAGE_CONF = "field.message";
    static final String FIELD_MESSAGE_DOC = "The field in the source record that contains the message to parse";
    static final String FIELD_MESSAGE_DEFAULT = "message";

    public static final String SAVE_UNPARSEABLE_MESSAGES = "save.unparseable.messages";
    static final String SAVE_UNPARSEABLE_MESSAGES_DOC = "Weather or not to ignore messages that don't parse, " +
        "if this is set, messages will be sent to `dead.letter.topic`";
    static final Boolean SAVE_UNPARSEABLE_MESSAGES_DEFAULT= false;

    public static final String DEAD_LETTER_TOPIC_CONF = "dead.letter.topic";
    static final String DEAD_LETTER_TOPIC_DOC = "The topic to use for messages that fail parsing";
    static final String DEAD_LETTER_TOPIC_DEFAULT = "wikipedia.failed";

    public final String fieldMessage;
    public final String deadLetterTopic;
    public final Boolean saveUnparseableMessages;

    public WikiEditTransformationConfig(Map<String, ?> parsedConfig) {
        super(config(), parsedConfig);
        this.fieldMessage = getString(FIELD_MESSAGE_CONF);
        this.saveUnparseableMessages = getBoolean(SAVE_UNPARSEABLE_MESSAGES);
        this.deadLetterTopic = getString(DEAD_LETTER_TOPIC_CONF);
    }

    static ConfigDef config() {
        return new ConfigDef()
            .define(FIELD_MESSAGE_CONF, ConfigDef.Type.STRING, FIELD_MESSAGE_DEFAULT, ConfigDef.Importance.HIGH, FIELD_MESSAGE_DOC)
            .define(SAVE_UNPARSEABLE_MESSAGES, ConfigDef.Type.BOOLEAN, SAVE_UNPARSEABLE_MESSAGES_DEFAULT, ConfigDef.Importance.LOW, SAVE_UNPARSEABLE_MESSAGES_DOC)
            .define(DEAD_LETTER_TOPIC_CONF, ConfigDef.Type.STRING, DEAD_LETTER_TOPIC_DEFAULT, ConfigDef.Importance.LOW, DEAD_LETTER_TOPIC_DOC);
    }
}
