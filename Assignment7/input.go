package main
var x int;
var y bool;
x = 10;
y = true;

// Relational operator only works for integers at the moment
if x < 5 {
    Println("hello world!");
    Println("Testing..1..2...3");
}

if 5 < 6 {
    Println("5 < 6");

    if 5 <= 5 {
        Println("5 <= 5");
    }

    if 3 > 5 {
        Println("ERROR: 3 > 5");
    }

    if 10.5 <= 11 {
        Println("10.5 <= 11");

        if 12.5 >= 12.5 {
            Println("12.5 >= 12.5");

            if 5 != 5.0 {
                Println("ERROR: 5 != 5.0");
            }
        }

        if 10 > 10.1 {
            Println("ERROR: 10 > 10.1");
        }
    }
}

if x < 100 {
    Println("It works!");
}

var loop int;
loop = 5;
for (loop <= 0) {

}
