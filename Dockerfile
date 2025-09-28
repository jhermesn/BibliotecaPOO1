FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY src/ src/
COPY lib/ lib/
COPY resources/ resources/

RUN set -eux; \
    mkdir -p bin; \
    find src -name "*.java" > sources.txt; \
    javac -d bin -cp "lib/mysql-connector-j-9.4.0.jar" @sources.txt; \
    echo "Main-Class: application.Main" > manifest.txt; \
    jar cvfm Biblioteca.jar manifest.txt -C bin .

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

RUN set -eux; \
    apt-get update; \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
      libxext6 libxrender1 libxtst6 libxi6 libxrandr2 libcups2 \
      fontconfig fonts-dejavu ca-certificates \
      xvfb x11vnc novnc websockify; \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /app/Biblioteca.jar /app/Biblioteca.jar
COPY lib/ /app/lib/
COPY resources/ /app/resources/

COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

EXPOSE 6080

CMD ["/app/start.sh"]
