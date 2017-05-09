/**
 * Class is used to extract feature points and descriptors from a specific image.
 * The feature points can be acsessed from variable keyPoint and descriptors from
 * variable descriptor.
 */
#include <string>
#include <cstdint>
#include <vector>
#include "KeyPoint.h"


#ifndef C_PROJECT_FEATUREPOINTS_H
#define C_PROJECT_FEATUREPOINTS_H

class FeaturePoints {
private:
    std::vector<KeyPoint> keyPoints;
    std::vector<std::vector<uint16_t>> descriptors;
    std::string imageName;
    int id;

public:
    /**
     * Constructor
     */
    FeaturePoints(std::string name, int id);

    /**
     * The main method of this class, method calculated keypoints and stores them in the variables keyPonts
     * and descriptors.
     * @param path - The complete path to the image.
     */
    void calculatePoints();

    const std::vector<KeyPoint> &getKeyPoints() const;
    void setKeyPoints(const std::vector<KeyPoint> &keyPoints);
    const std::vector<std::vector<uint16_t>> &getDescriptors() const;
    void setDescriptors(const std::vector<std::vector<uint16_t>> &descriptors);

    /**
     * writes keyPoints to file
     * @param name
     */
    void writeKeyPoints(char *name);


    /**
     * writes descriptors to file
     * @param name
     */
    void writeDescriptors(char *name);

};
#endif //C_PROJECT_FEATUREPOINTS_H

