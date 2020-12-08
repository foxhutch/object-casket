# Object Casket
Object Casket is a simple O/R mapper that can be used together with the [Java Persistence API (JPA)](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html).

The aim is to provide a simple solution for small projects to store multi-related entities in a simple manner. The current version is
experimental and still work in progress. So use at your own risk.

# Maven & Co.
If you want to use maven or some similar tool add the following code to your pom:
```xml
<repositories>
	<repository>
		<id>github</id>
		<name>GitHub Maven Packages</name>
		<url>https://nexus.fuchss.org/repository/github/</url>
	</repository>
</repositories>
<dependencies>
  <dependency>
    <groupId>org.fuchss</groupId>
    <artifactId>object-casket</artifactId>
    <version>develop-SNAPSHOT</version>
  </dependency>
</dependencies>
```
