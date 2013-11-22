package main
var x int
var foo; // no type
var float; // no variable name
var didFallDown boolean; // no boolean type; called bool

if true { // all this is valid and should be in cross ref table
    var a int;
    a = true; 
    var d int;
    d = 5;
}