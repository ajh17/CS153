package main
i int;
i = 5;

func printStuff(x int) void {
    Println("Inside function");
    x = 20;
    Println(x);
    Println(i); // Need to fix this. If i is not declared locally, look in static variables.
}

printStuff(i);
i = 10;
printStuff(i);
Println(i);

if i == 10 {
    Println("i = 10");
}
else {
    Println("i != 10");
}