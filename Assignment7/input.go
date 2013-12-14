package main
x int;
x = 1;

Println(x);

func printOne() void {
    Println(x);
    x = 10;
    Println(x);
}

printOne();
Println(x);