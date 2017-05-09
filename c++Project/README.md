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
(Opens Matlab GUI NOT CORRECT)

setenv LD_LIBRARY_PATH matlabroot/bin/glnxa64:matlabroot/sys/os/glnxa64

export LD_LIBRARY_PATH

### Qt
cd install

./install/qt-unified-linux-x64-2.0.5-2-online.run

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
