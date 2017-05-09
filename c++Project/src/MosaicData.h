/**
 * Class to run the pipeline of creating a mosaic and storing the required data.
 */

#ifndef TREEMARKUPTOOLBOX_MOSAICDATA_H
#define TREEMARKUPTOOLBOX_MOSAICDATA_H

#include <string>
#include <vector>
#include "FeaturePoints.h"

class MosaicData {
private:
    std::vector<std::string> directoryList;
    std::vector<std::string> pgmFileNames;
    std::vector<FeaturePoints> featurePointList;

public:
    /**
     * Constructor - an empty constructor called to initiate class.
     */
    MosaicData(void);

    /**
     * Add the directory to the directoryList
     * @param dir
     */
    void addDirectory(std::string dir);

    /**
     * Start the pipeline process.
     * OBS. The directoryList will need at least one directory for this function to work.
     */
    void startProcess();
    void extractFeaturePoints();

private:
    std::vector<std::string> readDirectoryFiles(const std::string &dir);
    void readPGMFromFolder();

};


#endif //TREEMARKUPTOOLBOX_MOSAICDATA_H
