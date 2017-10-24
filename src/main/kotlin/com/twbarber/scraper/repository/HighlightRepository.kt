package com.twbarber.scraper.repository

import sun.security.krb5.internal.Krb5.getErrorMessage
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.dynamodbv2.model.ListTablesResult
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB



class HighlightRepository {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val ddb = AmazonDynamoDBClientBuilder.defaultClient()

            var more_tables = true
            while (more_tables) {
                var last_name: String? = null
                try {
                    var table_list: ListTablesResult? = null
                    if (last_name == null) {
                        table_list = ddb.listTables()
                    }

                    val table_names = table_list!!.tableNames

                    if (table_names.size > 0) {
                        for (cur_name in table_names) {
                            System.out.format("* %s\n", cur_name)
                        }
                    } else {
                        println("No tables found!")
                        System.exit(0)
                    }

                    last_name = table_list.lastEvaluatedTableName
                    if (last_name == null) {
                        more_tables = false
                    }
                } catch (e: AmazonServiceException) {
                    System.err.println(e.errorMessage)
                    System.exit(1)
                }

            }
            println("\nDone!")
        }
    }



}