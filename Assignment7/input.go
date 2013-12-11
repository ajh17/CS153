package main
var x int;
var y bool;
x = 10;
y = true;
var a int;
var b float;
a = -4;
b = -5.102;

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
        Println("err: 3 > 5");
    }
    else {
        Println("else from: 3 > 5");
    }

    if 10.5 <= 11 {
        Println("10.5 <= 11");

        if 12.5 >= 12.5 {
            Println("12.5 >= 12.5");

            if 5 != 5.0 {
                Println("err: 5 != 5.0");
            }
        }

        if 10 > 10.1 {
            Println("err: 10 > 10.1");
        }
    }
}
else if 4 == 4 {
    Println("4 == 4");
}
else {
    Println("else from: 4 == 4");
}

if x < 100 {
    Println("It works!");
}

var loop int;
loop = 5;
/* while loops */
for loop <= 0 {

}

for (loop <= -2) {

}

/* C-Style for loops */
/*
for (loop = 0; loop <= 100; loop++) {

}
*/
