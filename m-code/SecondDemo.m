%% Hello, this is the project's MATLAB file!
% Load in tools that we need

% SIFT method
run('vlfeat-0.9.20/toolbox/vl_setup')

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
% [imageIDs, pos, orient] = m_geotag('block04_cameraPositions.txt');
% 
% Calculate distance matrix
% [d, restr, ref] = m_distmat(pos, 3, 0.98, 0.8);

% Applying restrictions
% pos = pos( restr , : );
% imageIDs = imageIDs( restr , : );
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



% I only use 3 images with lots of neighbors
% first = 251;
% last = 290;
% 2
% 297
%
% % Narrow arrays to the straight parts
% pos = pos(first:last,:);
% imageIDs = imageIDs(first:last,1);


% pos = pos([29; 72],:);
% imageIDs = imageIDs([29; 72],:);

% scatter(pos(:,1)', pos(:,2)', 200, 'b', 'filled')
% drawnow;

%% Create list of pairs Niclas-style

% [ d, ~, ref] = m_distmat(pos, 1.8, 0, 0);
ref = 2;
imageIDs = {'Strip1.png'; 
            'Strip2.png';
            'Strip3.png'};
% pairs = m_pairify(d);
pairs = [1 2; 2 3];

% imageIDs = {'Strip1.png'; 
%             'Strip2.png';
%             'Strip3.png';
%             'Strip4.png';
%             'Strip5.png';
%             'Strip6.png'};
% % pairs = m_pairify(d);
% pairs = [1 2; 2 3 ; 3 4 ; 4 5 ; 5 6];
clear pos orient d

%% WGS84 to XYZ
% pos = m_wgs2xyz(pos);

% Changing reference to first image
% pos = pos - pos(1, :);

toc

%% SIFT / SURF of each image
firstOct = 0;
featureTh = 1000;
tic
cd resized


[F, D] = m_sift(imageIDs, firstOct, featureTh);

cd ..
toc


%% Image comparison

tic
cd resized

% For repeatability
rng('shuffle')
% rng(23)

Hom = m_ransac(imageIDs, F, D, pairs);
Hom = Hom{1};
cd ..
toc

clear F D;
%% Starting approximation for Least Squares Niclas-style

tic
% Starting approximations for Pi
n = 1:numel(imageIDs);
P=repmat(eye(3),[1,1,numel(n)]);
P0=nan(size(P));
P0(:,:,ref)=eye(3);

hasEst=ref;
done=false(size(imageIDs));
done(n==ref)=true;

% Repeat until we have estimates of all Pi
while any(isnan(P0(:)))
    % Pick all pairs with Pi estimated and Pj not estimated.
    iPairs=ismember(pairs(:,1),hasEst) & ~ismember(pairs(:,2),hasEst);
    for k=find(iPairs)'
        % Pj=Pi * inv(Hij)
        i=pairs(k,1);
        j=pairs(k,2);
        if ~ismember(j,hasEst)
            P0(:,:,j)=P0(:,:,i)*inv(Hom(:,:,k));
            hasEst(end+1)=j;
            done(n==j)=true;
        end
    end
    % Pick all pairs with Pj estimated and Pi not estimated.
    jPairs=~ismember(pairs(:,1),hasEst) & ismember(pairs(:,2),hasEst);
    for k=find(jPairs)'
        % Pi=Pj * Hij
        i=pairs(k,1);
        j=pairs(k,2);
        if ~ismember(i,hasEst)
            P0(:,:,i)=P0(:,:,j)*Hom(:,:,k);
            hasEst(end+1)=i;
            done(n==i)=true;
        end
    end
    % If above is over, set remaining and singular solutions to Identity
    %if (isempty(find(iPairs)) & isempty(find(jPairs)))
    
    %end
end


%% Least squares
% x0=reshape(P0,[],1);
% 
% [x,n,code,l,X,alphas,C,L,nus,r,J,A]=sqpsq('multihomo_f', 'multihomo_c', ...
%    x0,1e-3,1e-8,20,{Hom,pairs,ref},0.1,0.1);
% 
% P(:,:,:) = round(reshape(x,3,3,[]),8);

% Or instead:
P = P0;

clear P0
toc
%% MOSAIC

offset = [3000 3000];
tic
cd resized

[canvas, impos] = m_stitch(imageIDs, P, 'set', offset);

cd ..
toc

figure(2)
subplot(1,1,1)
imshow(canvas)
iptsetpref('ImshowBorder','tight');
imwrite(canvas, 'Strip6.png')
%hold on
%scatter(impos(:,1)', impos(:,2)', 200, 'b', 'filled')

%% Export data
tic
cd resized

% Read in the geotag text
[neighIDs, neighpos, ~] = m_geotag('block04_cameraPositions.txt');

% Calculate distance matrix
[neighd, ~] = m_4distmat(neighpos, 5.8, 0.98, 0.8);

% % Invert Ps for other team
% for i = 1:size(P,3)
%     P(:,:,i) = inv(P(:,:,i));
% end


data = m_img_list_for_android(imageIDs, impos, P, neighd, offset);

cd ..
toc
