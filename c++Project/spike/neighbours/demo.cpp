#include <dirent.h>
#include <memory>
#include <cstring>
#include <iostream>
#include <vector>
#include "exif.h"
#include "DirectoryReader.h"

int main(){
	std::vector<std::string> dir = DirectoryReader::readDirectory("geotaggedPhotos");
	for(std::vector<std::string>::iterator it = dir.begin() ; it != dir.end(); ++it){
    		exif::EXIFInfo result = exif::read(*it);
		printf("GPS Latitude         : %f deg\n", result.GeoLocation.Latitude);
		printf("GPS Longitude        : %f deg\n", result.GeoLocation.Longitude);
		printf("GPS Altitude         : %f m\n", result.GeoLocation.Altitude);
 	}
	return 0;
}
