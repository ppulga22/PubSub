# PubSub
This app allow clients to subscribe topics, through TCP/UDP and they'll receive the new data added to the topic.

To start the app you should run the DriverClass, then run TCP and/or UDP clients. You should use this commands on console: 
-> publisher : {"type": pub, "topic": music, "payload": {"name": Nirvana}}
-> subscriber : {"type": sub, "topic": music}
This is an example, choose your topic and payload.
