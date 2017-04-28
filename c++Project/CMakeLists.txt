cmake_minimum_required(VERSION 3.5.1)
project(TreeMarkupToolbox)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin)

add_library(vl SHARED IMPORTED)
<<<<<<< HEAD
set_property(TARGET vl PROPERTY IMPORTED_LOCATION "lib/vlfeat-0.9.20/bin/glnxa64/libvl.so")
include_directories(lib/vlfeat-0.9.20)
link_directories(lib/vlfeat-0.9.20/bin/glnxa64)
include_directories("${PROJECT_SOURCE_DIR}/include")

set(SOURCE_FILES src/Main.cpp include/FeaturePoints.h src/FeaturePoints.cpp)
=======
set_property(TARGET vl PROPERTY IMPORTED_LOCATION "${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20/bin/glnxa64/libvl.so")
find_package( OpenCV REQUIRED )
include_directories( ${OpenCV_INCLUDE_DIRS} )

include_directories(${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20)

link_directories(${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20/bin/glnxa64)

set(SOURCE_FILES ${PROJECT_SOURCE_DIR}/src/Main.cpp)
>>>>>>> 753e8cf16b71d1e69ddbf30831a75c0d0c6782fc
add_executable(TreeMarkupToolbox ${SOURCE_FILES})
target_link_libraries(TreeMarkupToolbox vl ${OpenCV_LIBS})
