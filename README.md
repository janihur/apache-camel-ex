Apache Camel 3 Code Examples
============================

Currently used Camel version: 3.18.5

All examples might not compile when the Camel version have been changed.

How to compile and run the examples (unless otherwise noted):

```
mvn compile exec:java
```

`001-standalone`
----------------

[Running Camel Standalone](https://camel.apache.org/manual/latest/faq/running-camel-standalone.html) Java `main` example.

`002-camel-main`
----------------

How to use [Main](https://camel.apache.org/components/latest/others/main.html) module to run Camel stand-alone. Also demonstrates [Quartz](https://camel.apache.org/components/latest/quartz-component.html) scheduler component and how to call a [bean](https://camel.apache.org/components/latest/languages/bean-language.html).

`003-openapi`
-------------

How to compile [OpenAPI specification](https://swagger.io/specification/) to [REST DSL with Java](https://camel.apache.org/manual/latest/rest-dsl.html) by [camel-restdsl-swagger-plugin](https://github.com/apache/camel/blob/master/tooling/maven/camel-restdsl-swagger-plugin/src/main/docs/camel-restdsl-swagger-plugin.adoc). Runs with [Jetty](https://camel.apache.org/components/latest/jetty-component.html) in port 8081.

Try it:

```
$ curl http://0.0.0.0:8081/api101/hello
{hello: "Hello, World!"}
```

`004-timer`
-----------

[Timer](https://camel.apache.org/components/latest/timer-component.html) scheduler component example where message body is set by [Bean](https://camel.apache.org/components/latest/bean-component.html) component.

`005-xslt`
----------

How to convert XML messages with [XSLT](https://camel.apache.org/components/latest/xslt-component.html) component and how to unit test the transformation with [XMLUnit](https://www.xmlunit.org/) [XSLT support](https://github.com/xmlunit/user-guide/wiki/XSLT-Support).

Both

```
mvn compile exec:java
mvn test
```

works.

`006-subroute-exception`
------------------------

Subroute exception propagation examples. Run different cases with `-Dexec.args=` option:

```
mvn compile exec:java -Dexec.args=1
mvn compile exec:java -Dexec.args=2
```

`007-json-marshal`
------------------

JSON marshal/unmarshal with [JSON Jackson](https://camel.apache.org/components/latest/dataformats/json-jackson-dataformat.html) data format.

`008-test-mock`
---------------

How to unit test [existing routes](https://camel.apache.org/manual/latest/testing.html#Testing-Testingexistingroutes) with [Mock](https://camel.apache.org/components/latest/mock-component.html) component and [adviseWith](https://cwiki.apache.org/confluence/display/CAMEL/AdviceWith).

Both

```
mvn compile exec:java
mvn test
```

works, but of course `mvn test` is more important.

`009-calling-paginated-api`
---------------------------

How to call paginated API for all pages and aggregates the results. Requires [standalone](http://wiremock.org/docs/running-standalone/) [Wiremock](http://wiremock.org/) running:

```
--root-dir wiremock/case-01
--root-dir wiremock/case-02
```

`010-routing-java-objects`
--------------------------

How to route arbitrary Java objects. Uses [Splitter](https://camel.apache.org/components/latest/eips/split-eip.html) EIP to process every object in a Java container.

`011-simple-expression-language`
--------------------------------

[Simple](https://camel.apache.org/components/latest/languages/simple-language.html) expression language examples.

`012-xml-mangling`
------------------

XML example: get pieces from input XML and then use those to build a new XML with Saxon XSLT.

`013-split`
---------

How to use [Splitter](https://camel.apache.org/components/latest/eips/split-eip.html) and [Filter](https://camel.apache.org/components/latest/eips/filter-eip.html) EIPs:

1. with `AggregationStrategy` that merges the results into Java `List`.
2. with [Multicast](https://camel.apache.org/components/latest/eips/multicast-eip.html) EIP.

Run different cases with `-Dexec.args=` option:

```
mvn compile exec:java -Dexec.args=1
mvn compile exec:java -Dexec.args=2
```

`014-error-handling`
------------------

[Error handler](https://camel.apache.org/manual/error-handler.html) and [exception](https://camel.apache.org/manual/exception-clause.html) clause examples.

`015-calling-java-from-route`
---------------------------

A pattern how to combine the request and the response processor so that all the requests can be build up-front and then routed without branching logic.