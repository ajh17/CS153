package main
var x int;
x = 0;

for x < 5 {
    Println("While loop");
    x = x + 1;

    if x == 3 {
        Println("I'm 3");
    }
    else {
        Println("I'm NOT 3");
    }
}

for x = 0; x < 5; x = x + 1 {
    Println("For loop");
}
