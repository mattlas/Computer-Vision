
cmake_minimum_required(VERSION 3.5.1)
project(TreeMarkupToolbox)

set(CMAKE_CXX_STANDARD 11)

#set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)
#set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin)
#set(SOURCE_FILES ${PROJECT_SOURCE_DIR}/src/Main.cpp ${PROJECT_SOURCE_DIR}/src/FeaturePoints.cpp ${PROJECT_SOURCE_DIR}/src/FeaturePoints.h ${PROJECT_SOURCE_DIR}/src/KeyPoint.h ${PROJECT_SOURCE_DIR}/src/KeyPoint.cpp ${PROJECT_SOURCE_DIR}/src/imaq.h ${PROJECT_SOURCE_DIR}/src/imaq.cpp)
set(SOURCE_FILES ${PROJECT_SOURCE_DIR}/src/Main.cpp ${PROJECT_SOURCE_DIR}/src/FeaturePoints.cpp ${PROJECT_SOURCE_DIR}/src/FeaturePoints.h ${PROJECT_SOURCE_DIR}/src/KeyPoint.h ${PROJECT_SOURCE_DIR}/src/KeyPoint.cpp src/ImageData.cpp src/ImageData.h)

# OpenCV library
find_package( OpenCV REQUIRED )
include_directories( ${OpenCV_INCLUDE_DIRS} )

# vlfeat library
add_library(vl SHARED IMPORTED)
set_property(TARGET vl PROPERTY IMPORTED_LOCATION "${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20/bin/glnxa64/libvl.so")
include_directories(${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20)
link_directories(${PROJECT_SOURCE_DIR}/lib/vlfeat-0.9.20/bin/glnxa64)

# Find the QtWidgets library
set(CMAKE_PREFIX_PATH /home/5dv115/Qt/5.8/gcc_64/lib/cmake/Qt5Widgets)	# Please don't make it local computer specific. if you have it installed in the computer, find_package() will find it anyway.
find_package(Qt5Widgets)


# Project files
add_executable(TreeMarkupToolbox ${SOURCE_FILES})
#target_link_libraries(TreeMarkupToolbox vl Qt5::Widgets ${OpenCV_LIBS})
target_link_libraries(TreeMarkupToolbox vl Qt5::Widgets)