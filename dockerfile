FROM hhhu04/ubuntu:0.0.1
RUN mkdir /account
COPY /build/libs/Account-0.0.1.jar /account
COPY start.sh ./start.sh
EXPOSE 8080
ENTRYPOINT ["./start.sh"]
