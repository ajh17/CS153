package main

x float;
x = 10;

// Need to work on pass by reference
func passByReference(y *float) void {
    y = 5.6;
    Println(y);
}

passByReference(x);
Println(x);
