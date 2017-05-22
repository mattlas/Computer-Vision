function [ Hom ] = m_ransac(imageIDs, F, D, pairs)
Hm = zeros(3,3,size(pairs,1));
W = zeros(size(pairs,1), 1);

for iter = 1:size(pairs,1)
    i = pairs(iter,1);
    j = pairs(iter,2);
    
    numMatches = 0;
    matchThreshold = 1.5;
%     while numMatches < 200
        [matches, scores] = vl_ubcmatch(D{i},D{j},matchThreshold);
        numMatches = size(matches,2) ;
        matchThreshold = matchThreshold -0.2;
        scores = scores./(max(scores));
%     end
    
    Pi = F{i}(1:2,matches(1,:)) ;
    Pj = F{j}(1:2,matches(2,:)) ;
    scalei = F{i}(3,matches(1,:));
    scalej = F{j}(3,matches(2,:));
    % Normalize the angles to be between -pi and pi
    thetai = wrapToPi(F{i}(4,matches(1,:)));
    thetaj = wrapToPi(F{j}(4,matches(2,:)));
    hPi = homogeneous(Pi) ;
    hPj = homogeneous(Pj) ;
    
    n = size(Pi,2);
    
    % --------------------------------------------------------------------
    %                                         RANSAC with rigidbody model
    % -----------------------------4---------------------------------------
    
    clear H score ok ;
    % Initial homography
    H = eye(3);
    % Iteration counter
    ii=0;
    % Threshold for transform matches and inlier ratio
    t = 10*10;
    ts =  0.2;
    ta = 0.2;
    pp=0.99;
    % Initial consensus set size
    CS_0=0;
    N = 10000;
    
    
    % Fixed for loop
%     for ii = 1 : 5000
        
        % Adaptive RANSAC
            while ii<N
        % Choosing suitable random points
        rand_ = randi(size(Pi,2),[1 2]);
        ran1 = rand_(1);
        ran2 = rand_(2);
        % The first random p point
        p1=Pi(:,ran1)';
        % The second random p point
        p2=Pi(:,ran2)';
        % The first random q point
        q1=Pj(:,ran1)';
        % The second random q point
        q2=Pj(:,ran2)';
        % Calculating the translated points for all the points
        [ ~, ~, S, R, T, ~, ~, ~, theta] = procrustes([p1',p2'], [q1',q2'], 1);
        
        % Previous version
        % [R, T] = rigidalign( [p1',p2'], [q1',q2'] );
        % p_translated = R*[p1',p2']
        H(1:2,1:2) = S * R;
        H(1:2,3) = T;
        HX2_ = H * hPi ;
        du = HX2_(1,:)./HX2_(3,:) - Pj(1,:);
        dv = HX2_(2,:)./HX2_(3,:) - Pj(2,:);
        
        % First condition, for distances
        % Second condition, for scale
        %         Third condition for angle
        
        
                ok = (du.*du + dv.*dv) < t;
%         ok = (du.*du + dv.*dv) < t & ...
%             abs ( 1 - (S * scalei ./ scalej) ) <ts & ...
%             abs(angdiff(theta+thetai, thetaj)) < ta;
%         
        % Introducing weights
%         ok = ok .* scores;
        % Size of consensus set
        CS_n=sum(ok);
        if CS_n > CS_0
            % Updating size of consensus set
            CS_0 = CS_n;
            % Updating best homography FROM i TO j
            Hm(:,:,iter) = H;
            best = ok;
            %             W(iter, 1) = CS_n;
            W(iter, 1) = sum(best>0);
            N_n=log10(1-pp)/log10(1-(CS_n/n)^2);
            N = N_n;
            %             pairs(iter,1) / pairs(end, 1)
            %             [   iter; ...
            %                 sum((du.*du + dv.*dv) < t); ...
            %                 sum(abs ( 1 - (S * scalei ./ scalej) ) < 0.05); ...
            %                 sum(abs ( 2*pi - wrapTo2Pi((theta+thetai) - thetaj)) < 0.2)]
        end
        % Updating maximum number of iterations
        if ( CS_n == 0 )
            CS_n = 1;
        end
        
        % Updating iteration
        ii=ii+1;
        
    end
    
    
    % --------------------------------------------------------------------
    %                                                         Show matches
    % --------------------------------------------------------------------
%     im1 = imread(char(imageIDs{i,1}));
%     im1 = im2single(im1);
%     im1g = im1;
%     im2 = imread(char(imageIDs{j,1}));
%     im2 = im2single(im2);
%     im2g = im2;
%     
%     dh1 = max(size(im2g,1)-size(im1g,1),0) ;
%     dh2 = max(size(im1g,1)-size(im2g,1),0) ;
%     
%     figure(1) ; clf ;
%     subplot(2,1,1) ;
%     imagesc([padarray(im1g,dh1,'post') padarray(im2g,dh2,'post')]) ;
%     o = size(im1g,2) ;
%     line([F{i}(1,matches(1,:));F{j}(1,matches(2,:))+o], ...
%         [F{i}(2,matches(1,:));F{j}(2,matches(2,:))]) ;
%     title(sprintf('%d tentative matches with pairs: %d %d', numMatches, pairs(iter, 1), pairs(iter, 2))) ;
%     axis image off ;
%     
%     subplot(2,1,2) ;
%     imagesc([padarray(im1g,dh1,'post') padarray(im2g,dh2,'post')]) ;
%     o = size(im1,2) ;
%     line([F{i}(1,matches(1,best > 0));F{j}(1,matches(2,best > 0))+o], ...
%         [F{i}(2,matches(1,best > 0));F{j}(2,matches(2,best > 0))]) ;
%     title(sprintf('%d (%.2f%%) inliner matches out of %d', ...
%         sum(best > 0), ...
%         100*sum(best > 0)/numMatches, ...
%         numMatches)) ;
%     axis image off ;
%     drawnow ;
%     pause(0.5)
    
    
end
W = W .* (W > 10);
Hom = {Hm, W};
end