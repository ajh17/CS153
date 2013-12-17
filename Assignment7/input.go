package main

x int;
x = 10;
y float;
y = 15.5;
z float;
z = 99.9;

// Need to work on pass by reference
func passByReference(a *int, b float, c *float) void {
    Println("Inside function");
    Println(a);
    Println(b);
    Println(c);
    a = 5;
    b = 25.5;
    c = 100.123;
    Println(a);
    Println(b);
    Println(c);
    Println(x); // This would be 5 in Java. However, due to the limitations of the teacher's implementation
                // It x doesn't become 5 until it leaves the function and unwraps the Wrap class
    Println("Outside function");
}

func fizzbuzz() void {
    i int;
    for i = 1; i <= 100; i++ {
        Println(i);

        if i % 3 == 0 {
            Println("fizz");
        }

        if i % 5 == 0 {
            Println("buzz");
        }
    }
}


func getDollarPermutations() void {
    quarter int;
    dime int;
    nickel int;
    penny int;

    for quarter = 0; quarter <= 4; quarter++ {
        for dime = 0; dime <= 10; dime++ {
            for nickel = 0; nickel <= 20; nickel++ {
                for penny = 0; penny <= 100; penny++ {
                    if quarter * 25 + dime * 10 + nickel * 5 + penny == 100 {
                        Println("These make up a dollar");
                        Println("Quarters:");
                        Println(quarter);
                        Println("Dimes:");
                        Println(dime);
                        Println("Nickels:");
                        Println(nickel);
                        Println("Pennies:");
                        Println(penny);
                    }
                }
            }
        }
    }
}

getDollarPermutations();
fizzbuzz();
passByReference(x, y, z);
Println(x);
Println(y);
Println(z);
