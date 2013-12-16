package main

x int;
x = 10;

// Need to work on pass by reference
func passByReference(y *int) void {
    Println(y);
}

passByReference(x);
Println(x);
