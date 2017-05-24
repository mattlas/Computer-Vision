function [ output_args ] = m_copyImages( image_path )
%M_COPYIMAGES This function copies images from input to the selected
% destination 
%   Detailed explanation goes here
mkdir C:/ copy_TreemApp 
copyfile image_path C:/copy_TreemApp f

end

