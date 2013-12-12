package main
var i int;
var remainder3 int;
var remainder5 int;
var temp float;
remainder3 = 0;
remainder5 = 0;

for i = 1; i <= 100; i = i + 1 {
    temp = i / 3;
    if temp > remainder3 {
        Println("Fizz");
        remainder3 = remainder3 + 1;
    }

    temp = i / 5;
    if temp > remainder5 {
        Println("Buzz");
        remainder5 = remainder5 + 1;
    }

    Println(i);
}

// An array of 32 integers
var arr [32]int;
var primes [100]bool;
