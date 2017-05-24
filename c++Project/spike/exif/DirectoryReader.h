//
// Created by 5dv115 on 5/9/17.
//

#ifndef TREEMARKUPTOOLBOX_DIRECTORYREADER_H
#define TREEMARKUPTOOLBOX_DIRECTORYREADER_H


#include <string>
#include <vector>

class DirectoryReader {
public:
    static std::vector<std::string> readDirectory(const std::string &dir);
};


#endif //TREEMARKUPTOOLBOX_DIRECTORYREADER_H
