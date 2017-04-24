 % By Hao Jiang, Boston College, Fall 2010
 % comment by adahbingee 2015/01/20
 % max level
 mp{1} = nan(1500,2000);
 im1p{1} = imread('resized/DSC01110_geotag.JPG');
 im1p{1} = im2single(im1p{1});
 im2p{1} = imread('resized/DSC01111_geotag.JPG');
 im2p{1} = im2single(im2p{1});
M = floor(log2(max(size(imp{1}))));
 % Gaussian pyramid
for n = 2 : M
    % downsample image
    im1p{n} = imresize(im1p{n-1}, 0.5);
    im2p{n} = imresize(im2p{n-1}, 0.5);
    % downsample blending mask
    mp{n} = imresize(mp{n-1}, 0.5, 'bilinear');
end
 
 % Laplician pyramid
for n = 1 : M-1
    im1p{n} = im1p{n} - imresize(im1p{n+1}, [size(im1p{n},1), size(im1p{n},2)]);
    im2p{n} = im2p{n} - imresize(im2p{n+1}, [size(im2p{n},1), size(im2p{n},2)]);   
end   
 
 % Multi-band blending Laplician pyramid
for n = 1 : M
    imp{n} = im1p{n} .* mp{n} + im2p{n} .* (1-mp{n});
end
 
 % Laplician pyramid reconstruction
im = imp{M};
for n = M-1 : -1 : 1
    im = imp{n} + imresize(im, [size(imp{n},1) size(imp{n},2)]);
end