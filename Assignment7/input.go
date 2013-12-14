package main
x int;
y int;
x = 1;
y = 2;

// Need to work on parameter passing
func printOne(first int) void {
    Println(first);
}

func printXY(first int, second int) void {
    Println(first);
    Println(second);
}

printOne(x);
printXY(x, y);
printOne(1);
printXY(100, 200);
printOne(1 * 3 + 4 / 2);
printXY(1 * 3 + 4 / 2, x);