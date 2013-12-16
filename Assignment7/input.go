package main

func GoMain() void {
    x int;
    x = 10;

    // Need to work on pass by reference
    func passByReference(y *int) void {
        y = 5;
        Println(y);
    }

    passByReference(x);
    Println(x);
}

GoMain();