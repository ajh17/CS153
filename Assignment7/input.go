package main
var i int;
var j int;
var x float;
x = 0;

for i = 5; i <= 10; i = i + 1 {
    for j = 1; j <= 5; j = j + 1 {
        x = x + i * j - 10 / 2;
        Println(x);

        if x < 100 {
            Println("I'm still less than 100");
        }
        else {
            Println("I'm greater or equal than 100");
        }
    }
}

// An array of 32 integers
var arr [32]int;
var primes [100]bool;
