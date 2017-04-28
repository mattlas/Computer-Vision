function [ Hom ] = m_ransac(imageIDs, F, D, d)
Hom = cell(size(imageIDs, 1), size(imageIDs, 1));
for ID = 1:size(imageIDs)-1
    %           Final
    % 	neighbors = find(d(ID:end,ID))+ID-1;
    
    %               TEST
    if (ID < 2)
        neighbors = ID+1;
    else
        neighbors = [ID+1; ID-1];
    end
    
       
    
    
    
    for secID = neighbors'
        numMatches = 0;
        matchThreshold = 1.2;
        %         while numMatches < 100
        [matches, scores] = vl_ubcmatch(D{ID},D{secID},matchThreshold);
        numMatches = size(matches,2) ;
        matchThreshold = matchThreshold -0.2;
        %         end
        
        X1 = F{ID}(1:2,matches(1,:)) ; X1(3,:) = 1 ;
        X2 = F{secID}(1:2,matches(2,:)) ; X2(3,:) = 1 ;
        Match{ID,secID} = X1;
        
        % --------------------------------------------------------------------
        %                                         RANSAC with homography model
        % --------------------------------------------------------------------
        
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
        H = H{best};
        H = inv(H);
        H = H / H (3,3);
        Hm{ secID , ID } = H;
        W(ID,secID) = score;
        ok = ok{best} ;
        
        % --------------------------------------------------------------------
        %                                                         Show matches
        % --------------------------------------------------------------------
        
%         
%         im1 = imread(char(imageIDs{ID,1}));
%         im1 = im2single(im1);
%         im1g = im1;
%         im2 = imread(char(imageIDs{secID,1}));
%         im2 = im2single(im2);
%         im2g = im2;
%         
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
Hom = {Hm, W};
end