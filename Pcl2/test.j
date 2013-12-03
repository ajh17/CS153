.class public Test
.super java/lang/Object

.field private static _runTimer LRunTimer;
.field private static _standardIn LPascalTextIn;

.field private static i I
.field private static j I
.field private static k I
.field private static x F
.field private static y F
.field private static z F

.method public <init>()V

	aload_0
	invokenonvirtual	java/lang/Object/<init>()V
	return

.limit locals 1
.limit stack 1
.end method

.method public static main([Ljava/lang/String;)V

    new	 RunTimer
    dup
    invokenonvirtual	RunTimer/<init>()V
    putstatic	Test/_runTimer LRunTimer;
    new	 PascalTextIn
    dup
    invokenonvirtual	PascalTextIn/<init>()V
    putstatic	Test/_standardIn LPascalTextIn;

    ldc 1
    putstatic Test/i I
    getstatic Test/i I
    ldc 3
    iadd
    putstatic Test/j I
    ldc 2
    getstatic Test/j I
    imul
    putstatic Test/k I
    getstatic Test/i I
    getstatic Test/j I
    iadd
    i2f
    putstatic Test/x F
    ldc 3.1415925
    getstatic Test/i I
    i2f
    fadd
    getstatic Test/j I
    i2f
    fsub
    getstatic Test/k I
    i2f
    fadd
    putstatic Test/y F
    getstatic Test/x F
    getstatic Test/i I
    getstatic Test/j I
    imul
    getstatic Test/k I
    idiv
    i2f
    fadd
    getstatic Test/x F
    getstatic Test/y F
    fdiv
    fsub
    putstatic Test/z F

    getstatic	Test/_runTimer LRunTimer;
    invokevirtual	RunTimer.printElapsedTime()V

    return

.limit locals 6
.limit stack  16
.end method
