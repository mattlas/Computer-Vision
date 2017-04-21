cmake_minimum_required(VERSION 3.5.1)
project(c__Project)

set(CMAKE_CXX_STANDARD 11)

include_directories(vlfeat-0.9.20)
link_directories(vlfeat-0.9.20/bin/glnxa64)
set(SOURCE_FILES main.cpp TestClass.cpp TestClass.h)
add_executable(c__Project ${SOURCE_FILES})




