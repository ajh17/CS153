package main
i int;
quarter int;
dime int;
nickel int;
penny int;

for i = 1; i <= 100; i = i + 1 {
    Println(i);

    if i % 3 == 0 {
        Println("fizz");
    }

    if i % 5 == 0 {
        Println("buzz");
    }
}

for quarter = 0; quarter <= 4; quarter = quarter + 1 {
    for dime = 0; dime <= 10; dime = dime + 1 {
        for nickel = 0; nickel <= 20; nickel = nickel + 1 {
            for penny = 0; penny <= 100; penny = penny + 1 {
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

// An array of 32 integers
arr [32]int;
primes [100]bool;
