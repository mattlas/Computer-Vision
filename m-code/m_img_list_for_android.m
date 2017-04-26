function [ dataStr ] = m_img_list_for_android(imageIDs, impos, P, d);
%UNTITLED Summary of this function goes here
% function for saving the file with data for android app
%   Detailed explanation goes here
%    'blablabla.jpg', [2, 3], [ , , ; , , ; , , ], [ , , , , ]


fid = fopen('androidData.txt', 'w');

% searching for the closest 4 neighbours and sorting into NESW
for ids = 1:size(imageIDs,1)
    data = {
        'image name: ', strjoin(imageIDs(ids)), ...
        ', center: ', num2str(impos(ids,:)), ...
        ' inverse matrix:  [', num2str(P{ids}(1,:)), ' ; ' , ...
        num2str(P{ids}(2,:)), ' ; ' , ...
        num2str(P{ids}(3,:)), ' ] ', ...
        ' neighbour index: ', num2str(find(d(:,ids))')};
    dataStr = strjoin(data);
    % searching for the closest 4 neighbours and sorting into NESW
    % add neighbours to the data
    fprintf(fid, dataStr);
    fprintf(fid, '\n');
end
fclose(fid);
end