
function [dm, ref] = m_distmat( pos )
% Use positions and thresholds to create distance matrix.
% 	INPUT:  position (imgIDs x 3)
%           n_th (1)
%               Threshold for how many neighbours we use

% 	OUTPUT: image names (imgIDs x 1), 
%           corresponding positions (imgIDs x 3)

%   Our Example:
%       [d] = m_distmat(pos, 1.8, 0.98, 0.8);



%% Distance matrix
% http://stackoverflow.com/questions/19360047/how-to-build-a-distance-matrix-without-loop
% A. Donda's solution
x = [pos(:,1)'; pos(:,2)'];
IP = x' * x;

maxheight = max(pos(:,3));

dm_ = sqrt(bsxfun(@plus, diag(IP), diag(IP)') - 2 * IP);
[~, ref] = min(mean(dm_,1),[],2);

dm = zeros(size(dm_));

dm_ = triu(dm_);
dm_(dm_==0) = NaN;

[val, j] = min(dm_, [], 2);
j = j(1:end-1);
i = [1:length(j)]';

% Closest neighbor list
minima = sub2ind(size(dm_), i, j);
dm(minima) = 1;

% Average neigbor limit
n_th = 1.2;
neighborlimit = mean(val(1:end-1)) * n_th;
dm(dm_ < neighborlimit) = 1;
% Fo sho' include the last image
dm(end-1, end) = 1;

dm = dm + dm';


% Plotting distance matrix
% figure(1)
% spy(dm)
% title('Plots from m distmat')

% Plotting flight plan
figure(1)
clf;
scatter(pos(:,1)', pos(:,2)', 200,  pos(:, 3)/maxheight)
axis equal
hold on
scatter(pos(ref,1)', pos(ref,2)', 500,  pos(ref, 3)/maxheight, 'r', 'filled')
drawnow;

% Statistics
AverageOfNeighbor = mean(sum(dm))/2
end
