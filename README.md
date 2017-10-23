# Big Play Feed Scraper

Polls the NFL.com Big Play Feed and posts updates to your Slack channel of choice.

**Disclaimer**: I am in no way associated with the NFL, and claim no ownership to the videos, images, and other forms of media
retrieved by this application.

## Usage

1. Clone this repository
2. Create your [Slack Incoming Webhook](https://my.slack.com/services/new/incoming-webhook/)
3. Create a [Configuration File](#configuration) at `src/main/resources/application.properties`
4. Build the project using `./gradlew jar`
5. Launch your jar file using `java -jar big-play-scraper-0.0.1.jar`

## How it Works

While the [application is running](#usage), all future updates to the NFL Big Play Feed will be sent to the specified channel
via your Incoming Webhook. **Existing entries in the feed are ignored**, in order to prevent message spam when starting
and restarting the application.


## Configuration

A [Slack Incoming Webhook URL](https://my.slack.com/services/new/incoming-webhook/), destination channel, and polling wait time are 
all required configuration values.

You'll need to specify an `application.properties` file in `src/main/resources` in order
for this to run correctly. An example can be found [here](https://github.com/twbarber/big-play-scraper/blob/master/src/main/resources/application.properties.example).

Alternatively, create a new `application.properties` file and paste in the following properties
with your desired configuration values. 

### Properties

`slackWebhookUrl`: Your Slack incoming Webhook.  
`slackoChannel`: Channel you want highlights to post to.  
`refreshInterval`: How often to poll the feed, in milliseconds.

### Sample

```
settings.slackWebhookUrl=https://hooks.slack.com/services/.../.../...
settings.slackChannel=general
settings.refreshInterval=15000
```


