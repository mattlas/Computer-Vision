function [ imgIDs, pos, orient ] = m_geotag( gt_path )
% Reading in the geotag data and converting it to matlab structure.
% 	INPUT:  path of geotag text

% 	OUTPUT: image names (imgIDs x 1), 
%           corresponding positions (imgIDs x 3), 
%           orientations (imgIDs x 3).

%   Our Example:
%   [imageIDs, pos, orient] = m_geotag('block04_cameraPositions.txt');          

% Convert text to matlab table.
tags = readtable(gt_path, 'HeaderLines', 1);

% Name of the images
imgIDs = table2array(tags(:,1));

% Position data
pos = table2array(tags(:,2:4));

% Orientation data (not yet available)
orient = table2array(tags(:,2:4));

end
