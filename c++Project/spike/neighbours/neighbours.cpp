#include <math.h>
#include <limits>
#include <cstddef>
#include <iostream>
#include "neighbours.h"

namespace neighbours {
std::vector<std::vector<exif::EXIFInfo>> pairs(
		std::vector<exif::EXIFInfo> nodes){
	std::vector < exif::EXIFInfo > filtered;
	std::vector<std::vector<double>> distances;
	std::vector < std::vector < exif::EXIFInfo >> pairs;

	double x1; //longitude
	double y1; //latitude
	double x2;
	double y2;

	//filter ascend/descend images
	double maxheight = 0;
	double altitude = 0;
	for(std::size_t i = 0; i < nodes.size(); i++){
		altitude = nodes[i].GeoLocation.Altitude;
		if(maxheight < altitude){
			maxheight = altitude;
		}
	}

	for(std::size_t i = 0; i < nodes.size(); i++){
		if(0.98 * maxheight <= nodes[i].GeoLocation.Altitude){
			filtered.push_back(nodes[i]);
		}
	}

	if(filtered.size() < 2)
		return pairs;

	//calculate image pair distances
	for(std::size_t i = 0; i < filtered.size(); i++){
		for(std::size_t j = i + 1; j < filtered.size(); j++){
			x1 = filtered[i].GeoLocation.Longitude;
			y1 = filtered[i].GeoLocation.Latitude;
			x2 = filtered[j].GeoLocation.Longitude;
			y2 = filtered[j].GeoLocation.Latitude;
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
		for(std::size_t j = i + 1; j < filtered.size()-1; j++){
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
	
	double n = 0;
	for(std::size_t i = 0; i < filtered.size(); i++){
		n += (pairs[i].size()-1);
	}
	n /= filtered.size();
	
	printf("%f\n", n);

	return pairs;
}
}
