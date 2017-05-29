#include <iostream>
#include <limits>
#include <cstddef>
#include "neighbours.h"

namespace neighbours {
std::vector<std::vector<HomographyData>> pairs(
		std::vector<ImageData> nodes){
	std::vector <HomographyData > filtered;
	std::vector<std::vector<double>> distances;
	std::vector < std::vector < HomographyData >> pairs;

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
            HomographyData *homographyData = new HomographyData();
            homographyData->setImageData(&nodes[i]);
			filtered.push_back(*homographyData);
		}
	}

	if(filtered.size() < 2)
		return pairs;

	//calculate image pair distances
	for(std::size_t i = 0; i < filtered.size(); i++){
		for(std::size_t j = 0; j < filtered.size(); j++){
			x1 = filtered[i].getImageData()->getInfo().GeoLocation.Longitude;
			y1 = filtered[i].getImageData()->getInfo().GeoLocation.Latitude;
			x2 = filtered[j].getImageData()->getInfo().GeoLocation.Longitude;
			y2 = filtered[j].getImageData()->getInfo().GeoLocation.Latitude;
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
		for(std::size_t j = 0; j < filtered.size(); j++){

			double d = distances[i][j];
			if(neighbourlimit > d){
				neighbourlimit = d;
			}
		}
		neighbourlimit *= 1.2;
		
		for(std::size_t j = 0; j < filtered.size(); j++){
			if((distances[i][j] <= neighbourlimit) && (i != j)){
                pairs[i][j].setDistance(distances[i][j]);
                pairs[i].push_back(filtered[j]);

			}
		}
	}

	return pairs;
}
}
