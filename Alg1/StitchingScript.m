% run('vlfeat-0.9.20/toolbox/vl_setup')
% clear all
% close all
% help vl_siftaddpath(strcat(pwd,'/vlfeat-0.9.20'));

A = imread('im1.JPG');
B = imread('im2.JPG');

% A = imresize(A,0.35);
% B = imresize(B,0.35);

% I2 = imcrop(A,[1000 3000 1000 2000]);
% AC = A(500:3500, 1000:5000, :);
% figure(1);
% imshow(A);
% figure(2);
% imshow(AC);
% A = imread('css1.JPG');
% B = imread('css2.JPG');
% mosaic = sift_mosaic(A,B);

% figure(1);
% imagesc(A);
% figure(2);
% imagesc(B);
% figure(3);
% imagesc(mosaic);

% A_Gray = rgb2gray(A); % Convert to gray ...
% imagesc(A_Gray);
% B_Gray = rgb2gray(B); % Convert to gray ...
% tic
mosaic = sift_mosaic(A,B);
% toc

% rmat = A(:,:,1);
% subplot(2,2,1), imagesc(rmat); title('Red Plane');
% gmat = A(:,:,2);
% subplot(2,2,2), imagesc(gmat); title('Green Plane');
% bmat = A(:,:,3);
% subplot(2,2,3), imagesc(bmat); title('Blue Plane');
% subplot(2,2,4), imagesc(A); title('Original Image');
% 
% % Treashold level ...
% level = 0.6;
% A_MOD = imbinarize(A_Gray,level);
% 
% figure(1);
% imshowpair(A,A_Gray,'montage');
% figure(2);
% imshowpair(A,A_MOD,'montage');