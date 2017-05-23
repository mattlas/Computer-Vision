#ifndef __NEIGHBOURS_H
#define __NEIGHBOURS_H
#include <string>
#include <list>
#include "exif.h"

namespace neighbours{
   std::list<std::list<exif::EXIFInfo>> pairs(std::list<exif::EXIFInfo> nodes);
}
#endif
