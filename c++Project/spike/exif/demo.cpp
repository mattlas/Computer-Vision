#include <cstring>
#include <iostream>
#include "exif.h"

exif::EXIFInfo read(const std::string &filepath){
	exif::EXIFInfo result;

	// Read the JPEG file into a buffer
	FILE *fp = fopen(filepath.c_str(), "rb");
	if(!fp){
		std::cout << "Can't open file.\n" << std::strerror(errno) << filepath << std::endl;
		return result;
	}
	fseek(fp, 0, SEEK_END);
	unsigned long fsize = ftell(fp);
	rewind(fp);
	unsigned char *buf = new unsigned char[fsize];
	if(fread(buf, 1, fsize, fp) != fsize){
		std::cout << "Can't open file.\n" << std::strerror(errno) << filepath << std::endl;
		delete[] buf;
		return result;
	}
	fclose(fp);

	// Parse EXIF

	int code = result.parseFrom(buf, fsize);
	delete[] buf;
	if(code){
		std::cout << "Error parsing EXIF: code" << std::strerror(errno) << filepath << std::endl; // %d\n", code)
		return result;
	}

	// Dump EXIF information

	return result;
}

int main(){
	exif::EXIFInfo result = read("geotaggedPhotos/DSC01035_geotag.JPG");
	printf("GPS Latitude         : %f deg\n", result.GeoLocation.Latitude);
	printf("GPS Longitude        : %f deg\n", result.GeoLocation.Longitude);
	printf("GPS Altitude         : %f m\n", result.GeoLocation.Altitude);
	return 0;
}
