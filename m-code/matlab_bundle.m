% Script testing matlabs built in functions for feature extraction
% feature matching and bundle adjustment to extract 3D points from images. 

% Load all images from folder
imds = imageDatastore(fullfile('test_data'), 'FileExtensions', '.jpg');

% Convert to grayscale
images = cell(1, numel(imds.Files));
for i = 1:numel(imds.Files)
    I = readimage(imds, i);
    images{i} = rgb2gray(I);
end

% Sets camera parameters
% Focal length is 30mm
% We think thare are 72 pixels per mm...
% Sets principal point to middle of image.
focalLength = [30*72, 30*72];
principalPoint = size(images{1}) / 2;
intrinsicMatrix = [focalLength(1)    0                 0;
                   0                 focalLength(2)    0;
                   principalPoint(1) principalPoint(2) 1];
   
cameraParams = cameraParameters('ImageSize', size(images{1}), ...
    'IntrinsicMatrix', intrinsicMatrix);

% Extract features from first image
I = images{1};
prevPoints = detectSURFFeatures(I, 'NumOctaves', 8);
prevFeatures = extractFeatures(I, prevPoints);

% Creates a view set
vSet = viewSet;
viewId = 1;
vSet = addView(vSet, viewId, 'Point', prevPoints, ...
    'Orientation', eye(3, 'like', prevPoints.Location), ...
    'Location', zeros(1,3, 'like', prevPoints.Location));

for i = 2:numel(images)
   I = images{i};
   
   % Extract features
   fprintf('Extracting features image %d \n', i);
   tic; 
   currPoints = detectSURFFeatures(I, 'NumOctaves', 8);
   currFeatures = extractFeatures(I, currPoints);
   toc;
   
   % Match features
   fprintf('Matching features of images %d and %d \n', i-1, i);
   tic;
   indexPairs = matchFeatures(prevFeatures, currFeatures, ...
       'MaxRatio', 0.6, 'Unique', true);
   toc;
   
   % Matched points
   matchedPoints1 = prevPoints(indexPairs(:, 1));
   matchedPoints2 = currPoints(indexPairs(:, 2));
   
   % Estimate relative poses
   fprintf('Estimating relative pose between images %d and %d \n', i-1, i);
   tic;
   [relativeOrient, relativeLoc, inlierIdx] = helperEstimateRelativePose(...
       matchedPoints1, matchedPoints2, cameraParams);
   toc;
   fprintf('\n\n');
   
   % Add current points
   vSet = addView(vSet, i, 'Points', currPoints);
   
   % Add feature matches
   vSet = addConnection(vSet, i-1, i, 'Matches', indexPairs(inlierIdx,:));
   
   % Get previous pose
   prevPose = poses(vSet, i-1);
   prevOrientation = prevPose.Orientation{1};
   prevLocation = prevPose.Location{1};
   
   % Compute current pose
   orientation = relativeOrient * prevOrientation;
   location = prevLocation + relativeLoc * prevOrientation;
   vSet = updateView(vSet, i, 'Orientation', orientation, ...
       'Location', location);
   
   prevFeatures = currFeatures;
   prevPoints = currPoints;   
end

tracks = findTracks(vSet);

camPoses = poses(vSet);

fprintf('Estimating 3D points \n');
tic;
xyzPoints = triangulateMultiview(tracks, camPoses, cameraParams);
[xyzPoints, camPoses, reprojectionErrors] = bundleAdjustment(...
   xyzPoints, tracks, camPoses, cameraParams);
toc;

vSet = updateView(vSet, camPoses);

% Plot camear poses
figure;
plotCamera(camPoses, 'Size', 0.05);
hold on;

% Extract points with good reprojectionErrors
goodIdx = (reprojectionErrors < 5);
xyzPoints = xyzPoints(goodIdx, :);

% Plots good points
pcshow(xyzPoints, 'VerticalAxis', 'y', 'VerticalAxisDir', 'up', ...
    'MarkerSize', 45);
grid on;
xlabel('x-axis');
ylabel('y-axis');
zlabel('z-axis');
title('Sparse points');
hold off;

figure;
plotCamera(camPoses, 'Size', 0.05);
hold on;
x = double(xyzPoints(:,1));
y = double(xyzPoints(:,2));
z = double(xyzPoints(:,3));

tri = delaunay(x, y);
trimesh(tri, x, y, z);
grid on;
xlabel('x-axis');
ylabel('y-axis');
zlabel('z-axis');
title('Mesh');
hold off;
    


