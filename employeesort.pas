program SortEmployees;

type
    Classification = (Factory, Office, Supervisor, VP, President);
    Gender = (M, F);

    BirthDate = record
        birthYear  : array [0..3] of char;
        birthMonth : array [0..9] of char;
        birthDay   : array[0..1] of char;
    end;

    Employee = record
        id             : PtrInt;
        lastName       : array [0..7] of char;
        initials       : array [0..1] of char;
        birthdate      : BirthDate;
        departmentCode : array[0..3] of char;
        gender         : Gender;
        classification : Classification;
    end;

    Node = record
        data  : ^Employee;
        left  : ^Node;
        right : ^Node;
    end;

    NodePtr = ^Node;
    EmployeePtr = ^Employee;

var
    inputFile : text;
    buffer : array [0..255] of char;
    root : NodePtr;
    tempEmployee : EmployeePtr;
    error : PtrInt;
    tempBirthMon : array[0..1] of char;
    tempBirthDay : array[0..1] of char;
    tempStrBirthDate: String;
    Workers : set of Classification = [Factory, Office];
    Managers : set of Classification = [Supervisor..President];
    workerCount : PtrInt; (* total number of workers *)
    managerCount : PtrInt; (* total number of managers *)
    maleCount : PtrInt; (* total number of males *)
    femaleCount : PtrInt; (* total number of females *)


procedure initNode (node : Nodeptr);
begin
    node^.data := nil;
    node^.left := nil;
    node^.right := nil;
end;

function compare (first : EmployeePtr; second : EmployeePtr) : PtrInt;
begin
    compare := first^.id - second^.id;
end;

function insertSearch (var root, newNode : NodePtr) : NodePtr;
begin
    if (root = nil) then
        insertSearch := newNode
    else if (compare(newNode^.data, root^.data) < 0) then
    begin
        root^.left := insertSearch(root^.left, newNode);
        insertSearch := root;
    end
    else
    begin
        root^.right := insertSearch(root^.right, newNode);
        insertSearch := root;
    end;
end;

procedure insertNode (var root : NodePtr; var newData : EmployeePtr);
var newNode : NodePtr;
begin
    new(newNode);
    initNode(newNode);
    newNode^.data := newData;

    if (root = nil) then
        root := newNode
    else
        insertSearch(root, newNode);
end;

procedure printPreorderBST (root : NodePtr);
begin
    if (root <> nil) then
    begin
        printPreorderBST(root^.left);
        with root^.data^ do
        begin
            FillChar(tempStrBirthDate[0],20,#32);
            tempStrBirthDate := birthdate.birthMonth + ' ' 
            + birthdate.birthDay +  ', ' + birthdate.birthYear;
            Setlength(tempStrBirthDate,20);

            writeln(id:7, '  ' , lastName:8, '  ', initials:2, '      ', 
            tempStrBirthDate, ' '  , departmentCode:4, '  ' , gender:6, ' ', classification);
        end;
        printPreorderBST(root^.right);
    end;
end;

procedure printSummaryReport;
begin
    writeln;
    writeln('Summary':12);
    writeln('------------------');
    writeln('Males:', maleCount:11);
    writeln('Females: ', femaleCount:8);
    writeln('Worker count: ', workerCount:3);
    writeln('Manager count: ', managerCount:2);
end;


procedure readFile;
begin
    root := nil;
    assign(inputFile, 'employees.in');
    reset(inputFile);

    repeat
        new(tempEmployee);
        readln(inputFile, buffer);
        val(copy(buffer, 1, 7), tempEmployee^.id, error);
        tempEmployee^.lastName := copy(buffer, 9, 8);
        tempEmployee^.initials := copy(buffer, 17, 2);

        tempEmployee^.birthdate.birthYear := copy(buffer, 20, 4);
        tempBirthMon := copy(buffer, 24, 2);
        if (tempBirthMon = '01') then
            tempEmployee^.birthdate.birthMonth := 'January'
        else if (tempBirthMon = '02') then
            tempEmployee^.birthdate.birthMonth := 'February'
        else if (tempBirthMon = '03') then
            tempEmployee^.birthdate.birthMonth := 'March'
        else if (tempBirthMon = '04') then
            tempEmployee^.birthdate.birthMonth := 'April'
        else if (tempBirthMon = '05') then
            tempEmployee^.birthdate.birthMonth := 'May'
        else if (tempBirthMon = '06') then
            tempEmployee^.birthdate.birthMonth := 'June'
        else if (tempBirthMon = '07') then
            tempEmployee^.birthdate.birthMonth := 'July'
        else if (tempBirthMon = '08') then
            tempEmployee^.birthdate.birthMonth := 'August'
        else if (tempBirthMon = '09') then
            tempEmployee^.birthdate.birthMonth := 'September'
        else if (tempBirthMon = '10') then
            tempEmployee^.birthdate.birthMonth := 'October'
        else if (tempBirthMon = '11') then
            tempEmployee^.birthdate.birthMonth := 'November'
        else
            tempEmployee^.birthdate.birthMonth := 'December';

      {if the birth day starts with 0, get rid of it. ie. May 01, should be May 1}
        tempBirthDay := copy(buffer, 26, 1);
        if(tempBirthDay = '0') then
            tempEmployee^.birthdate.birthDay := copy(buffer, 27, 1)

        else
            tempEmployee^.birthdate.birthDay := copy(buffer, 26, 2);

        tempEmployee^.departmentCode := copy(buffer, 29, 4);

        case buffer[33] of 
            'F': tempEmployee^.gender := F;
            'M': tempEmployee^.gender := M;
        end;

        case buffer[35] of
            'F': tempEmployee^.classification := Factory;
            'O': tempEmployee^.classification := Office;
            'S': tempEmployee^.classification := Supervisor;
            'V': tempEmployee^.classification := VP;
            'P': tempEmployee^.classification := President;
        end;

        if (tempEmployee^.gender = F) then
            femaleCount := femaleCount + 1
        else
            maleCount := maleCount + 1;

        if (tempEmployee^.classification in Workers) then
            workerCount := workerCount + 1
        else
            managerCount := managerCount + 1;

        insertNode(root, tempEmployee);

    until eof(inputFile);

    close(inputFile);
end;


{----MAIN----}
begin {SortEmployees}
    readFile;
    writeln;
    writeln('EMP ID   LAST NAME INITIAL BIRTH DATE          DEPT  GENDER CLASSIFICATION');
    writeln('-------- --------- ------- ------------------- ----- ------ --------------');
    printPreorderBST(root);
    printSummaryReport();
    dispose(tempEmployee);
end.
