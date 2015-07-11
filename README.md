# Open mHealth Sample Data Generator

When people become familiar with [Open mHealth](http://www.openmhealth.org/), they often ask if there's a data set they can test against, especially 
one containing data matching the data types, time scales, and trends they're interested in. This project is meant to help all
 of those people generate the specific data sets they need.
 
The data generator is a command-line tool that creates Open mHealth [data points](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_data-point) 
for different kinds of measures. It reads a [configuration file](#configuration) to understand what data it should create, and writes that data
out to the console or to a file. 

### Requirements

- If you want to run the generator using Docker, you'll need [Docker](https://docs.docker.com/installation/#installation/) and nothing else.
- If you want to run the code directly instead, you'll need a Java JRE.
- If you want to modify the code, you'll need a Java SDK.
 
### Installation

To install the generator using Docker, download the data generator image by running `docker pull openmhealth/omh-sample-data-generator:latest`.
This will download around 500 MB of Docker images, most of which is the [OpenJDK 8 JRE](https://registry.hub.docker.com/_/java/).

Alternatively, if you don't want to use Docker, download the `data-generator-x.y.z.jar` JAR file from 
the [latest release](https://github.com/openmhealth/sample-data-generator/releases) on GitHub. You'll need a Java JRE to run it.

### Configuration 

To configure the data generator, you'll modify a configuration file called `application.yml`. If you haven't created
a configuration file yet, the quickest way to get started is to copy the
default configuration file from [here](backend/src/main/resources/application.yml) (it might be easier to copy if you
click the 'Raw' button on that page.)

The generator configuration file is written in [YAML](https://en.wikipedia.org/wiki/YAML) and is split into three main
sections

- an [output settings](#output_settings) section, which controls what the generator does with the data points it creates 
- a [header settings](#header_settings) section, which sets certain header fields in the generated data points  
- a [measure generation settings](#measure_generation_settings) section, which specifies the data to generate 

A simple commented configuration file might looks like this

```yaml
# output settings
# meaning: Write any generated data points to the console.
output:
  destination: console

data:
  # header settings
  # meaning: Set the `header.user-id` property of the data point to the username `some-user`. 
  header:
    user-id: some-user

  # measure generation settings
  measure-generation-requests:
  
  # meaning: A generator called `body-weight` should create a year's worth of data for 2014, 
  # with data points 24 hours apart, or about 365 data points in total. The weight should
  # trend from 55kg at the beginning of the year to 60kg at the end of the year.
  - generator: body-weight
    start-date-time: 2014-01-01T12:00:00Z
    end-date-time: 2015-01-01T12:00:00Z
    mean-inter-point-duration: PT24h
    trends:
      ? weight-in-kg
      : start-value: 55
        end-value: 60
```

This example is significantly shorter than the default configuration file because defaults are being used. In general, 
if any key in the default configuration file has a "defaults to" comment, that key can be omitted, and the generator will
set the value of that key to the stated default.

The following paragraphs explain what each group of settings does. The default configuration file is also heavily
documented, but we recommend you first read the following sections before diving in.

#### Output settings

The `output` key controls what the generator does with the data points it creates. The complete
settings are as follows

```yaml
output:
  # whether to write data points to the "console" or to a "file", defaults to "console"
  destination: console
  file:
    # the file to write the data points to
    filename: some-filename.json
    # true if the file should be appended to, false if it should be overwritten, defaults to true
    append: true
```

The `file` key is ignored if the destination is set to `console`.

The generator writes one data point per line, with no separators. When writing to a file, this format makes it easy
to add import the file into a MongoDB collection using the command

- `mongoimport -d some_database -c some_collection --file some-filename.json`


#### Data point header settings

The `data.header` key gives you a way to tune some of the operational data in the data points. The complete 
settings are as follows

```yaml
data:
  header:
    # the user to associate the data points with, defaults to "some-user"
    user-id: some-user

    acquisition-provenance:
      # the name of the source of the data points, defaults to "generator"
      source-name: generator
```

At this point, only the user and source name settings are available. We'll add more settings based on demand.

#### Measure generation settings

 
