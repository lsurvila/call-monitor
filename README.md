<!-- Generator: Widdershins v4.0.1 -->

<h1 id="call-monitor">Call Monitor v1.0.0</h1>

<p>Android application that allows call monitoring (API 23+). To start monitoring toggle switch in top right. 
Application requires be set as default phone application and requires read-contacts permission.
Upon toggling monitor service on user will be asked to approve these permissions.</p>

<p>After phone role permission is granted application acts as default phone app even when monitoring service is switched off. In any case to answer/reject phone call use Reject and Answer buttons near service switch.
To dial a number user can use pre-installed phone app as Call Monitor does not support phone dialer/keypad. After initiating a call user will be redirected to Call Monitor.
User can restore default phone app in default app settings, Call Monitor also provides shortcut to settings (3 dots menu in top right)</p>

<p>Call Monitor service requires active Wifi connection. If for some reason call monitor service cannot be started error will be
printed out in the app. If service successfully starts it will launch http server, which can be accessed from the same Wifi network.
Address and port will be printed out in the application</p>

<p>Call Monitor server  API description is provided below:</p>

## GET /

```shell
# You can also use wget
curl -X GET / \
  -H 'Accept: application/json'

```

*Show available services*

> 200 Response

```json
{
  "start": "2018-02-02T23:00:00+00:00",
  "services": [
    {
      "name": "status",
      "uri": "http://192.168.1.100:12345/status"
    },
    {
      "name": "log",
      "uri": "http://192.168.1.100:12345/log"
    }
  ]
}
```

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Show available services|[Services](#schemaservices)|

## GET /status

```shell
# You can also use wget
curl -X GET /status \
  -H 'Accept: application/json'

```

*Shows data on the current call status*

> 200 Response

```json
{
  "ongoing": true,
  "number": "+12025550108",
  "name": "John Doe"
}
```

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Shows data on the current call status|[OngoingCall](#schemaongoingcall)|

## GET /log

```shell
# You can also use wget
curl -X GET /log \
  -H 'Accept: application/json'

```

*Shows data on previous calls since server was started*

> 200 Response

```json
[
  {
    "beginning": "2018-05-02T23:00:00+00:00",
    "duration": "498",
    "number": "+12025550203",
    "name": "Jane Doe",
    "timesQueried": 5
  },
  {
    "beginning": "2018-05-01T09:00:00+00:00",
    "duration": "721",
    "number": "+12025550108",
    "name": "John Doe",
    "timesQueried": 0
  }
]
```

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Shows data on previous calls since server was started|[LoggedCall](#schemaloggedcall)|

# Schemas

<h2 id="tocS_Services">Services</h2>
<!-- backwards compatibility -->
<a id="schemaservices"></a>
<a id="schema_Services"></a>
<a id="tocSservices"></a>
<a id="tocsservices"></a>

```json
{
  "start": "string",
  "services": [
    {
      "name": "string",
      "uri": "string"
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|start|string|true|none|Time server was started|
|services|[[Service](#schemaservice)]|true|none|List of available services|

<h2 id="tocS_Service">Service</h2>
<!-- backwards compatibility -->
<a id="schemaservice"></a>
<a id="schema_Service"></a>
<a id="tocSservice"></a>
<a id="tocsservice"></a>

```json
{
  "name": "string",
  "uri": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|name|string|true|none|Name of the service|
|uri|string|true|none|Address where service can be reached|

<h2 id="tocS_OngoingCall">OngoingCall</h2>
<!-- backwards compatibility -->
<a id="schemaongoingcall"></a>
<a id="schema_OngoingCall"></a>
<a id="tocSongoingcall"></a>
<a id="tocsongoingcall"></a>

```json
{
  "ongoing": true,
  "number": "string",
  "name": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|ongoing|boolean|true|none|True if call is active, false if ringing, connecting, disconnected, etc|
|number|string|false|none|Phone number user is connected to, null if call is not ongoing/active|
|name|string|false|none|Name of the contact user is conncected if number is in users' contact list and call is ongoing/active, null otherwise|

<h2 id="tocS_LoggedCall">LoggedCall</h2>
<!-- backwards compatibility -->
<a id="schemaloggedcall"></a>
<a id="schema_LoggedCall"></a>
<a id="tocSloggedcall"></a>
<a id="tocsloggedcall"></a>

```json
{
  "beginning": "string",
  "duration": "string",
  "number": "string",
  "name": "string",
  "timesQueried": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|beginning|string|true|none|Time when call got connected|
|duration|string|true|none|Duration of the call in seconds|
|number|string|true|none|Number to which call was connected to|
|name|string|false|none|Name of the contact user is conncected if number is in users' contact list, null otherwise|
|timesQueried|integer|true|none|How many times call (when active) was monitored with /status endpoint|

