#ifndef __NEIGHBOURS_H
#define __NEIGHBOURS_H
#include <string>
#include <list>
#include <vector>
#include <math.h>
#include "exif.h"

struct GPS{
	exif::EXIFInfo imageData;
	double distance;
};

namespace neighbours {
std::vector<std::vector<GPS>> pairs(std::vector<exif::EXIFInfo> nodes);
}
#endif
