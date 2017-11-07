package com.twbarber.amazon

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.model.PublishRequest

typealias Config = ClasspathPropertiesFileCredentialsProvider

object SnsService {

    fun send(region: Region, topicArn: String, message: String) {
        val client: AmazonSNSClient = AmazonSNSClient(Config()).withRegion(region)
        val publishRequest = PublishRequest(topicArn, message)
        val publishResult = client.publish(publishRequest)
        println("MessageId - " + publishResult.messageId)
    }

    private fun AmazonSNSClient.withRegion(r: Region) : AmazonSNSClient { this.setRegion(r); return this }

}