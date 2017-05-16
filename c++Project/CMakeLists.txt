
cmake_minimum_required(VERSION 3.5.1)
project(TreeMarkupToolbox )

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_AUTOUIC ON)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
#set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)
#set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin)
#set(SOURCE_FILES ${PROJECT_SOURCE_DIR}/src/Main.cpp ${PROJECT_SOURCE_DIR}/src/FeaturePoints.cpp ${PROJECT_SOURCE_DIR}/src/FeaturePoints.h ${PROJECT_SOURCE_DIR}/src/KeyPoint.h ${PROJECT_SOURCE_DIR}/src/KeyPoint.cpp ${PROJECT_SOURCE_DIR}/src/imaq.h ${PROJECT_SOURCE_DIR}/src/imaq.cpp)
set(SOURCE_FILES ${PROJECT_SOURCE_DIR}/src/Main.cpp ${PROJECT_SOURCE_DIR}/src/mainwindow.cpp ${PROJECT_SOURCE_DIR}/src/FeaturePoints.cpp ${PROJECT_SOURCE_DIR}/src/FeaturePoints.h ${PROJECT_SOURCE_DIR}/src/KeyPoint.h ${PROJECT_SOURCE_DIR}/src/KeyPoint.cpp src/MosaicData.cpp src/MosaicData.h src/DirectoryReader.cpp src/DirectoryReader.h src/MatchPoints.cpp src/MatchPoints.h)

# OpenCV library
find_package( OpenCV REQUIRED )
include_directories( ${OpenCV_INCLUDE_DIRS} )

# MEX library
#include_directories(/pkg/matlab/2017a/extern/include)

# vlfeat library
add_library(vl SHARED IMPORTED)
set_property(TARGET vl PROPERTY IMPORTED_LOCATION "${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20/bin/glnxa64/libvl.so")
include_directories(${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20)

#add_library(match ${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20/toolbox/sift/vl_ubcmatch.c)
#include_directories(${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20/toolbox)


link_directories(${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20/bin/glnxa64)

# Find the QtWidgets library
set(CMAKE_PREFIX_PATH /home/5dv115/Qt/5.8/gcc_64/lib/cmake/Qt5Widgets)
find_package(Qt5Widgets)
qt5_add_resources(UI_RESOURCES resources.qrc)

set(THREADS_PREFER_PTHREAD_FLAG ON)
find_package(Threads REQUIRED)

# Project files
add_executable(TreeMarkupToolbox ${SOURCE_FILES} ${UI_RESOURCES})
#target_link_libraries(TreeMarkupToolbox vl Qt5::Widgets ${OpenCV_LIBS})
target_link_libraries(TreeMarkupToolbox vl Qt5::Widgets Threads::Threads)
