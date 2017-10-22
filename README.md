# Big Play Feed Scraper

Polls the NFL.com Big Play Feed and posts updates to your Slack channel of choice.

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
