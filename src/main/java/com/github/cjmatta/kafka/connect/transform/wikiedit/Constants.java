package com.github.cjmatta.kafka.connect.transform.wikiedit;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Timestamp;

/**
 * Created by chris on 4/28/17.
 */
public class Constants {
    public static final String CREATEDAT = "createdat";
    public static final String WIKIPAGE = "wikipage";
    public static final String CHANNEL = "channel";
    public static final String ISNEW = "isnew";
    public static final String ISMINOR = "isminor";
    public static final String ISUNPATROLLED = "isunpatrolled";
    public static final String ISBOT = "isbot";
    public static final String DIFFURL = "diffurl";
    public static final String USERNAME = "username";
    public static final String BYTECHANGE = "bytechange";
    public static final String COMMITMESSAGE = "commitmessage";

    public static final Schema SCHEMA = SchemaBuilder.struct()
        .name("com.github.cjmatta.kafka.connect.transform.wikiedit.WikiEdit")
        .doc("The parsed Wikipedia Edit")
        .field(
            CREATEDAT,
            Timestamp.builder().doc("The timestamp of the edit").build()
        )
        .field(
            WIKIPAGE,
            SchemaBuilder.string().doc("The page that was edited").build()
        )
        .field(
            CHANNEL,
            SchemaBuilder.string().doc("The source channel the edit came through").build()
        )
        .field(
            USERNAME,
            SchemaBuilder.string().doc("Username of editor").build()
        )
        .field(
            COMMITMESSAGE,
            SchemaBuilder.string().doc("Commit message for edit.").build()
        )
        .field(
            BYTECHANGE,
            SchemaBuilder.int32().doc("number of bytes added or removed with this edit.").build()
        )
        .field(
            DIFFURL,
            SchemaBuilder.string().doc("The URL showing the edit diff.").build()
        )
        .field(
            ISNEW,
            SchemaBuilder.bool().defaultValue(false).optional().doc("Is this a new page?").build()
        )
        .field(
            ISMINOR,
            SchemaBuilder.bool().defaultValue(false).optional().doc("Is this edit minor?").build()
        )
        .field(
            ISBOT,
            SchemaBuilder.bool().defaultValue(false).optional().doc("Is this an edit by a bot?").build()
        )
        .field(
            ISUNPATROLLED,
            SchemaBuilder.bool().defaultValue(false).optional().doc("Is this edit unpatrolled?").build()
        ).build();
}
