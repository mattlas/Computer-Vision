%% Hello, this is the project's MATLAB file!
% Load in tools that we need

% SIFT method
run('vlfeat-0.9.20/toolbox/vl_setup')

% Niclas' Bundle Adjustment toolkit
run('dbat/code/dbatSetup')
addpath machinevision-toolbox-matlab-master/
addpath toolbox-common-matlab-master/

%% Setup work

% First, we take care of the geotags
clear all
close all

% Read in the only txt file in the folder.
txt = dir('*.txt');

% Limit based on max. height
heightlimit = 0.5;

% Convert it to matlab text.
tags = readtable(txt.name, 'HeaderLines', 1);
pos = table2array(tags(:,2:4));

%% Taking out ascend/descend images
maxheight = max(pos(:,3));
up = pos(:, 3) >  maxheight * heightlimit;
RatioOfUp = sum(up)/size(pos,1);

% Narrow arrays to the up parts
imageIDs = tags(up,1);
pos = pos(up,:);
first = 37;
last = 332;


%% Scatter plot of the drone positions in XY plane
figure(1)
subplot(2,1,1)
scatter(pos(:,1)', pos(:,2)', [],  pos(:, 3)/maxheight)
axis equal
hold on
drawnow;
pause(10);

%% Distance matrix

% http://stackoverflow.com/questions/19360047/how-to-build-a-distance-matrix-without-loop
% A. Donda's solution
% This x only contains the up positions
x = [pos(:,1)'; pos(:,2)'];
IP = x' * x;
d = sqrt(bsxfun(@plus, diag(IP), diag(IP)') - 2 * IP);

% Extracting subdiagonal to get average distance, 0.8 is arbitrary
neighborlimit = mean(diag(d,1)) * 2.0;

%% Taking out turning images
str = [diag(d,1);1] > mean(diag(d,1))*0.8;

% Narrow arrays to the straight parts
pos = pos(str,:);
imageIDs = imageIDs(str,1);
d = d(str,str);

d = sparse(((d+eye(size(d,2))) < neighborlimit));
AverageOfNeighbor = mean(sum(d));

%% Spy of distance matrix
subplot(2,1,2)
spy(d)


%% User input of first ID
subplot(2,1,1)
scatter(pos(:,1)', pos(:,2)', 200, 'r', 'filled')
hold on
a = [1:size(pos,1)]'; b = num2str(a); c = cellstr(b);
dy = -0.0001; dx = -0.0001; % displacement so the text does not overlay the data points
text(pos(:,1)+dx, pos(:,2)+dy, c,'FontSize',8);

promptfirst =  'What is the first position? ';
promptlast =  'What is the last position? ';

% first = input(promptfirst);
% last = input(promptlast);


first = 37;
last = 332;

% Narrow arrays to the straight parts
pos = pos(first:last,:);
imageIDs = imageIDs(first:last,1);
d = d(first:last,first:last);

subplot(2,1,1)
scatter(pos(:,1)', pos(:,2)', 200, 'b', 'filled')
drawnow;
pause(3)
    
%% Scatter plot of all the straight drone positions in XY plane for all of the images

for i = 1:size(imageIDs,1)
    cla(subplot(2,1,1))
    subplot(2,1,1)
    scatter(pos(:,1)', pos(:,2)', 200,  pos(:, 3)/maxheight)
    axis equal
    hold on
    testpoint = i;
    testneighbors = find(d(:,testpoint));
    scatter(pos(testpoint,1)', pos(testpoint,2)',600, 'b','x')
    scatter(pos(testneighbors,1)', pos(testneighbors,2)', 200, 'r', 'filled')
    pause(0.02)
end
    cla(subplot(2,1,1))
    subplot(2,1,1)
    scatter(pos(:,1)', pos(:,2)', 200,  pos(:, 3)/maxheight)
    axis equal
    hold on


%% WGS84 to XYZ
pos = wgs2xyz(pos);
pos = pos - pos(1, :);


%% SIFT / SURF of each image
cd resized
for ids=1:6
    im = imread(char(imageIDs{ids,1}));
    im = im2single(im);
    img = rgb2gray(im);
    numFeatures = 0;
    octaveThreshold = 2;
    while numFeatures < 800
        [F{ids}, D{ids}] = vl_sift(img, 'FirstOctave', octaveThreshold, 'Magnif', 2, 'Levels', 5);
        numFeatures = size(F{ids},2);
        octaveThreshold = octaveThreshold -1;
    end
subplot(2,1,2)
imshow(im);
    perm = randperm(size(F{ids},2)) ;
sel = perm(1:70) ;
h1 = vl_plotframe(F{ids}(:,sel)) ;
h2 = vl_plotframe(F{ids}(:,sel)) ;
set(h1,'color','k','linewidth',3) ;
set(h2,'color','y','linewidth',2) ;
pause(0.5)
end
cd ..


%% Image comparison


cd resized
for ID = 1:5
    %           Final
% 	neighbors = find(d(:,ID));
        
%               TEST
    neighbors = ID+1;

    
    
    for secID = neighbors
        
        numMatches = 0;
        matchThreshold = 2.1;
        while numMatches < 100
            [matches, scores] = vl_ubcmatch(D{ID},D{secID},matchThreshold);
            numMatches = size(matches,2) ;
            matchThreshold = matchThreshold -0.2;
        end
        
        X1 = F{ID}(1:2,matches(1,:)) ; X1(3,:) = 1 ;
        X2 = F{secID}(1:2,matches(2,:)) ; X2(3,:) = 1 ;
        Match{ID,secID} = X1;
        
        % --------------------------------------------------------------------
        %                                         RANSAC with homography model
        % --------------------2------------------------------------------------
        
        clear H score ok ;
        for t = 1:1000
            % estimate homograpyh
            subset = vl_colsubset(1:numMatches, 4) ;
            A = [] ;
            for i = subset
                A = cat(1, A, kron(X1(:,i)', vl_hat(X2(:,i)))) ;
            end
            [U,S,V] = svd(A) ;
            H{t} = reshape(V(:,9),3,3);
            
            % score homography
            X2_ = H{t} * X1 ;
            du = X2_(1,:)./X2_(3,:) - X2(1,:)./X2(3,:) ;
            dv = X2_(2,:)./X2_(3,:) - X2(2,:)./X2(3,:) ;
            ok{t} = (du.*du + dv.*dv) < 6*6 ;
            score(t) = sum(ok{t}) ;
        end
        
        [score, best] = max(score) ;
        H = H{best} ;
        Homtest{1} = eye(3);
        Homtest{secID} = Homtest{secID-1} * H ;
        W(ID,secID) = score;
        ok = ok{best} ;
        
    end
end

cd ..

%% MOSAIC

cd resized
% Crop Factor
subplot(2,1,2);
cf = 0.3;
composite = nan(2000,1700);
for ids=1:6
    im = iread(char(imageIDs{ids,1}), 'double');
    imsize = size(im);
    im = imcrop(im,[imsize(1)*cf imsize(2)*cf imsize(1)*(1-cf) imsize(2)*(1-cf)]);
    [tile,t] = homwarp(inv(Homtest{ids}), im, 'full');
    composite = ipaste(composite, tile, t + [500 200], 'set');
end
cd ..
imshow(composite)
drawnow;















































%% Timing part
tic
disp('Setting up data'); 

% First, we take care of the geotags
clear all

% Read in the only txt file in the folder.
txt = dir('*.txt');

% Limit based on max. height
heightlimit = 0.98;

% Convert it to matlab text.
tags = readtable(txt.name, 'HeaderLines', 1);
pos = table2array(tags(:,2:4));

%% Taking out ascend/descend images
maxheight = max(pos(:,3));
up = pos(:, 3) >  maxheight * heightlimit;
RatioOfUp = sum(up)/size(pos,1);

% Narrow arrays to the up parts
imageIDs = tags(up,1);
pos = pos(up,:);

%% Distance matrix

% http://stackoverflow.com/questions/19360047/how-to-build-a-distance-matrix-without-loop
% A. Donda's solution
% This x only contains the up positions
x = [pos(:,1)'; pos(:,2)'];
IP = x' * x;
d = sqrt(bsxfun(@plus, diag(IP), diag(IP)') - 2 * IP);

% Extracting subdiagonal to get average distance, 0.8 is arbitrary
neighborlimit = mean(diag(d,1)) * 1.4;

%% Taking out turning images
str = [diag(d,1);1] > mean(diag(d,1))*0.8;

% Narrow arrays to the straight parts
pos = pos(str,:);
imageIDs = imageIDs(str,1);
d = d(str,str);

d = sparse(((d+eye(size(d,2))) < neighborlimit));
AverageOfNeighbor = mean(sum(d));
toc

%% User input of first ID
first = 1;
last = 284;

% Narrow arrays to the straight parts
pos = pos(first:last,:);
imageIDs = imageIDs(first:last,1);
d = d(first:last,first:last);

pos = wgs2xyz(pos);

%% SIFT / SURF of each image

disp(['Using ' num2str(size(imageIDs, 1)) ' images instead of the original ' num2str(size(tags,1))]); 
tic
disp(['SIFT for ' num2str(size(imageIDs, 1)) ' images']); 
cd resized
for ids=1:size(imageIDs,1)
    im = imread(char(imageIDs{ids,1}));
    im = im2single(im);
    img = rgb2gray(im);
    numFeatures = 0;
    octaveThreshold = 3;
    while numFeatures < 800
        [F{ids}, D{ids}] = vl_sift(img, 'FirstOctave', octaveThreshold, 'Magnif', 2, 'Levels', 5);
        numFeatures = size(F{ids},2);
        octaveThreshold = octaveThreshold -1;
    end
end
cd ..
toc


%% Image comparison

tic
disp(['RANSAC for ' num2str(ceil(size(imageIDs, 1)*AverageOfNeighbor)) ' image pairs']); 

cd resized
for ID = 1:size(imageIDs,1)-1
    neighbors = ID+1;  
    for secID = neighbors'
        numMatches = 0;
        matchThreshold = 1.7;
        while numMatches < 100
            [matches, scores] = vl_ubcmatch(D{ID},D{secID},matchThreshold);
            numMatches = size(matches,2) ;
            matchThreshold = matchThreshold -0.2;
        end
        
        X1 = F{ID}(1:2,matches(1,:)) ; X1(3,:) = 1 ;
        X2 = F{secID}(1:2,matches(2,:)) ; X2(3,:) = 1 ;
        Match{ID,secID} = X1;
        
        % --------------------------------------------------------------------
        %                                         RANSAC with homography model
        % --------------------------------------------------------------------
        
        clear H score ok ;
        for t = 1:500
            % estimate homograpyh
            subset = vl_colsubset(1:numMatches, 4) ;
            A = [] ;
            for i = subset
                A = cat(1, A, kron(X1(:,i)', vl_hat(X2(:,i)))) ;
            end
            [U,S,V] = svd(A) ;
            H{t} = reshape(V(:,9),3,3);
            
            % score homography
            X2_ = H{t} * X1 ;
            du = X2_(1,:)./X2_(3,:) - X2(1,:)./X2(3,:) ;
            dv = X2_(2,:)./X2_(3,:) - X2(2,:)./X2(3,:) ;
            ok{t} = (du.*du + dv.*dv) < 4*4 ;
            score(t) = sum(ok{t}) ;
        end
        
        [score, best] = max(score) ;
        H = H{best} ;
        Hom{ID,secID} = H;
        W(ID,secID) = score;
        ok = ok{best} ;
    end
end

cd ..
toc


%% MOSAIC
tic
disp(['Creating (incorrect) mosaic for ' num2str(size(imageIDs, 1)) ' images']); 


cd resized
% Crop Factor
cf = 0.25;
Homtest = eye(3);
composite = nan(20000,20000);
for ids=1:size(imageIDs,1)
    im = iread(char(imageIDs{ids,1}), 'double');
    imsize = size(im);
    im = imcrop(im,[imsize(1)*cf imsize(2)*cf imsize(1)*(1-cf) imsize(2)*(1-cf)]);
    [tile,t] = homwarp(inv(Homtest), im, 'full');
    composite = ipaste(composite, tile, t + [500 200], 'set');
end
cd ..
toc
