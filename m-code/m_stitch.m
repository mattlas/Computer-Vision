function [canvas, center] = m_stitch( imageIDs, P , method)

% Crop Factor
cf = 0.2;
canvas = zeros(3000,3000);
center = zeros(size(imageIDs, 1), 2);

% Go through all the images
for ids=1:size(imageIDs,1)
    % Read in images
    im = iread(char(imageIDs{ids,1}), 'double');
%     im = ones(10);
    
    %Cropping
%     imsize = size(im);
%     im = imcrop(im,[imsize(2)*cf imsize(1)*cf imsize(2)*(1-cf*2)  imsize(1)*(1-cf*2)]);
    [tile,t] = homwarp(P{ids}, im, 'full');
    
    imsize = size(tile);
    tile = imcrop(tile,[imsize(2)*cf imsize(1)*cf imsize(2)*(1-cf*2)  imsize(1)*(1-cf*2)]);

    canvas = ipaste(canvas, tile, t + [1000 1000], method);
    imshow(canvas)
    center(ids, :) = t + [ imsize(2)*(1-cf*2)/2  imsize(1)*(1-cf*2)/2 ];
end

end