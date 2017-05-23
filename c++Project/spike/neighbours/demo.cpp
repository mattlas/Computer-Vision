#include <dirent.h>
#include <memory>
#include <cstring>
#include <iostream>
#include <vector>
#include <list>
#include "exif.h"
#include "DirectoryReader.h"
#include "neighbours.h"

int main(){
	std::vector<std::string> dir = DirectoryReader::readDirectory("geotaggedPhotos");
	std::list<exif::EXIFInfo> nodes;
	for(std::vector<std::string>::iterator it = dir.begin() ; it != dir.end(); ++it){
    		exif::EXIFInfo result = exif::read(*it);
		nodes.push_back(result);
 	}
	neighbours::pairs(nodes);
	return 0;
}
