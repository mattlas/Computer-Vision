%% Hello, this is the project's MATLAB file!
% Load in tools that we need

% SIFT method
run('vlfeat-0.9.20/toolbox/vl_setup')

% 
run('dbat/code/dbatSetup')
addpath machinevision-toolbox-matlab-master/
addpath toolbox-common-matlab-master/

%% Setup work
tic

% First, we take care of the geotags
clear all
close all

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


%% Scatter plot of the drone positions in XY plane
% figure(1)
% scatter(pos(:,1)', pos(:,2)', [],  pos(:, 3)/maxheight)
% axis equal
% hold on

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

%% Spy of distance matrix
figure(2)
spy(d)
drawnow;


%% Scatter plot of the straight drone positions in XY plane
% figure(3)
% scatter(pos(:,1)', pos(:,2)', 200,  pos(:, 3)/maxheight)
% axis equal
% hold on
% testpoint = randi(size(imageIDs,1));
% testneighbors = find(d(:,testpoint));
% scatter(pos(testpoint,1)', pos(testpoint,2)',600, 'b','x')
% scatter(pos(testneighbors,1)', pos(testneighbors,2)', 200, 'r', 'filled')


%% Scatter plot of all the straight drone positions in XY plane
for i = 1:size(imageIDs,1)
clf
figure(3)
scatter(pos(:,1)', pos(:,2)', 200,  pos(:, 3)/maxheight)
axis equal
hold on
testpoint = i;
testneighbors = find(d(:,testpoint));
scatter(pos(testpoint,1)', pos(testpoint,2)',600, 'b','x')
scatter(pos(testneighbors,1)', pos(testneighbors,2)', 200, 'r', 'filled')
pause(2)
end

  
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
% 
% % Narrow arrays to the straight parts
% pos = pos(first:last,:);
% imageIDs = imageIDs(first:last,1);
% d = d(first:last,first:last);
% 
% scatter(pos(:,1)', pos(:,2)', 200, 'b', 'filled')
% drawnow;

toc

%% SIFT / SURF of each image
tic
cd resized
for ids=1:30%size(imageIDs)
    im = imread(char(imageIDs{ids,1}));
    im = im2single(im);
    img = rgb2gray(im);
    numFeatures = 0;
    octaveThreshold = 1;
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

cd resized
for ID = 1:29%size(imageIDs)-1
    %           Final
%         neighbors = find(d(ID:end,ID))+ID-1;
    
    
    %           TESTING WITH IMAGES
    neighbors = ID+1;
%         im1 = imread(char(imageIDs{ID,1}));
%         im1 = im2single(im1);
%         im1g = im1;
    
    
    
    for secID = neighbors'
%         im2 = imread(char(imageIDs{secID,1}));
%         im2 = im2single(im2);
%         im2g = im2;
        
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
        Homtest{1} = [1 0 0; 0 1 0; 0 0 1];
        Homtest{secID} = Homtest{secID-1} * H;
        Hom{ID,secID} = H;
        W(ID,secID) = score;
        ok = ok{best} ;
        
        % --------------------------------------------------------------------
        %                                                  Optional refinement
        % --------------------------------------------------------------------
        
