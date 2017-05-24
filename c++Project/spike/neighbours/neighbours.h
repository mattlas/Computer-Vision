#ifndef __NEIGHBOURS_H
#define __NEIGHBOURS_H
#include <string>
#include <vector>
#include "exif.h"

namespace neighbours {
std::vector<std::vector<exif::EXIFInfo>> pairs(std::vector<exif::EXIFInfo> nodes);
}
#endif
