x int
y x; // The type of x will be the type of y
q float;
r string;
x++
--x;
a = 5 + 5 + 5 + 5;
b := 6
c = 7.8
d := 9.0
e = "\tasdf\n"
f = "\\ghjk\"";
g = `\n\n\tabc\t\t`
h := `\r\n"""""def"`;

if true {
    a = 5;
    b := 6
    c = 7.0
    d := "hello\n";
}

if a > b {
    a = b;
}

for i = 0; i < 10; i++ {
    e = "hello\n";
}

switch {
    case true: 
	case false: 
    default: 
}

i := 5
switch i {
    case 4: 
    case 5: 
    case 6:
}