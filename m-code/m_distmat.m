function [dm, restr] = m_distmat( pos, n_th, h_th, d_th )
% Use positions and thresholds to create distance matrix.
% 	INPUT:  position (imgIDs x 3)
%           n_th (1)
%               Threshold for how many neighbours we use
%           h_th (1)
%               Threshold for how many heighs
%           d_th (1)
%               Threshold for turning images


% 	OUTPUT: image names (imgIDs x 1), 
%           corresponding positions (imgIDs x 3), 
%           orientations (imgIDs x 3)

%   Our Example:
%       [d, restr] = m_distmat(pos, 1.8, 0.98, 0.8);

if nargin<3, h_th=0; end
if nargin<4, d_th=0; end


%% Distance matrix
% http://stackoverflow.com/questions/19360047/how-to-build-a-distance-matrix-without-loop
% A. Donda's solution
x = [pos(:,1)'; pos(:,2)'];
IP = x' * x;
dm = sqrt(bsxfun(@plus, diag(IP), diag(IP)') - 2 * IP);

% Needed variables
maxheight = max(pos(:,3));
neighborlimit = mean(diag(dm,1)) * n_th;


% Restrictions
up = pos(:, 3) >  maxheight * h_th;
straight = [diag(dm,1);1] > mean(diag(dm,1))*d_th;
restr = up & straight;


% Applying restrictions
dm = dm( restr , restr );

% Make dm logical
dm = sparse(((dm+eye(size(dm,2))) < neighborlimit));

% Plotting distance matrix
% figure(1)
% subplot(2,1,1)
% spy(dm)
% title('Plots from m distmat')

% Plotting flight plan
% subplot(2,1,2)
% pos = pos( restr , : );
% scatter(pos(:,1)', pos(:,2)', 200,  pos(:, 3)/maxheight)
% axis equal
% hold on
% drawnow;

% Statistics
RatioOfUp = sum(up)/size(pos,1);
AverageOfNeighbor = mean(sum(dm));
end
