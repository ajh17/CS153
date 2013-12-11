package main
var i int;
var j int;
var x int;
x = 0;

for i = 1; i <= 5; i = i + 1 {
    for j = 1; j <= 5; j = j + 1 {
        x = x + i;
        Println(x);

        if x < 30 {
            Println("I'm still less than 30");
        }
        else {
            Println("I'm greater or equal than 30");
        }
    }
}

/* func reverse(s string) { */

/* } */
