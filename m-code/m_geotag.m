function [ imgIDs, pos , ref ] = m_geotag
% Reading in the geotag data and converting it to matlab structure.
% 	INPUT: 

% 	OUTPUT: image names (imgIDs x 1),
%           corresponding positions (imgIDs x 3),
%           orientations (imgIDs x 3).

%   Our Example:
%   [imageIDs, pos, orient] = m_geotag('block04_cameraPositions.txt');

% Convert text to matlab table.
im_file = dir('*.JPG');
for i = 1:size(im_file, 1)
    imgIDs{i, 1} = im_file(i).name;
    im_data = imfinfo(char(im_file(i).name));
    pos(i, 1:3) = [ dms2degrees(im_data.GPSInfo.GPSLongitude), ...
        dms2degrees(im_data.GPSInfo.GPSLatitude), ...
        im_data.GPSInfo.GPSAltitude];    
end
ref = 1;
lat0 = mean(pos(ref, 1));
lon0 = mean(pos(ref,2));
h0 = pos(1, 3);
ref = [lat0, lon0, h0, ref];
[x, y, z] = geodetic2ned(pos(:,1), pos(:,2), pos(:,3), lat0, lon0, h0, wgs84Ellipsoid);
pos = [x y z];
end
