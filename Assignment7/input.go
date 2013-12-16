package main

x int;
x = 10;
y float;
y = 15.5;

// Need to work on pass by reference
func passByReference(y *int, z float) void {
    y = 5;
    z = 25.5;
    Println(y);
    Println(z);
}

passByReference(x, y);
Println(x);
Println(y);
