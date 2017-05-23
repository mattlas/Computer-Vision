#include "neighbours.h"


namespace neighbours{
    std::vector<std::vector<GPS>> pairs(std::vector<exif::EXIFInfo> nodes){

        std::vector<std::vector<GPS>> pairs;
        std::vector<GPS> neighbours;
        neighbours.resize(nodes.size());

        int i = 0;
        int j = 0;
        double latitude1 = -1;
        double longitude1 = -1;
        double latitude2 = -1;
        double longitude2 = -1;
        for(int i = 0; i < nodes.size(); i++) {
            for(std::vector<exif::EXIFInfo>::iterator it=nodes.begin(); it != nodes.end(); it++){

                neighbours[j].imageData = nodes[j];
                neighbours[j+1].imageData = nodes[j+1];

                latitude1 = neighbours[j].imageData.GeoLocation.Latitude;
                longitude1 = neighbours[i].imageData.GeoLocation.Longitude;
                latitude2 = neighbours[i+1].imageData.GeoLocation.Latitude;
                longitude2 = neighbours[i+1].imageData.GeoLocation.Longitude;
                neighbours[i].distance = (latitude1*latitude1+longitude1*longitude1)
                                         +(latitude2*latitude2+longitude2*longitude2)
                                         -2*(latitude1*latitude2+longitude1*longitude2);

                pairs.push_back(neighbours);
                i++;
             }
 	    }

        return pairs;
    }
}
