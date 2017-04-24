function [ F, D ] = m_sift( imageIDs, octaveThreshold, featureThreshold)
% Pre-allocation
F = cell(size(imageIDs,1), 1);
D = cell(size(imageIDs,1), 1);
AverageTh = 0;
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
    numFeatures = 0;
    % Going until reaches threshold
    while numFeatures < featureThreshold
        [F{ids}, D{ids}] = vl_sift(img, 'FirstOctave', octTh, 'Magnif', 2, 'Levels', 5);
        numFeatures = size(F{ids},2);
        octTh = octTh -1;
    end
    
    % Statistics
    
    AverageTh = ( AverageTh + octTh ) / 2;
end

end