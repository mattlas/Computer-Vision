function [ data ] = m_img_list_for_android(imageIDs, impos, P, d);
%UNTITLED Summary of this function goes here
% function for saving the file with data for android app
%   Detailed explanation goes here
%    'blablabla.jpg', [2, 3], [ , , ; , , ; , , ], [ , , , , ]


fid = fopen('androidData.txt', 'w');

% searching for the closest 4 neighbours and sorting into NESW
for ids = 1:size(imageIDs,1)
    data = [
        strjoin([imageIDs(ids), ","], ''), ...
        sprintf('%.0f,' ,impos(ids,1:2)), ...
        sprintf('%.0f,' ,P{ids}(1,:)) , ...
        sprintf('%.0f,' ,P{ids}(2,:)) , ...
        sprintf('%.0f,' ,P{ids}(3,:)), ...
        sprintf('%.0f,' ,d(ids,:))];
    data = strjoin(data, '');
    fprintf(fid, data);
    fprintf(fid, '\n');
end
fclose(fid);
end