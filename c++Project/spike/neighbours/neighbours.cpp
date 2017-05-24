#include "neighbours.h"

namespace neighbours {
std::vector<std::vector<GPS>> pairs(std::vector<exif::EXIFInfo> nodes){

	std::vector < std::vector < GPS >> pairs;
	std::vector < GPS > neighbours;
	neighbours.resize(nodes.size());

	double longitude1 = -1;
	double latitude1 = -1;
	double longitude2 = -1;
	double latitude2 = -1;
	for(std::size_t i = 0; i < nodes.size(); i++){
		for(std::size_t j = i; j < nodes.size(); j++){
			neighbours[j].imageData = nodes[j];
			neighbours[j + 1].imageData = nodes[j + 1];

			longitude1 = neighbours[i].imageData.GeoLocation.Longitude;
			latitude1 = neighbours[j].imageData.GeoLocation.Latitude;
			longitude2 = neighbours[i + 1].imageData.GeoLocation.Longitude;
			latitude2 = neighbours[i + 1].imageData.GeoLocation.Latitude;
			neighbours[i].distance = sqrt((longitude1 * longitude1)
					+ (latitude1 * latitude1)
					+ (longitude2 * longitude2 + latitude2 * latitude2)
					- 2 * (longitude1 * longitude2 + latitude1 * latitude2));

			pairs.push_back(neighbours);
		}
	}

	return pairs;
}
}
