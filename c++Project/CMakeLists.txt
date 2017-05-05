
cmake_minimum_required(VERSION 3.5.1)
project(TreeMarkupToolbox)

set(CMAKE_CXX_STANDARD 11)

add_library(vl SHARED IMPORTED)
set_property(TARGET vl PROPERTY IMPORTED_LOCATION "lib/vlfeat-0.9.20/bin/glnxa64/libvl.so")
include_directories(lib/vlfeat-0.9.20)
link_directories(lib/vlfeat-0.9.20/bin/glnxa64)

set(SOURCE_FILES src/Main.cpp src/FeaturePoints.cpp
        src/FeaturePoints.h src/KeyPoint.h src/KeyPoint.cpp)
add_executable(TreeMarkupToolbox ${SOURCE_FILES})
target_link_libraries(TreeMarkupToolbox vl)

# Find the QtWidgets library
set(CMAKE_PREFIX_PATH /home/5dv115/Qt/5.8/gcc_64/lib/cmake/Qt5Widgets)
find_package(Qt5Widgets)
target_link_libraries(TreeMarkupToolbox Qt5::Widgets)
