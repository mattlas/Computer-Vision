%% Hello, this is the project's MATLAB file!
% Load in tools that we need

% SIFT method
run('vlfeat-0.9.20/toolbox/vl_setup')

% Niclas' Bundle Adjustment toolkit
run('dbat/code/dbatSetup')


addpath machinevision-toolbox-matlab-master/
addpath toolbox-common-matlab-master/
addpath ../hello/
addpath lsmethod/

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
last = 5;



% Narrow arrays to the straight parts
pos = pos(first:last,:);
imageIDs = imageIDs(first:last,1);
d = d(first:last,first:last);
% scatter(pos(:,1)', pos(:,2)', 200, 'b', 'filled')
% drawnow;


%% WGS84 to XYZ
% pos = m_wgs2xyz(pos);

% Changing reference to first image
% pos = pos - pos(1, :);

toc

%% SIFT / SURF of each image
firstOct = 3;
featureTh = 2000;
tic
cd resized


[F, D] = m_sift(imageIDs, firstOct, featureTh);

cd ..
toc


%% Image comparison

tic
cd resized

Hom = m_ransac(imageIDs, F, D, d);
Hom = Hom{1};
cd ..
toc


%% Least Squares

tic


[row, col] = find(~cellfun(@isempty,Hom));
p0 = zeros(9*size(row,1),1);
for id = 1:size(row,1)
    p0(id*9-8:id*9,1) = reshape(Hom{row(id),col(id)},9,1);
end



convTol = 1e-8; % Convergence tolerance ...
maxIter = 1000; % Maximum iteration ...
J=1; p=1; r=0; % Initial data so that it'll start ...
params = {p0, row, col}; nu0=0.1; epsC=1e-4; mu=1e-3;
L=eye(size(p0,2)*2);

% Method for constrained problems
x0 = repmat(reshape(eye(3),[],1),size(imageIDs, 1),1);
[x,nItr,code,l,X,a,C,L,nus,r,J,A]=sqpsq(@resid,@const,x0,convTol,epsC,maxIter,params,nu0,mu,L);



%% MOSAIC

% Test
for ids = 1:size(imageIDs)
    P{ids} = round(reshape(x(ids*9-8:ids*9),3,3),3);
end
toc

tic
cd resized

[canvas, impos] = m_stitch(imageIDs, P, 'mean');

cd ..
toc

imshow(canvas)
hold on
scatter(impos(:,1)'+1000, impos(:,2)'+1000, 200, 'b', 'filled')

%% Export data
tic
cd resized
% 
% % Read in the geotag text
% [neighIDs, neighpos, ~] = m_geotag('block04_cameraPositions.txt');
% 
% % Calculate distance matrix
% [neighd, ~] = m_4distmat(neighpos, 5.8, 0.98, 0.8);
% 
% for ids = 1:size(neighIDs)
%     P{ids} = round(inv(eye(3)),3);
% end
% 
% figure(2)
% scatter(neighpos(:,1)', neighpos(:,2)')
% axis equal
% hold on
% testpoint = randi(size(neighIDs,1));
% testneighbors = find(d(:,testpoint));
% scatter(neighpos(testpoint,1)', neighpos(testpoint,2)',600, 'b','x')
% scatter(neighpos(testneighbors,1)', neighpos(testneighbors,2)', 200, 'r', 'filled')


data = m_img_list_for_android(imageIDs, impos, P, d);

cd ..
toc
