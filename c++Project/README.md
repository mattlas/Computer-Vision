# C++

## Project Build order

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:lib/vlfeat-0.9.20/bin/glnxa64

cmake CMakeLists.txt

make

./TreeMarkupToolbox


### Matlab in Linux
(modify)
setenv LD_LIBRARY_PATH matlabroot/bin/glnxa64:matlabroot/sys/os/glnxa64
export LD_LIBRARY_PATH

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

#### Bundle Adjustment

#### Mosaic
