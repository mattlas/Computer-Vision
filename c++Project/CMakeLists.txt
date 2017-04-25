cmake_minimum_required(VERSION 3.5.1)
project(TreeMarkupToolbox)

set(CMAKE_CXX_STANDARD 11)

add_library(vl SHARED IMPORTED)
set_property(TARGET vl PROPERTY IMPORTED_LOCATION "lib/vlfeat-0.9.20/bin/glnxa64/libvl.so")
include_directories(lib/vlfeat-0.9.20)
link_directories(lib/vlfeat-0.9.20/bin/glnxa64)

set(SOURCE_FILES src/Main.cpp)
add_executable(TreeMarkupToolbox ${SOURCE_FILES})
target_link_libraries(TreeMarkupToolbox vl)
