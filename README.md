# Project 2

Project 2 for 315 with Java and C++ implementations:

Two implementations of a multi-threaded request scheduler in C++ and Java

## C++ Instructions

To run, clone this repository and navigate to '/Cpp', ensure that you have g++ compiler installed (Which should be...)

Then, run `make` and finally `./main` to run the program. Simply input the number of consumer threads.

Producer currently operates with a random "execution time" to produce requests of 1-5s up to a maximum of 5 requests, consumers 1-10s.

To alter the maximum request buffer size, alter line 57 of the source code.

### Note: No exit condition works right now: to quit the program use `ctrl+c`

Acknowledgments: C++ is not a language I am familiar with and therefore I referenced https://cpluspluspedia.com/en/tutorial/9785/semaphore as a guide for the semaphore implementation.

They use a concept in their implementation which I believe to be necessary to ensure the compiler keeps the lock atomic : the inline keyword allows the compiler to not jump around code when calling the function, thus executing in the fastest manner possible. 

To keep things simple for the marking, all classes are in the same file, which I understand is typically bad practice :)


## Java Instructions

To run, clone this repository and navigate to '/CppJava', ensure that you have a java environment installed.

Create a java project and copy the main.java file into the '/src' folder.

Run the program using the run button and insert the number of threads and number of requests when prompted.

Producer currently operates with a random "execution time" to produce requests of 1-5s up to a maximum of 5 requests, consumers 1-10s.

The program will exit automatically when the requests have all been fullfilled.
