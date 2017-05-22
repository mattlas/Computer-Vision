function [dmlist] = m_4distmat( pos)
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



%% Distance matrix
% http://stackoverflow.com/questions/19360047/how-to-build-a-distance-matrix-without-loop
% A. Donda's solution
x = [pos(:,1)'; pos(:,2)'];
IP = x' * x;
dm = sqrt(bsxfun(@plus, diag(IP), diag(IP)') - 2 * IP);

% Make dm logical
[~, I] = sort(dm,2);
dmlist = I(:,2:5);
% Plotting distance matrix
% figure(1)
% subplot(2,1,1)
% spy(dm)
% title('Plots from m distmat')
end
