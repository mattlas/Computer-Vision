m = matrixData;

for i = 1:height(m)
   matrix = m(i,:);

   coolMatrix = [matrix{1,1} matrix{1,2} matrix{1,3}; ...
                 matrix{1,3} matrix{1,8} matrix{1,6}; ...
                 matrix{1,7} matrix{1,8} matrix{1,9}];
   disp(coolMatrix)
end

