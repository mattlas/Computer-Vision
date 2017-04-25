// Import OpenCV libraries
#include <opencv2/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>

#include <iostream>
#include <string>

using namespace cv;
using namespace std;
int main( int argc, char** argv )
{
    String imageName( "test1.jpg" );            // Default filename
    if( argc > 1)
    {
        imageName = argv[1];
    }
    Mat image;
    image = imread( imageName, IMREAD_COLOR ); // Read the Image file
    if( image.empty() )                         
    {
        cout <<  "Could not open or find the image" << std::endl ;
        return -1;
    }
    namedWindow( "Display window", WINDOW_AUTOSIZE ); // Create a new window to display the image
    imshow( "Display window", image );                // Display the image
    waitKey(0); // Wait for a keystroke in the window
    return 0;
}