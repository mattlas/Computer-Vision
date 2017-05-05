# C++

## Project Build order

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:lib/vlfeat-0.9.20/bin/glnxa64

cmake CMakeLists.txt

make

./TreeMarkupToolbox [path to pgm image file]

### Call Matlab from C++ in Linux
(Opens Matlab GUI NOT CORRECT)

setenv LD_LIBRARY_PATH matlabroot/bin/glnxa64:matlabroot/sys/os/glnxa64

export LD_LIBRARY_PATH

### Qt
install latest Qt pack

install OpenGL: sudo apt-get install mesa-common-dev

### Compile OpenCV

(Still under construction)

sudo apt-get update

sudo apt-get upgrade

sudo apt-get install build-essential

sudo apt-get install cmake git libgtk2.0-dev pkg-config libavcodec-dev libavformat-dev libswscale-dev

sudo apt-get install python-dev python-numpy libtbb2 libtbb-dev libjpeg-dev libpng-dev libtiff-dev libjasper-dev libdc1394-22-dev

sudo apt-get install libv4l-dev

cd /usr/include/linux

sudo ln -s ../libv4l1-videodev.h videodev.h

cd ~/opencv

mkdir release

cd release

cmake -D CMAKE_BUILD_TYPE=RELEASE -D CMAKE_INSTALL_PREFIX=/usr/local ..

make -j$n //$n = number of cores or skip

### Compile LAPACK

http://matrixprogramming.com/2011/04/using-lapack-from-c

wget http://www.netlib.org/lapack/lapack-3.7.0.tgz

tar zxvf lapack-3.7.0.tgz

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
