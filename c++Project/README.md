# C++

## Project Build order

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:lib/vlfeat-0.9.20/bin/glnxa64

cmake CMakeLists.txt

make

./TreeMarkupToolbox [path to pgm image file]

### Qt
cd install

./install/qt-unified-linux-x64-2.0.5-2-online.run

install it under: /home/5dv115/Qt/5.8/...

install OpenGL: sudo apt-get install mesa-common-dev

### Compile OpenCV

sudo bash install/opencv_install.sh

#### src
source for the application

#### tests
test code for the application

#### lib
libraries

#### include 
interface for the library *.h

#### install
install files/scripts for external libraries that isn't included in /lib/

#### doc
documentation of any kind

#### spikes
small tests for proof-of-concept

#### Geotag

#### Image compression

#### Find neighbours

#### SIFT

#### RANSAC

#### Least-Squares

#### Mosaic
