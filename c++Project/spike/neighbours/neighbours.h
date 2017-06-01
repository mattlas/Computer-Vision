#ifndef __NEIGHBOURS_H
#define __NEIGHBOURS_H
#include <string>
#include <list>
#include <vector>
#include "exif.h"


struct GPS {
    exif::EXIFInfo imageData;
    double distance;
};

namespace neighbours{
    std::vector<std::vector<GPS>> pairs(std::list<exif::EXIFInfo> nodes);
}
#endif
