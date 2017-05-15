# C++

## Project Build order

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:lib/vlfeat-0.9.20/bin/glnxa64

cmake CMakeLists.txt

make

./TreeMarkupToolbox [path to pgm image file]

### Qt
install latest Qt pack

install OpenGL: sudo apt-get install mesa-common-dev

### Call Matlab from C++ in Linux
-MATLAB Coder translates Matlab code into c++
-Only works for MATLAB functions, not scripts
-Must know about input data types and set them directly

### Create standalone application from Matlab code
The guide below is an easy example of how to make a standalone exectable from MATLAB code:

To skip problems with login, read the guide in MATLAB by pasting this link in the MATLAB command window:
web(fullfile(docroot, 'compiler/create-and-install-a-standalone-application-from-matlab-code.html'))

Some notes on the guide:
-On our systems, the magicsquare.m is located at /pkg/matlab/2017a/extern/examples/compiler/magicsquare.m

-Choose to bundle the runtime package instead of downloading 

-To run the exectable the path to the runtime libs might have to be set:
export LD_LIBRARY_PATH=MATLAB_Runtime_Root/v92/runtime/glnxa64:MATLAB_Runtime_Root/v92/bin/glnxa64:MATLAB_Runtime_Root/v92/sys/os/glnxa64

OTHER INFORMATION
Start guide:
web(fullfile(docroot, 'compiler/index.html'))

Compilation limitations:
web(fullfile(docroot, 'compiler/limitations-about-what-may-be-compiled.html'))

### Qt
cd install

./install/qt-unified-linux-x64-2.0.5-2-online.run

install it under: /home/5dv115/Qt/5.8/...

install OpenGL: sudo apt-get install mesa-common-dev

### Compile OpenCV

sudo bash install/opencv_install.sh

### Compile LAPACK

http://matrixprogramming.com/2011/04/using-lapack-from-c

wget http://www.netlib.org/lapack/lapack-3.7.0.tgz

tar zxvf lapack-3.7.0.tgz

cd lapack-3.7.0

cp INSTALL/make.inc.gfortran make.inc

make blaslib

make lapacklib

### File System

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

## Mosaic Pipeline
Geotag->Image compression->Find neighbours->SIFT->RANSAC->Bundle Adjustment->Mosaic

#### Geotag

#### Image compression

#### Find neighbours

#### SIFT

#### RANSAC

#### Least-Squares

#### Mosaic
