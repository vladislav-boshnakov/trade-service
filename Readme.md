
# model.Trade Service

model.Trade service is an application which accepts incoming timestamped
trades and is responsible for acceptance and processing.

## Getting Started

This project was built using TDD, there is no main method
to run it, just run the tests.

You will see that some libraries have been used throughout
most notably [Project Lombok](https://projectlombok.org/) - a 
nifty little library, that removes a lot of the Javism fluff
(like having to write getters and setters)

I recommend opening this with [Intellij IDEA](https://www.jetbrains.com/idea/) and installing the 
Lombok support plugin from the plugin marketplace.

## Running the tests

You can run the tests class by class from your IDE

Alternatively just type in the following into your terminal
```
mvn test
```

## Developer Notes

I've decided to use RxJava because it fits quite well in the domain of 
infinite streams of data. Observers have good error handling(unused here because out of scope)
are fairly fast and can be chained both sequentially and in a multithreading context.

I believe that the persistence layer is an abstraction that did not need to be tested in this case,
testing whether java's map interface works seemed irrelevant to the task at hand.

Assumptions Made:
```
1. When doing the calculations we should round down
2. In memory maps work as a good replacement for a DB layer in this test,
    because the trade size will be small enough.
3. We are not building this as a multithreaded platform
```
 
## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Vladislav Boshnakov** - *Initial work* 
