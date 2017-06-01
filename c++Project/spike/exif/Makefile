CXX=g++
CXXFLAGS=-O2 -pedantic -Wall -Wextra -ansi -std=c++11

all: demo

DirectoryReader.o: DirectoryReader.cpp
	$(CXX) $(CXXFLAGS) -c DirectoryReader.cpp

exif.o: exif.cpp
	$(CXX) $(CXXFLAGS) -c exif.cpp

demo: DirectoryReader.o exif.o demo.cpp
	$(CXX) $(CXXFLAGS) -o demo DirectoryReader.o exif.o demo.cpp

clean:
	rm -f *.o demo demo.exe
