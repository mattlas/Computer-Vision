#ifndef __NEIGHBOURS_H
#define __NEIGHBOURS_H
#include <string>
#include <list>
#include <vector>
#include <math.h>
#include "../spike/neighbours/exif.h"
#include "ImageData.h"
#include "HomographyData.h"

/**
 * Class used to find all the images neighbours. Saves images to imagePairs a 2dim vector.
 */
namespace neighbours {
std::vector<std::vector<HomographyData*>> pairs(std::vector<ImageData*> nodes);
}
#endif
