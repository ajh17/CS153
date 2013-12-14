package main
i int;
i = 5;

func printStuff(x int) void {
    Println("Inside function");
    x = 20;
    Println(x);
    Println(i);



    Println("Leaving function");
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