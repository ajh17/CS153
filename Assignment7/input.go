package main
var x int;
var y bool;
x = 10;
y = true;
var a int;
var b float;
a = -4;
b = -5.102;

if 5 < 6 {
    Println("5 < 6");

    if 3 > 5 {
        Println("err: 3 > 5");
    }
    else {
        Println("else from: 3 > 5");
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
