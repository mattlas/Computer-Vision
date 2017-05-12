function pairs = m_pairify ( d )
pairs = zeros(0, 2);
% Go through every image
for i = 1 : size(d, 1)
    % Check the neighbors with higher indeces (no doublecheck)
    neighbors = find(d(i:end,i))+i-1;
    % Number of neighbors
    n = length(neighbors);
    % Update pairs vector 
    pairs(end+1:end+n,:) = [repmat(i, n, 1), neighbors];
end
end