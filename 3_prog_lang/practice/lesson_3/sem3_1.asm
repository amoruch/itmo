%define num 10
%define i 0
%rep num
  db 'a' + i
  %if i < num - 1
    db ", "
  %endif
  %define i i + 1
%endrep
%undef i

