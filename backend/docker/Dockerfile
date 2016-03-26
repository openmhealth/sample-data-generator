FROM java:openjdk-8-jre
MAINTAINER Emerson Farrugia <emerson@openmhealth.org>

ENV BASE_DIR /opt/omh-sample-data-generator
ADD data-generator.jar $BASE_DIR/

# the configuration file will be read from a directory called 'mount', and output will be written to it
RUN mkdir -p $BASE_DIR/mount
WORKDIR $BASE_DIR/mount

CMD /usr/bin/java -jar $BASE_DIR/data-generator.jar
