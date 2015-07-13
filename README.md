# Open mHealth Sample Data Generator [![Build Status](https://travis-ci.org/openmhealth/sample-data-generator.svg?branch=master)](https://travis-ci.org/openmhealth/sample-data-generator)

When people become familiar with [Open mHealth](http://www.openmhealth.org/), they often ask if there's a data set they can test against, especially 
one containing data matching the data types, time scales, and trends they're interested in. This project is meant to help all
 of those people generate the specific data sets they need.
 
The data generator is a command-line tool that creates Open mHealth [data points](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_data-point) 
for different kinds of measures. It reads a [configuration file](#configuration) to understand what data it should create, and writes that data
out to the console or to a file. 

### Requirements

- If you want to run the generator using Docker, you'll need [Docker](https://docs.docker.com/installation/#installation/) and nothing else.
- If you want to run the generator natively instead, you'll need a Java 8 JRE.
- If you want to modify the code, you'll need a Java 8 SDK.
 
### Installation

To install the generator using Docker, download the data generator image by running 

`docker pull openmhealth/omh-sample-data-generator:latest`

in
a terminal. This will download around 500 MB of Docker images, most of which is the [OpenJDK 8 JRE](https://registry.hub.docker.com/_/java/). 
Alternatively, if you don't want to use Docker, download the `data-generator-x.y.z.jar` JAR file from 
the [latest release](https://github.com/openmhealth/sample-data-generator/releases) on GitHub. 
You'll need a Java JRE to run it.

### Configuration 

To configure the data generator, you'll modify a [YAML](https://en.wikipedia.org/wiki/YAML) configuration file called `application.yml`. If you haven't created
a configuration file yet, the quickest way to get started is to copy the
default configuration file from [here](backend/src/main/resources/application.yml) (it might be easier to copy if you
click the 'Raw' button on that page.)

> You can save the configuration file anywhere, unless you plan to run the generator using Docker and are using Boot2Docker on Mac OS X. 
In that case, save the configuration somewhere under your `/Users` directory. This is due to a [restriction](https://docs.docker.com/userguide/dockervolumes/)
in the directories the Docker daemon has access to when mounting volumes.
 
There's a big section below on the configuration file, but first let's make sure you can run the generator.

### Running

To run the generator using Docker, and assuming you're in the same directory as the `application.yaml` configuration
file, run

``docker run --rm -v `pwd`:/opt/omh-sample-data-generator/mount openmhealth/omh-sample-data-generator:latest``
 
in a terminal. If you're not in the same directory as the configuration file, replace `` `pwd` `` with the directory 
the configuration file is in.

Alternatively, to run the generator natively, navigate to the directory that contains the configuration file and 
run

`java -jar data-generator-x.y.z.jar`  

In either case, you should see output that looks something like

```
Starting Application on machine with PID 15861 (/Users/foo/backend.jar started by foo in /Users/foo)
Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@6e8d6976: startup date [Sat Jul 11 16:09:24 CEST 2015]; root of context hierarchy
Registering beans for JMX exposure on startup
Started Application in 2.056 seconds (JVM running for 2.998)
A total of 0 data point(s) have been written.
Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@6e8d6976: startup date [Sat Jul 11 16:09:24 CEST 2015]; root of context hierarchy
Unregistering JMX-exposed beans on shutdown
```

By default, no data points will be generated. Now that you can run the generator, read on to learn how to configure it
to create some data.

### Configuration, take two

The generator configuration file is written in YAML. If you're new to YAML, just keep in mind that indentation
matters and to use spaces, not tabs. 

The configuration file has keys that divide it into three sections

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
  # meaning: Set the `header.user-id` property of the data point to the username `joe`. 
  header:
    user-id: joe

  # measure generation settings
  measure-generation-requests:
  
  # meaning: A measure generator called `body-weight` should create a year's worth of data
  # for 2014, with data points 24 hours apart, or about 365 data points in total. The 
  # weight should trend from 55kg at the beginning of the year to 60kg at the end of the year.
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
    # the file to write the data points to, defaults to "output.json"
    filename: output.json
    # true if the file should be appended to, false if it should be overwritten, defaults to true
    append: true
```

The `file` key is ignored if the destination is set to `console`.

The `filename` key supports both absolute paths and relative paths. If you're writing to a file and running the
generator in Docker, however, you should use a simple filename in the `filename` key. The file will be written to the
same directory that contains your configuration file.

The generator writes one data point per line, with no separators. When writing to a file, this format makes it easy
to add import the file into a MongoDB collection using the command

- `mongoimport -d some_database -c some_collection --file output.json`


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

The data generator can create data points for different measures. The measures which are supported so far are

* [blood pressure](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_blood-pressure)
* [body weight](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_body-weight)
* [heart rate](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_heart-rate)
* [minutes of moderate activity](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_minutes-moderate-activity)
* [physical activity](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_physical-activity)
* [step count](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_step-count)

To create data points for a specific measure, the data generator uses a *measure generator* that is capable of
generating that type of measure. For example, to create body weight data points, the data generator uses a body
weight measure generator.

A measure generator needs at least one value to generate a measure. For example, to create a body weight measure,
the generator needs a mass value. You may also want the generator to use different values in the different instances it
creates. This is achieved using *trends*.

##### Trends

A trend represents the changing of a value over time. The trend has a *start timestamp* and *start value*, and an *end
timestamp* and *end value*. Using linear interpolation, a generator can compute the value at any time between
the start time and the end time. For example, if the start timestamp is midnight January 1st, 2015 with a weight of 60kg
and the end timestamp is March 1st, 2015 at midnight with a weight of 65kg, the generator will interpolate the weight
in the beginning of February to be 62.5kg.

Although a generator can compute the value at any point along the trend, you need to tell it which points to generate data for.
This is achieved using a *mean inter-point duration*. The duration you specify in the configuration file is fed to
an exponential distribution to pick timestamps along the trend. A mean inter-point duration of `PT6h`, expressed in an
 ISO 8601 duration format, tells the generator to create a data point every 6 hours on average along the trend.

The configuration for the above example looks like this
  
```yaml
measure-generation-requests:
- generator: body-weight
  start-date-time: 2015-01-01T12:00:00Z
  end-date-time: 2015-03-01T12:00:00Z
  mean-inter-point-duration: PT6h
  trends:
    ? weight-in-kg
    : start-value: 60
      end-value: 65
```          

In the example, the name of the measure generator is `body-weight`. This is defined by the measure generator [included](backend/src/main/java/org/openmhealth/data/generator/service/BodyWeightDataPointGenerator.java)
in the data generator. It is possible to create different generators for the same measure, and you would differentiate 
between generators by name. Each measure generator defines the trends it needs, and the `body-weight` measure
 generator uses a trend called `weight-in-kg`. The data generator will warn you if you use unrecognized keys, or fail
 to provide required keys. The full list of included measure generators and their keys is available in
 [Appendix A](#appendix-a-measure-generators).

When executed, this configuration generates about 240 data points (60 days times 4 data points per day), where a
 data point looks like this:
 
```json
{
    "header": {
        "id": "423c4b46-15ac-438b-9734-f8556cb94b6a",
        "creation_date_time": "2015-01-01T15:34:25Z",
        "acquisition_provenance": {
            "source_name": "generator",
            "source_creation_date_time": "2015-01-01T15:33:25Z",
            "modality": "sensed"
        },
        "user_id": "some-user",
        "schema_id": {
            "namespace": "omh",
            "name": "body-weight",
            "version": "1.0"
        }
    },
    "body": {
        "effective_time_frame": {
            "date_time": "2015-01-01T15:33:25Z"
        },
        "body_weight": {
            "unit": "kg",
            "value": 60.01255983207784
        }
    },
    "id": "423c4b46-15ac-438b-9734-f8556cb94b6a"
}
```

A graph of the generated data looks like this:

![Body weight no variance](resources/images/body-weight-no-variance.png?raw=true "Weight trend")

> These graphs are generated using an interactive graph component from the Open mHealth [visualization library](http://www.openmhealth.org/documentation/#/visualize-data/visualization-library).
We are in the process of releasing this code to help you create similar visualizations, and will update this message
 when we do.

A closer view of a few days in January looks like this:

![Body weight no variance (zoomed)](resources/images/body-weight-no-variance-zoomed.png?raw=true "Weight trend (zoomed)")

There are a couple of things to note from this graph. The first is that the data points aren't evenly spaced in time. This is 
caused by the configured inter-point duration being a *mean*. Some points will naturally be nearer each other and some farther
apart as the exponential distribution is sampled to come up with effective time frames. This variability is typically desired as
it more closely represents real world data. The second thing to note is that there's no variability off the trend itself,
i.e. the weights follow the trend perfectly, which is not representative of real world data.

##### Standard deviation

To add variability to the values, you can specify 
a *standard deviation* when configuring a trend. Once the generator interpolates a value at a specific point in time,
it treats that value as the mean of a Gaussian distribution with the specified standard deviation. The random variable
is then sampled to come up with the value to set.
   
If we add a standard deviation to our example configuration as follows:
                     
```yaml
measure-generation-requests:
- generator: body-weight
  start-date-time: 2015-01-01T12:00:00Z
  end-date-time: 2015-03-01T12:00:00Z
  mean-inter-point-duration: PT6h
  trends:
    ? weight-in-kg
    : start-value: 60
      end-value: 65
      standard-deviation: 0.25
```          

A graph of the generated data looks like this:

![Body weight with variance](resources/images/body-weight-with-variance.png?raw=true "Weight trend with variance")

And a closer view of a few days in January now looks like this:

![Body weight with variance (zoomed)](resources/images/body-weight-with-variance-zoomed.png?raw=true "Weight trend with variance (zoomed)")

##### Limits

When using the `standard-deviation` key, some generated values will be far away from the trend, possibly so far away
as to be undesirable or outright invalid, such as negative weights. To mitigate this, the configuration supports
*minimum value* and *maximum value* keys. For example:

```yaml
measure-generation-requests:
- generator: body-weight
  start-date-time: 2015-01-01T12:00:00Z
  end-date-time: 2015-03-01T12:00:00Z
  mean-inter-point-duration: PT6h
  trends:
    ? weight-in-kg
    : start-value: 60
      end-value: 65
      standard-deviation: 0.25
      minimum-value: 59
      maximum-value: 66
```          

All generated values will fall within these bounds. 

##### Night time measure suppression

You may want to suppress the generation of measures that occur at night, typically when modelling self-reported data.
The generator has a *suppress-night-time-measures* key that skips data points whose effective time frames
 falls between 11pm and 5am. Our example configuration would look like
   
```yaml
measure-generation-requests:
- generator: body-weight
  start-date-time: 2015-01-01T12:00:00Z
  end-date-time: 2015-03-01T12:00:00Z
  mean-inter-point-duration: PT6h
  suppress-night-time-measures: true
  trends:
    ? weight-in-kg
    : start-value: 60
      end-value: 65
      standard-deviation: 0.25
      minimum-value: 59
      maximum-value: 66
```
           
Our closer view of those few days in January now looks like this:

![Body weight without night time measures](resources/images/body-weight-without-night-time-measures.png?raw=true "Weight trend without night time measures")

As you can see, the data points no longer have effective time frames at night.

##### Multi-trend measures

Some measure generators build measures by combining multiple trends. To define these trends, simply add more
trend definitions to the `trends` key using the question-mark-colon notation. For example, to create blood pressure measures, 
specify both `systolic-blood-pressure` and `diastolic-blood-pressure` trends to the `blood-pressure` generator as
follows:

```yaml
measure-generation-requests:
- generator: blood-pressure
  start-date-time: 2015-01-01T12:00:00Z
  end-date-time: 2015-03-01T12:00:00Z
  mean-inter-point-duration: PT12h
  suppress-night-time-measures: true
  trends:
    ? systolic-blood-pressure
    : start-value: 110
      end-value: 125
      minimum-value: 100
      maximum-value: 140
      standard-deviation: 3
    ? diastolic-blood-pressure
    : start-value: 70
      end-value: 80
      minimum-value: 60
      maximum-value: 90
      standard-deviation: 3
```      

A graph of the generated data looks like this:

![Blood pressure](resources/images/blood-pressure-with-variance.png?raw=true "Blood pressure")

##### Avoiding repetition

A single configuration file can create different types of measures. Simply concatenate measure generation requests in
the configuration file, and the data generator will generate all configured measures. For example:

```yaml
measure-generation-requests:
- generator: blood-pressure
  start-date-time: 2015-01-01T12:00:00Z
  end-date-time: 2015-03-01T12:00:00Z
  mean-inter-point-duration: PT12h
  suppress-night-time-measures: true
  trends:
    ? systolic-blood-pressure
    : start-value: 110
      end-value: 125
      minimum-value: 100
      maximum-value: 140
      standard-deviation: 3
    ? diastolic-blood-pressure
    : start-value: 70
      end-value: 80
      minimum-value: 60
      maximum-value: 90
      standard-deviation: 3

- generator: body-weight
  start-date-time: 2015-01-01T12:00:00Z
  end-date-time: 2015-03-01T12:00:00Z
  mean-inter-point-duration: PT6h
  suppress-night-time-measures: true
  trends:
    ? weight-in-kg
    : start-value: 55
      end-value: 60
      minimum-value: 50
      maximum-value: 65
      standard-deviation: 0.1
```      
 
In the above example, some measure generator settings are repeated. To avoid this repetition,
you can also place common settings under the `data` key, and only override them per generator as necessary. The above
settings would then look like this.

```yaml                    
data:
  start-date-time: 2015-01-01T12:00:00Z        # defaults to January 1st, 2014 at noon UTC
  end-date-time: 2015-03-01T12:00:00Z          # defaults to January 1st, 2015 at noon UTC
  suppress-night-time-measures: true           # defaults to false

  measure-generation-requests:
  - generator: blood-pressure
    mean-inter-point-duration: PT12h           # defaults to PT24h
    trends:
      ? systolic-blood-pressure
      : start-value: 110
        end-value: 125
        minimum-value: 100
        maximum-value: 140
        standard-deviation: 3
      ? diastolic-blood-pressure
      : start-value: 70
        end-value: 80
        minimum-value: 60
        maximum-value: 90
        standard-deviation: 3

  - generator: body-weight
    mean-inter-point-duration: PT6h
    trends:
      ? weight-in-kg
      : start-value: 55
        end-value: 60
        minimum-value: 50
        maximum-value: 65
        standard-deviation: 0.1
```

If the generator settings are omitted, the defaults in the comments take effect.

You can add as many measure generator requests in a configuration file as you want, including
requests for the same measure generator. This lets you assemble data sets that include more complex trends.

### Contributing

To contribute to this repository

1. Open an [issue](https://github.com/openmhealth/sample-data-generator/issues) to let us know what you're going to work on.
  1. This lets us give you feedback early and lets us put you in touch with people who can help.
2. Fork this repository.
3. Create your feature branch from the `develop` branch.
4. Commit and push your changes to your fork.
5. Create a pull request.
 
 
### Appendix A. Measure generators

The following table shows the currently included measure generators and their trend keys. For
 more information, take a look at the code or ask us a [question](https://github.com/openmhealth/sample-data-generator/issues).
The default configuration file also includes a sample configuration for each measure generator. 

|name|Open mHealth measure schema|supported trend keys|required trend keys|
|----|---------------------------|--------------------|-------------------| 
|[blood-pressure](backend/src/main/java/org/openmhealth/data/generator/service/BloodPressureDataPointGenerator.java)|[omh:blood-pressure](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_blood-pressure)|systolic-blood-pressure, diastolic-blood-pressure|same|
|[body-weight](backend/src/main/java/org/openmhealth/data/generator/service/BodyWeightDataPointGenerator.java)|[omh:body-weight](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_body-weight)|weight-in-kg|same|
|[heart-rate](backend/src/main/java/org/openmhealth/data/generator/service/HeartRateDataPointGenerator.java)|[omh:heart-rate](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_heart-rate)|rate-in-bpm|same|
|[minutes-moderate-activity](backend/src/main/java/org/openmhealth/data/generator/service/MinutesModerateActivityDataPointGenerator.java)|[omh:minutes-moderate-activity](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_minutes-moderate-activity)|minutes|same|
|[physical-activity](backend/src/main/java/org/openmhealth/data/generator/service/PhysicalActivityDataPointGenerator.java)|[omh:physical-activity](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_physical-activity)|duration-in-seconds, distance-in-meters|duration-in-seconds|
|[step-count](backend/src/main/java/org/openmhealth/data/generator/service/StepCountDataPointGenerator.java)|[omh:step-count](http://www.openmhealth.org/documentation/#/schema-docs/schema-library/schemas/omh_step-count)|steps-per-minute, duration-in-seconds|same|

    
Measure generators are short and quite easy to write. Feel free to [contribute](#contributing) others.
