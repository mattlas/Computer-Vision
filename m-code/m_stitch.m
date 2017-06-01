function [center, offs] = m_stitch( imageIDs, P , method)

% Crop Factor
cf = 0.2;
center = zeros(size(imageIDs, 1), 2);
tile = cell(size(imageIDs,1), 1);
t = zeros(size(imageIDs,1), 2);
imsize = zeros(size(imageIDs,1), 3);

% Go through all the images
for ids=1:size(imageIDs,1)
    % Read in images
    im = iread(char(imageIDs{ids,1}), 'double');
    %     im = ones(10);
%     H = maketform('affine', P(:,:,ids)');
%     [tile{ids}, xdata, ydata] = imtransform(im, H);
    H_ = affine2d(P(:,:,ids)');
    [tile{ids}, rb] = imwarp(im, H_);
    t(ids, :) = [rb.XWorldLimits(1), rb.YWorldLimits(1)];
%     [tile{ids},t(ids,:)] = homwarp(P(:,:,ids), im, 'full');
    
    imsize(ids,:) = size(tile{ids});
    tile{ids} = imcrop(tile{ids},[imsize(ids, 2)*cf imsize(ids, 1)*cf imsize(ids, 2)*(1-cf*2)  imsize(ids, 1)*(1-cf*2)]);
    if (strcmp(method,'add'))
        tile{ids}(:,:,4) = 1;
    end
    center(ids, :) = t(ids,:) + [ imsize(ids, 2)*(1-cf*2)/2  imsize(ids, 1)*(1-cf*2)/2 ];
end
imsize_ = max(imsize);
offs = - min(t) + [2 2];
% offs_max = max(t) +  [ imsize_(1)*(1-cf) imsize_(2)*(1-cf) ] + [2 2];
offs_max = max(t) +  [ imsize_(2)*(1-cf) imsize_(1)*(1-cf) ] + [2 2];
center = center + offs;
t = t + offs;

canvas = zeros( ceil(offs_max(2) + offs(2)), ...
    ceil(offs_max(1) + offs(1)));

if (strcmp(method,'add'))
    canvas(:,:,4) = 1;
end
% Probably an unnecessary for loop
for ids = 1:size(imageIDs,1)
    canvas = ipaste(canvas, tile{ids}, t(ids,:), method);
end
if (strcmp(method,'add'))
    canvas = canvas(:,:,1:3) ./ canvas(:,:,4);
end
figure(3)
imshow(canvas)
iptsetpref('ImshowBorder','tight');
imwrite(canvas, ['Mosaic_',datestr(datetime('now')),'.png'])
end