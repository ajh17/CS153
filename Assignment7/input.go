package main
var i int;

for i = 1; i <= 100; i = i + 1 {
    Println(i);

    if i % 3 == 0 {
        Println("fizz");
    }

    if i % 5 == 0 {
        Println("buzz");
    }
}

// An array of 32 integers
var arr [32]int;
var primes [100]bool;
