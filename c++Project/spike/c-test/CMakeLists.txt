cmake_minimum_required(VERSION 3.7)
project(Project)

set(CMAKE_C_STANDARD 99)



add_library(vlfeat_sift /home/5dv115/vlfeat-0.9.20/vl)

set(SOURCE_FILES test.c)
add_executable(Project ${SOURCE_FILES})

target_link_libraries(Project /home/5dv115/vlfeat-0.9.20/bin/glnxa64)

