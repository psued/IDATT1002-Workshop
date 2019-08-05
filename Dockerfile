FROM jetty:latest
COPY ./target/chat.war /var/lib/jetty/webapps/root.war