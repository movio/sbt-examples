# sbt-examples

Some simple examples on how to create an sbt plugins - as presented at the Movio itch week

Publish the plugin locally

    git clone git@github.com:movio/sbt-examples.git
    cd sbt-examples
    sbt
    > publishLocal

Using the plugin

    cd sbt-examples/playground
    sbt
    > help exampleTask
    > help exampleInput
    > help exampleTaskExecution
    > help commandExample 

# License

Copyright (c) 2013 Movio 

Published under the [Apache License 2.0](http://en.wikipedia.org/wiki/Apache_license).
