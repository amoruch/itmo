%macro abc 1
  %assign %%i 0
  %rep %1
    db 'a' + %%i
    %if %%i < %1 - 1
      db ", "
    %endif
    %assign %%i %%i + 1
  %endrep
%endmacro

abc 5

%macro alloc_string 2+
    %1: db %2
%endmacro

alloc_string msg, "hello", ",", " world"

%macro alloc_string_2 1-2+ "Hello, world!", 10, 0
    %1: db %2
%endmacro

alloc_string_2 msg1
alloc_string_2 msg2, "hello", ",", " world"
