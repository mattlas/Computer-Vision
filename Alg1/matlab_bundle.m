% Script testing matlabs built in functions for feature extraction
% feature matching and soon bundle adjument

% Load and convert images to gray scale
im1 = rgb2gray(imread('im1_750.jpg'));
im2 = rgb2gray(imread('im2_750.jpg'));

% Detect and extract features from image 1
im1points = detectSURFFeatures(im1);
[im1features, im1valid] = extractFeatures(im1, im1points);

% Detect and extract features from image 2
im2points = detectSURFFeatures(im2);
[im2features, im2valid] = extractFeatures(im2, im2points);

% Match features
indexPairs = matchFeatures(im1features, im2features);

% Extract matched points from each image
matchedpoints1 = im1valid(indexPairs(:, 1));
matchedpoints2 = im2valid(indexPairs(:, 2));

% Plot matched points
figure;
showMatchedFeatures(im1, im2, matchedpoints1, matchedpoints2, 'montage');
title('Matched points');

% Sets camera parameters
% Focal length is 30mm
% We think thare are 72 pixels per mm...
% Sets principal point to middle of image.
focalLength = [30*72, 30*72];
principalPoint = size(im1) / 2;
intrinsicMatrix = [focalLength(1)    0                 0;
                   0                 focalLength(2)    0;
                   principalPoint(1) principalPoint(2) 1];
   
cameraParams = cameraParameters('ImageSize', size(im1), ...
    'IntrinsicMatrix', intrinsicMatrix);

% Estimate relative poses
[relativeOrient, relativeLoc, inlierIdx] = helperEstimateRelativePose(...
    matchedpoints1, matchedpoints2, cameraParams);

% Creates a view set
vSet = viewSet;

% Adds point from image 1 with identity orientation and origin location
vSet = addView(vSet, 1, 'Point', im1points, 'Orientation', eye(3), ...
    'Location', zeros(1,3));

% Adds points from image 2 with estimated pose data
vSet = addView(vSet, 2, 'Point', im2points, 'Orientation', ...
    relativeOrient, 'Location', relativeLoc);

% Adds connections between points in view set
vSet = addConnection(vSet, 1, 2, 'Matches', indexPairs(inlierIdx, :));

% Finds tracks
tracks = findTracks(vSet);

% Gets camera poses 
camPoses = poses(vSet);

% Estimates xyzPoints 
xyzPoints = triangulateMultiview(tracks, camPoses, cameraParams);

% Refines camPoses and xyzPoints
[xyzPoints, camPoses, reprojectionErrors] = bundleAdjustment(xyzPoints, ...
    tracks, camPoses, cameraParams, 'FixedViewId', 1);

% Plot camear poses
figure;
plotCamera(camPoses, 'Size', 0.05);
hold on;

% Extract points with good reprojectionErrors
goodIdx = (reprojectionErrors < 5);
xyzPoints = xyzPoints(goodIdx, :);

% Plots good points
pcshow(xyzPoints, 'VerticalAxis', 'y', 'VerticalAxisDir', 'down', ...
    'MarkerSize', 45);
grid on;
hold off;