%         u = H(1) * X1(1,ok) + H(4) * X1(2,ok) + H(7) ;
%         v = H(2) * X1(1,ok) + H(5) * X1(2,ok) + H(8) ;
%         d = H(3) * X1(1,ok) + H(6) * X1(2,ok) + 1 ;
%         du = X2(1,ok) - u ./ d ;
%         dv = X2(2,ok) - v ./ d ;
%         err = sum(du.*du + dv.*dv) ;
%         
%         H = H / H(3,3) ;
%         opts = optimset('Display', 'none', 'TolFun', 1e-8, 'TolX', 1e-8) ;
%         H(1:8) = fminsearch(@residual, H(1:8)', opts) ;
        
        % --------------------------------------------------------------------
        %                                                         Show matches
        % --------------------------------------------------------------------
        
%         dh1 = max(size(im2g,1)-size(im1g,1),0) ;
%         dh2 = max(size(im1g,1)-size(im2g,1),0) ;
%         
%         figure(1) ; clf ;
%         subplot(2,1,1) ;
%         imagesc([padarray(im1g,dh1,'post') padarray(im2g,dh2,'post')]) ;
%         o = size(im1g,2) ;
%         line([F{ID}(1,matches(1,:));F{secID}(1,matches(2,:))+o], ...
%             [F{ID}(2,matches(1,:));F{secID}(2,matches(2,:))]) ;
%         title(sprintf('%d tentative matches', numMatches)) ;
%         axis image off ;
%         
%         subplot(2,1,2) ;
%         imagesc([padarray(im1g,dh1,'post') padarray(im2g,dh2,'post')]) ;
%         o = size(im1,2) ;
%         line([F{ID}(1,matches(1,ok));F{secID}(1,matches(2,ok))+o], ...
%             [F{ID}(2,matches(1,ok));F{secID}(2,matches(2,ok))]) ;
%         title(sprintf('%d (%.2f%%) inliner matches out of %d', ...
%             sum(ok), ...
%             100*sum(ok)/numMatches, ...
%             numMatches)) ;
%         axis image off ;
%         
%         drawnow ;
    end
end

cd ..
toc


%% MOSAIC

tic
cd resized
figure(5)
clf(5)
composite = nan(10000,10000);
for ids=1:29%size(imageIDs)
    im = iread(char(imageIDs{ids,1}), 'double');
    imsize = size(im);
    %im = imcrop(im,[imsize(1)*(1/4) imsize(2)*(1/4) imsize(1)*(3/4) imsize(2)*(3/4)]);
    [tile,t] = homwarp(inv(Homtest{ids}), im, 'full');
    composite = ipaste(composite, tile, t + [5000 5000], 'mean');
%     
% imshow(composite)
end
cd ..
imshow(composite)
toc


%% Bundle adjustment

% 
% damping = 'gna';doPause='off';
% 
% % Extract name of current directory.
% curDir=fileparts(mfilename('fullpath'));
% 
% % Base dir with input files.
% inputDir=fullfile(curDir,'data');
% 
% % PhotoModeler text export file and report file.
% inputFile=fullfile(inputDir,'roma-pmexport.txt');
% % Report file name.
% reportFile=fullfile(inputDir,'roma-dbatreport.txt');
% 
% fprintf('Loading data file %s...',inputFile);
% prob=loadpm(inputFile);
% probRaw=prob;
% if any(isnan(cat(2,prob.images.imSz)))
%     error('Image sizes unknown!');
% end
% disp('done.')
% 
% % Convert loaded PhotoModeler data to DBAT struct.
% s0=prob2dbatstruct(prob);
% ss0=s0;
% 
% % Don't estimate IO data, i.e. treat it as exact.  This block is not
% % really necessary, but may serve as a starting point if IO
% % parameters are to be estimated.
% s0.IO=s0.prior.IO;
% s0.estIO=false(size(s0.IO));
% s0.useIOobs=false(size(s0.IO));
% 
% % Noise sigma [m].
% noiseLevel=0.1;
% 
% % Reset random number generator.
% rng('defapm file ult');
% 
% % Use supplied EO data as initial values. Again, this block is not
% % really necessary but may serve as a starting point for modifications.
% s0.EO=s0.prior.EO;
% s0.estEO(1:6,:)=true; % 7th element is just the axis convention.
% s0.useEOobs=false(size(s0.EO));
% s0.EO(1:3,:)=s0.EO(1:3,:)+randn(3,size(s0.EO,2))*noiseLevel;
% 
% % Copy CP values and treat them as fixed.
% s0.OP(:,s0.isCtrl)=s0.prior.OP(:,s0.isCtrl);
% s0.estOP=repmat(~s0.isCtrl(:)',3,1);
% s0.useOPobs=repmat(s0.isCtrl(:)',3,1);
% % Compute initial OP values by forward intersection.
% correctedPt=reshape(pm_multilenscorr1(diag([1,-1])*s0.markPts,s0.IO,3,2,...
%                                       s0.ptCams,size(s0.IO,2)),2,[]);
% s0.OP(:,~s0.isCtrl)=pm_multiforwintersect(s0.IO,s0.EO,s0.cams,s0.colPos,correctedPt,find(~s0.isCtrl));
% 
% % Warn for non-uniform mark std.
% uniqueSigmas=unique(s0.markStd(:));
% 
% if length(uniqueSigmas)~=1
%     uniqueSigmas
%     error('Multiple mark point sigmas')
% end
% 
% if all(uniqueSigmas==0)
%     warning('All mark point sigmas==0. Using sigma==1 instead.');
%     s0.prior.sigmas=1;
%     s0.markStd(:)=1;
% end
% 
% % Fix the datum by fixing camera 1...
% s0.estEO(:,1)=false;
% % ...and the largest other absolute camera coordinate.
% camDiff=abs(s0.EO(1:3,:)-repmat(s0.EO(1:3,1),1,size(s0.EO,2)));
% [i,j]=find(camDiff==max(camDiff(:)));
% s0.estEO(i,j)=false;
% 
% fprintf('Running the bundle with damping %s...\n',damping);
% 
% % Run the bundle.
% tic
% [result,ok,iters,sigma0,E]=bundle(s0,damping,'trace');
% toc   
% 
% if ok
%     fprintf('Bundle ok after %d iterations with sigma0=%.2f (%.2f pixels)\n',...
%             iters,sigma0,sigma0*s0.prior.sigmas(1));
% else
%     fprintf(['Bundle failed after %d iterations. Last sigma0 estimate=%.2f ' ...
%              '(%.2f pixels)\n'],iters,sigma0,sigma0*s0.prior.sigmas(1));
% end
% 
% COP=bundle_result_file(result,E,reportFile);
% 
% fprintf('\nBundle result file %s generated.\n',reportFile);
% 
% % Don't plot iteration history for the 26000+ object points.
% h=plotparams(result,E,'noop');
% 
% h=plotcoverage(result,true);
% 
% h=plotimagestats(result,E);
% 
% h=plotopstats(result,E,COP);
% 
% fig=tagfigure(sprintf('damping=%s',damping));
% fprintf('Displaying bundle iteration playback for method %s in figure %d.\n',...
%         E.damping.name,double(fig));H
% plotnetwork(resultpm file ,E,'trans','up','align',1,'title',...
%             ['Damping: ',damping,'. Iteration %d of %d'], ...
%             'axes',fig,'pause',doPause);
% 
% imName='';
% imNo=1;
% % Check if image files exist.
% if exist(s0.imDir,'dir')
%     % Handle both original-case and lower-case file names.
%     imNames={s0.imNames{imNo},lower(s0.imNames{imNo}),upper(s0.imNames{imNo})};    
%     imNames=fullfile(s0.imDir,imNames);
%     imExist=cellfun(@(x)exist(x,'file')==2,imNames);
%     if any(imExist)
%         imName=imNames{find(imExist,1,'first')};
%     end
% else
%     warning('Image directory %s does not exist.',s0.imDir);
% end
% 
% if exist(imName,'file')
%     fprintf('Plotting measurements on image %d.\n',imNo);
%     imFig=tagfigure('image');
%     h=[h;imshow(imName,'parent',gca(imFig))];
%     pts=s0.markPts(:,s0.colPos(s0.vis(:,imNo),imNo));
%     ptsId=s0.OPid(s0.vis(:,imNo));
%     isCtrl=s0.isCtrl(s0.vis(:,imNo));
%     % Plot non-control points as red crosses.
%     if any(~isCtrl)
%         line(pts(1,~isCtrl),pts(2,~isCtrl),'marker','x','color','r',...
%              'linestyle','none','parent',gca(imFig));
%     end
%     % Plot control points as black-yellow triangles.
%     if any(isCtrl)
%         line(pts(1,isCtrl),pts(2,isCtrl),'marker','^','color','k',...
%              'markersize',2,'linestyle','none','parent',gca(imFig));
%         line(pts(1,isCtrl),pts(2,isCtrl),'marker','^','color','y',...
%              'markersize',6,'linestyle','none','parent',gca(imFig));
%     end
%     for i=1:length(ptsId)
%         text(pts(1,i),pts(2,i),int2str(ptsId(i)),'horizontal','center',...
%              'vertical','bottom','color','b','parent',gca(imFig));
%     end
% end
