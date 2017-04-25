cmake_minimum_required(VERSION 3.5.1)
project(c__Project)

set(CMAKE_CXX_STANDARD 11)

add_library(vl SHARED IMPORTED)
set_property(TARGET vl PROPERTY IMPORTED_LOCATION "vlfeat-0.9.20/bin/glnxa64/libvl.so")
include_directories(vlfeat-0.9.20)
link_directories(vlfeat-0.9.20/bin/glnxa64)

set(SOURCE_FILES main.cpp)
add_executable(c__Project ${SOURCE_FILES})
target_link_libraries(c__Project vl)
