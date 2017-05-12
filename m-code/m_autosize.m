[original,map] = imread('Demo2.png');

figure(1)
imshow(original)
figure(2)
%gray
gray = rgb2gray(original);
[m,n]=size(gray)
imshow(gray)

% change background to white
for i=1:n
    for j=1:m
        if gray(i,j) == 0
            gray(i,j)=255;
        end
    end
end
figure(3)
imshow(gray)
% imwrite(gray,'grayimage.png')           



%binary
figure(4)
level = graythresh(gray);
Binary=im2bw(gray,level);
Binary=complementary(Binary); 
imshow(Binary)
% remove noise
Binary = medfilt2(Binary,[5,5]);

figure(5)
Fill = imfill(Binary,'holes');
imshow(Fill)
figure(6)
%remove continuous component
Remove = bwareaopen(Fill, round(0.00001*numel(Fill)));
imshow(Remove)

figure(6)
[label,num]= bwlabel(Remove,8);
subplot(1, 2, 1),imshow(label,[]);title('Labeled Image, from bwlabel()');
coloredLabels = label2rgb (label, 'hsv', 'k', 'shuffle'); % pseudo random color labels
% coloredLabels is an RGB image.  We could have applied a colormap instead (but only with R2014b and later)
subplot(1, 2, 2);imshow(coloredLabels);title('random color Labeled Image, from bwlabel()');
axis image;

Measurements = regionprops(label, gray, 'BoundingBox','Area');
AreaTresh=100000;
numberOfregion = size(Measurements, 1);
for k = 1 : numberOfregion
    ThisArea=Measurements(k).Area
    if ThisArea>AreaTresh
       thisBoundingbox=Measurements(k).BoundingBox
       
    end

end
CropImage = imcrop(original,thisBoundingbox);
figure(7)
imshow(CropImage)
size(CropImage)
imwrite(CropImage,'mosaic.png')
%%zero padding
