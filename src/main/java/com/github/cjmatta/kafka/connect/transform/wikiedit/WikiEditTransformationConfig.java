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

    public final String fieldMessage;

    public WikiEditTransformationConfig(Map<String, ?> parsedConfig) {
        super(config(), parsedConfig);
        this.fieldMessage = getString(FIELD_MESSAGE_CONF);
    }

    static ConfigDef config() {
        return new ConfigDef()
            .define(FIELD_MESSAGE_CONF, ConfigDef.Type.STRING, FIELD_MESSAGE_DEFAULT, ConfigDef.Importance.HIGH, FIELD_MESSAGE_DOC);
    }
}
