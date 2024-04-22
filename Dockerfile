    FROM ubuntu:20.04
    RUN apt update \
        && apt install -y python3 fortune \
        && cd /usr/bin \
        && ln -s python3 python
    RUN /usr/games/fortune > /mnt/greeting-while-building.txt
    ADD ./data /mnt/data
    EXPOSE 80
    CMD ["python" , "-m" , "http.server" , "-d" , "/mnt/" , "80"]