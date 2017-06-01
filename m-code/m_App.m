% function m_App(im_path)
%% Hello, this is the project's MATLAB file!
% Load in tools that we need

% SIFT method
run('vlfeat-0.9.20/toolbox/vl_setup')

addpath machinevision-toolbox-matlab-master/
addpath toolbox-common-matlab-master/
addpath ../hello/
addpath lsmethod/

%% Setup work
% First, we take care of the geotags
clear all
close all

startRuntime = tic;

% The only input we need.
im_path = 'resized';


cd(im_path)
% Read in the geotag text
[imageIDs, pos, reference] = m_geotag;
cd ..
% Calculate distance matrix

[d, ref] = m_distmat(pos);
pairs = m_pairify(d);


%% Support plot
% for i = 200:size(imageIDs,1)
%     clf
%     figure(1)
% 
%     scatter(pos(:,1)', pos(:,2)', 200,  pos(:, 3))
%     axis equal
%     hold on
%     testpoint = i;
%     testneighbors = find(d(:,testpoint));
%     scatter(pos(testpoint,1)', pos(testpoint,2)',600, 'b','x')
%     scatter(pos(testneighbors,1)', pos(testneighbors,2)', 200, 'r', 'filled')
%     pause(0.01)
% end

%% SIFT / SURF of each image

tic

cd(im_path)


firstOct = 2;
[F, D] = m_sift(imageIDs, firstOct);

cd ..
toc


%% Image comparison

tic
cd(im_path)


rng('shuffle')
Hom = m_ransac(imageIDs, F, D, pairs);

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
done=zeros(size(imageIDs));
done(n==ref)=500;
iter = 0;
prev_hasEst = 0;

% Repeat until we have estimates of all Pi
while any(isnan(P0(:)))
    % Pick all pairs with Pi estimated and Pj not estimated.
    iPairs=ismember(pairs(:,1),hasEst) & ~ismember(pairs(:,2),hasEst);
    for k=find(iPairs)'
        % Pj=Pi * inv(Hij)
        i=pairs(k,1);
        j=pairs(k,2);
        if (~ismember(j,hasEst) || done(n==j) < Hom{2}(k))
            P0(:,:,j)=P0(:,:,i)*inv(Hom{1}(:,:,k));
            hasEst(end+1)=j;
            done(n==j)=Hom{2}(k);
        end
    end
    
    % Pick all pairs with Pj estimated and Pi not estimated.
    jPairs=~ismember(pairs(:,1),hasEst) & ismember(pairs(:,2),hasEst);
    for k=find(jPairs)'
        % Pi=Pj * Hij
        i=pairs(k,1);
        j=pairs(k,2);
        if (~ismember(i,hasEst) || done(n==i) < Hom{2}(k))
            P0(:,:,i)=P0(:,:,j)*Hom{1}(:,:,k);
            hasEst(end+1)=i;
            done(n==i)=Hom{2}(k);
        end
    end
    
    % We need a break here, if the elapsed time is too short, set up
    % identity as approximation    
    if( all( ismember( hasEst, prev_hasEst )))
        imageIDs = imageIDs(hasEst);
        P0 = P0(:,:,hasEst);
    end
    
    prev_hasEst = hasEst;
    
end


%% Least squares

% Do we need the starting approximation? (No)
% P0=repmat(eye(3),[1,1,size(imageIDs, 1)]);
% 
% 
% x0=reshape(P0,[],1);
% 
% [x,n,code,l,X,alphas,C,L,nus,r,J,A]=sqpsq('multihomo_f', 'multihomo_c', ...
%    x0,1e-3,1e-8,20,{Hom,pairs,ref},0.1,0.1);
% 
% P(:,:,:) = round(reshape(x,3,3,[]),8);
% 
% sum(sum(sum(abs(P-P0))))

% Or instead:
P = P0;

% sum(sum(sum(abs(P-P0))))
% clear P0
toc
%% MOSAIC

tic
cd(im_path)

[impos, offset] = m_stitch(imageIDs, P, 'add');

cd ..
toc


elapsedRuntime = toc(startRuntime);fprintf('\n');
fprintf('Total Runtime was: %.2f seconds', elapsedRuntime);
%% Export data
tic
cd(im_path)

% Read in the geotag text
% [neighIDs, neighpos, ~] = m_geotag;

% Calculate distance matrix
[neighd] = m_4distmat(pos);

% % Invert Ps for other team
% for i = 1:size(P,3)
%     P(:,:,i) = inv(P(:,:,i));
% end


data = m_img_list_for_android(imageIDs, impos, P, neighd, offset);

cd ..
toc

% end
