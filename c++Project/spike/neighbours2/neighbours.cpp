#include "neighbours.h"

namespace neighbours{
   std::list<std::list<exif::EXIFInfo>> pairs(std::list<exif::EXIFInfo> nodes){
	std::list<std::list<exif::EXIFInfo>> pairs;
	for(std::list<exif::EXIFInfo>::iterator it=nodes.begin(); it != nodes.end(); ++it){
		std::list<exif::EXIFInfo> neighbours;
		pairs.push_back(neighbours);
 	}
	return pairs;
   }
}
