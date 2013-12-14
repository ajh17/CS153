package main
i int;
i = 5;

func printStuff(x int) void {
    Println("Inside function");
    x = 20;
    Println(x);
}

printStuff(i);
i = 10;
printStuff(i);
Println(i);