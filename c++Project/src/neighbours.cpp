#include <iostream>
#include <limits>
#include <cstddef>
#include "neighbours.h"

namespace neighbours {
std::vector<std::vector<ImageData>> pairs(
		std::vector<ImageData> nodes){
	std::vector <ImageData > filtered;
	std::vector<std::vector<double>> distances;
	std::vector < std::vector < ImageData >> pairs;

	double x1; //longitude
	double y1; //latitude
	double x2;
	double y2;

	//filter ascend/descend images
	double maxheight = 0;
	double altitude = 0;
	for(std::size_t i = 0; i < nodes.size(); i++){
		altitude = nodes[i].getInfo().GeoLocation.Altitude;
		if(maxheight < altitude){
			maxheight = altitude;
		}
	}

	for(std::size_t i = 0; i < nodes.size(); i++){
		if(0.98 * maxheight <= nodes[i].getInfo().GeoLocation.Altitude){
			filtered.push_back(nodes[i]);
		}
	}

	if(filtered.size() < 2)
		return pairs;

	//calculate image pair distances
	for(std::size_t i = 0; i < filtered.size(); i++){
		for(std::size_t j = i + 1; j < filtered.size() - 1; j++){
			x1 = filtered[i].getInfo().GeoLocation.Longitude;
			y1 = filtered[i].getInfo().GeoLocation.Latitude;
			x2 = filtered[j].getInfo().GeoLocation.Longitude;
			y2 = filtered[j].getInfo().GeoLocation.Latitude;
			distances.resize(filtered.size());
			distances[i].resize(filtered.size());
			distances[i][j] = sqrt(
					x1 * x1 + y1 * y1 + x2 * x2 + y2 * y2
							- 2 * (x1 * x2 + y1 * y2));
		}
	}

	//filter closest neighbours
	pairs.resize(filtered.size());
	for(std::size_t i = 0; i < filtered.size(); i++){
		pairs[i].push_back(filtered[i]);
		double neighbourlimit = std::numeric_limits<double>::max();
		for(std::size_t j = i + 1; j < filtered.size(); j++){
			double d = distances[i][j];
			if(neighbourlimit > d){
				neighbourlimit = d;
			}
		}
		neighbourlimit *= 1.2;
		
		for(std::size_t j = i + 1; j < filtered.size(); j++){
			if(distances[i][j] <= neighbourlimit){
				pairs[i].push_back(filtered[j]);
			}
		}
	}

	return pairs;
}
}
