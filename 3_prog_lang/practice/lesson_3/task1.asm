%macro pushn 0-* 0
    %rep %0
        push %1
        %rotate 1
    %endrep
%endmacro

%macro popn 0-* 0
    %rep %0
        %rotate -1
        pop %1
    %endrep
%endmacro

pushn 1, 2, 3
popn 1, 2, 3

