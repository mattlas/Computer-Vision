function [ F, D ] = m_sift( imageIDs, octaveThreshold)
% Pre-allocation
F = cell(size(imageIDs,1), 1);
D = cell(size(imageIDs,1), 1);
% AverageTh = 0;
% Go through all the images
for ids=1:size(imageIDs)
    % Set up threshold
    octTh = octaveThreshold;
    %Load them in
    im = imread(char(imageIDs{ids,1}));
    % Turn to single (could be modified)
    im = im2single(im);
    % Grayscale
    img = rgb2gray(im);
%     numFeatures = 0;
    [F{ids}, D{ids}] = vl_sift(img, 'FirstOctave', octTh,  'Levels', 3);
%     numFeatures = size(F{ids},2);
%     
%     % Have backup for too many solutions
%     if numFeatures < featureThreshold
%         octTh = 0;
%         [F{ids}, D{ids}] = vl_sift(img, 'FirstOctave', octTh, 'Magnif', 2, 'Levels', 5);
%     end
    
%     subplot(1,1,1)
%     imshow(img);
%     text(20, -10, num2str(size(F{ids},2)))
%     perm = randperm(size(F{ids},2)) ;
%     sel = perm(1:70) ;
%     h1 = vl_plotframe(F{ids}(:,sel)) ;
%     h2 = vl_plotframe(F{ids}(:,sel)) ;
%     set(h1,'color','k','linewidth',3) ;
%     set(h2,'color','y','linewidth',2) ;
%     pause(2)
    % Statistics
    
%     AverageTh = ( AverageTh + octTh ) / 2;
end

end