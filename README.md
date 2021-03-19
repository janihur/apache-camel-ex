Apache Camel 3 Code Examples
============================

Currently used Camel version: 3.4.3

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

[Timer](https://camel.apache.org/components/latest/timer-component.html) scheduler component example.

`005-xslt`
----------

How to convert XML messages with [XSLT](https://camel.apache.org/components/latest/xslt-component.html) component and how to unit test the transformation with [XMLUnit](https://www.xmlunit.org/)'s [XSLT support](https://github.com/xmlunit/user-guide/wiki/XSLT-Support).

Both

```
mvn compile exec:java
mvn test
```

works.