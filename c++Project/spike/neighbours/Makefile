CXX=g++
CXXFLAGS=-O2 -pedantic -Wall -Wextra -ansi -std=c++11

all: demo

neighbours.o: neighbours.cpp
	$(CXX) $(CXXFLAGS) -c neighbours.cpp

DirectoryReader.o: DirectoryReader.cpp
	$(CXX) $(CXXFLAGS) -c DirectoryReader.cpp

exif.o: exif.cpp
	$(CXX) $(CXXFLAGS) -c exif.cpp

demo: neighbours.o DirectoryReader.o exif.o demo.cpp
	$(CXX) $(CXXFLAGS) -o demo neighbours.o DirectoryReader.o exif.o demo.cpp

clean:
	rm -f *.o demo demo.exe
