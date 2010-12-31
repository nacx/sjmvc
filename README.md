Simple MVC framework for Java
=============================
       
SJMVC is a simple MVC framework for Java, that provides an easy
way to develop Java based web applications following common
best practices.

The intention of this framework is to keep things as simple as
possible. It relies on the JEE standard libraries, and it is up
to the developers to include third party libraries to extend the
core functionality.


Compiling SJMVC
---------------

SJMVC can be compiled as a standard [Maven](http://maven.apache.org/) project:

    mvn clean package
  
That will run all tests and generate the packaged file in the *target/* folder.


Using SJMVC in your project
---------------------------

To use SJMVC in your proyect, just add the dependency in your *pom.xml*:

    <dependency>
        <groupId>org.sjmvc</groupId>
        <artifactId>sjmvc</artifactId>
        <version>0.1-SNAPSHOT</version>
    </dependency>

To use snapshot versions, you will have to add the [Sonatype Snapshot Repository](https://oss.sonatype.org/content/repositories/snapshots/)
to your *pom.xml* as follows:

    <repositories>
        <repository>
            <id>oss-sonatype-snapshots</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
            <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
    
You will find instructions and examples on how to use it in the [project wiki](https://github.com/nacx/sjmvc/wiki).

Customizing and Contributing
----------------------------

Any contribution to the project is welcome. Feel free to check
it out from the [Project site](https://github.com/nacx/sjmvc) and play with it.

Issue Tracking
--------------

If you find any issue, please submit it to the [Bug tracking system](https://github.com/nacx/sjmvc/issues) and we
will do our best to fix it.

License
-------

See LICENSE file.
