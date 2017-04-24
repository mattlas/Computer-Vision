%% Hello, this is the project's MATLAB file!
% Load in tools that we need

% % SIFT method
% run('vlfeat-0.9.20/toolbox/vl_setup')
% 
% % Niclas' Bundle Adjustment toolkit
% run('dbat/code/dbatSetup')
% 
% 
% addpath machinevision-toolbox-matlab-master/
% addpath toolbox-common-matlab-master/
% addpath ../hello/

%% Setup work
tic

% First, we take care of the geotags
clear all
close all

% Read in the geotag text
[imageIDs, pos, orient] = m_geotag('block04_cameraPositions.txt');

% Calculate distance matrix
[d, restr] = m_distmat(pos, 1.8, 0.98, 0.8);

% Applying restrictions
pos = pos( restr , : );
imageIDs = imageIDs( restr , : );

%% Support plots

% figure(1)
% scatter(pos(:,1)', pos(:,2)')
% axis equal
% hold on
% testpoint = randi(size(imageIDs,1));
% testneighbors = find(d(:,testpoint));
% scatter(pos(testpoint,1)', pos(testpoint,2)',600, 'b','x')
% scatter(pos(testneighbors,1)', pos(testneighbors,2)', 200, 'r', 'filled')
% 
% 
% % Scatter plot of all the straight drone positions in XY plane for all of the images
% 
% for i = 1:size(imageIDs,1)
%     clf
%     figure(1)
%     scatter(pos(:,1)', pos(:,2)', 200,  pos(:, 3)/maxheight)
%     axis equal
%     hold on
%     testpoint = i;
%     testneighbors = find(d(:,testpoint));
%     scatter(pos(testpoint,1)', pos(testpoint,2)',600, 'b','x')
%     scatter(pos(testneighbors,1)', pos(testneighbors,2)', 200, 'r', 'filled')
%     pause(2)
% end


%% User input of first ID
% figure(4)
% scatter(pos(:,1)', pos(:,2)', 200, 'r', 'filled')
% hold on
% a = [1:size(pos,1)]'; b = num2str(a); c = cellstr(b);
% dy = -0.0001; dx = -0.0001; % displacement so the text does not overlay the data points
% text(pos(:,1)+dx, pos(:,2)+dy, c,'FontSize',8);
% 
% promptfirst =  'What is the first position? ';
% promptlast =  'What is the last position? ';
% 
% first = input(promptfirst);
% last = input(promptlast);



%                       I ONLY USE FIVE IMAGES!!!!!!!!!!!
                first = 2;
                last = 7;
               
                
                
% Narrow arrays to the straight parts
pos = pos(first:last,:);
imageIDs = imageIDs(first:last,1);
d = d(first:last,first:last);
% scatter(pos(:,1)', pos(:,2)', 200, 'b', 'filled')
% drawnow;


%% WGS84 to XYZ
pos = m_wgs2xyz(pos);

% Changing reference to first image
pos = pos - pos(1, :);

toc

%% SIFT / SURF of each image
firstOct = 3;
featureTh = 500;
tic
cd resized


[F, D] = m_sift(imageIDs, firstOct, featureTh);

cd ..
toc


%% Image comparison

tic
cd resized

Hom = m_ransac(imageIDs, F, D, d);

cd ..
toc


%% MOSAIC

for ids = 1:siz

tic
cd resized
figure(5)
clf(5)
% Crop Factor
cf = 0.25;
composite = nan(10000,10000);
for ids=1:size(imageIDs)-1
    im = iread(char(imageIDs{ids,1}), 'double');
    imsize = size(im);
    %im = imcrop(im,[imsize(1)*cf imsize(2)*cf imsize(1)*(1-cf) imsize(2)*(1-cf)]);
    [tile,t] = homwarp(inv(Hom{ids, ids+1}), im, 'full');
    composite = ipaste(composite, tile, t + [5000 5000], 'mean');
end
cd ..
imshow(composite)
toc

