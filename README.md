# Backend Developer Challenge

The challenge proposed here is to build a system which acts as a socket server, reading events from an *event source* and forwarding them when appropriate to *user clients*.

Clients will connect through *TCP* and use the simple protocol decribed in the section below. There will be 2 types of clients connecting to your server: -

**One** *event source: -* It will send you a stream of events which may or may not require clients to be notified.  
**Many** *user clients: -* Each one representing a specific user, these wait for notification for events which would be relevant to the user they represent.

*Please try to avoid any unrequired code libraries for the task, unless specifically required*


# Getting started

To compile maven project

```
$ mvn clean install
```


# Starting EventServer

### Parameters:
**port:** numeric
    
```
$ java -jar Server/target/server.jar 8000
```

# Starting UserClient

### Parameters:
**host:** string
**port:** numeric
**username** string
    
```
$ java -jar UserClient/target/userClient.jar localhost 8000 johnDoe
```

# Starting EventClient

### Parameters:
**host:** string
**port:** numeric
    
```
$ java -jar EventClient/target/eventClient.jar localhost 8000
```

#### Sending Message To User

```
Enter Event User Name :
johnDoe
Enter Event Content :
foo
```

