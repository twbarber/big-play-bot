package com.twbarber.scraper.repository;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

public class Put
{
    public static void main(String[] args)
    {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    PutItem <table> <name> [field=value ...]\n\n" +
                "Where:\n" +
                "    table    - the table to put the item in.\n" +
                "    name     - a name to add to the table. If the name already\n" +
                "               exists, its entry will be updated.\n" +
                "Additional fields can be added by appending them to the end of the\n" +
                "input.\n\n" +
                "Example:\n" +
                "    PutItem Cellists Pau Language=ca Born=1876\n";

        String table_name = "highlights";
        Integer name = 41256;

        HashMap<String , AttributeValue> item_values = new HashMap<String, AttributeValue>();

        item_values.put("id", new AttributeValue().withN(name.toString()));

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

        try {
            ddb.putItem(table_name, item_values);
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.\n", table_name);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            System.exit(1);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }
}