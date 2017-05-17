/**
 * Class to run the pipeline of creating a mosaic and storing the required data.
 */

#ifndef TREEMARKUPTOOLBOX_MOSAICDATA_H
#define TREEMARKUPTOOLBOX_MOSAICDATA_H

#include <string>
#include <vector>
#include "FeaturePoints.h"
#include "imaq.h"
#include <memory>
//#include <thread>
#include <pthread.h>
#include <mutex>

class MosaicData {
private:
    std::vector<std::string> directoryList;
    std::vector<std::string> pgmFileNames;
    std::string pgmFolder;
    std::vector<std::string> fileNames;
    std::vector<FeaturePoints> featurePointList;
    std::mutex readMutex;
    std::mutex writeMutex;

    imaq *im = NULL;

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
    void startProcess(const char *string, const char *string1);

    void extractFeaturePoints();

    void extractFeaturePointsThreaded();

    void createThreads();


    void ubcMatch();

    static void classWrapper(MosaicData* mosaicData);

private:
    void readFiles(const char *string);
    std::vector<std::string> readDirectoryFiles(const std::string &dir);
    void readPGMFromFolder();

    void convertToPGM(const char *string);
};


#endif //TREEMARKUPTOOLBOX_MOSAICDATA_H
