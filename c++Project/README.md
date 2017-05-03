# C++

## Project Build order

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:lib/vlfeat-0.9.20/bin/glnxa64

cmake CMakeLists.txt

make

./TreeMarkupToolbox


<<<<<<< HEAD
### Matlab in Linux
(modify)
setenv LD_LIBRARY_PATH matlabroot/bin/glnxa64:matlabroot/sys/os/glnxa64
=======
### Call Matlab from C++ in Linux
(Opens Matlab GUI NOT CORRECT)

setenv LD_LIBRARY_PATH matlabroot/bin/glnxa64:matlabroot/sys/os/glnxa64

>>>>>>> a65dc693b060dd0c3d43948ab2a81e6b2024621c
export LD_LIBRARY_PATH

### Compile LAPACK
http://matrixprogramming.com/2011/04/using-lapack-from-c

wget http://www.netlib.org/lapack/lapack-3.7.0.tgz
<<<<<<< HEAD
tar zxvf lapack-3.7.0.tgz
cp INSTALL/make.inc.gfortran make.inc
make blaslib
make lapacklib
ls *.a
//You should see blas_LINUX.a  and lapack_LINUX.a
mv blas_LINUX.a libblas.a
mv lapack_LINUX.a liblapack.a
=======

tar zxvf lapack-3.7.0.tgz

cp INSTALL/make.inc.gfortran make.inc

make blaslib

make lapacklib

ls *.a
>>>>>>> a65dc693b060dd0c3d43948ab2a81e6b2024621c

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

<<<<<<< HEAD
#### Bundle Adjustment
=======
#### Least-Squares
>>>>>>> a65dc693b060dd0c3d43948ab2a81e6b2024621c

#### Mosaic
