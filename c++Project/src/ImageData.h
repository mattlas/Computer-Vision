//
// Created by 5dv115 on 5/8/17.
//

#ifndef TREEMARKUPTOOLBOX_IMAGEDATA_H
#define TREEMARKUPTOOLBOX_IMAGEDATA_H

#include <string>
#include <vector>

class ImageData {
private:
    std::vector<std::string> directories;
    std::vector<std::string> pgmFileNames;
    std::vector<std::string> GetDirectoryFiles(const std::string& dir);
    void readPGMFromFolder();
public:
    ImageData(void);
    void addDirectory(std::string dir);
    void startProcess();

};


#endif //TREEMARKUPTOOLBOX_IMAGEDATA_H
