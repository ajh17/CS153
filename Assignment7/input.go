package main

x int;
x = 10;

// Need to work on pass by reference
func passByReference(x int) void {
    x = 5;
    Println(x);
}

passByReference(x);
Println(x);