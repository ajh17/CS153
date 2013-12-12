package main
var temp float;
var temp2 float;
var i int;

for i = 1; i <= 100; i = i + 1 {
    Println(i);

    temp = i % 3;
    if temp == 0 {
        Println("fizz");
    }

    temp2 = i % 5;

    if temp2 == 0 {
        Println("buzz");
    }
}

// An array of 32 integers
var arr [32]int;
var primes [100]bool;
