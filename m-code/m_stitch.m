function [canvas, center, offs] = m_stitch( imageIDs, P , method)

% Crop Factor
cf = 0.25;
center = zeros(size(imageIDs, 1), 2);
tile = cell(size(imageIDs,1), 1);
t = zeros(size(imageIDs,1), 2);
imsize = zeros(size(imageIDs,1), 3);

% Go through all the images
for ids=1:size(imageIDs,1)
    % Read in images
    im = iread(char(imageIDs{ids,1}), 'double');
%     im = ones(10);
    
    [tile{ids},t(ids,:)] = homwarp(P(:,:,ids), im, 'full');
    
    imsize(ids,:) = size(tile{ids});
    tile{ids} = imcrop(tile{ids},[imsize(ids, 2)*cf imsize(ids, 1)*cf imsize(ids, 2)*(1-cf*2)  imsize(ids, 1)*(1-cf*2)]);
    tile{ids}(:,:,4) = 1;
    center(ids, :) = t(ids,:) + [ imsize(ids, 2)*(1-cf*2)/2  imsize(ids, 1)*(1-cf*2)/2 ];
end
imsize_ = max(imsize);
offs = - min(t) + [2 2];
offs_max = max(t) +  [ imsize_(1)*(1-cf) imsize_(2)*(1-cf) ] + [2 2];
center = center + offs;
t = t + offs;

canvas = zeros( ceil(offs_max(2) + offs(2)), ...
                ceil(offs_max(1) + offs(1)));
            
            % Probably an unnecessary for loop
for ids = 1:size(imageIDs,1)
    canvas = ipaste(canvas, tile{ids}, t(ids,:), method);
end
    canvas = canvas ./ canvas(:,:,4); 
    
    
    
%     imshow(canvas)
% hold on
% scatter(center(:,1)', center(:,2)', 200, 'b', 'filled')

end