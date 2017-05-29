/**
 * Class to run the pipeline of creating a mosaic and storing the required data.
 */

#ifndef TREEMARKUPTOOLBOX_MOSAICDATA_H
#define TREEMARKUPTOOLBOX_MOSAICDATA_H

#include <string>
#include <vector>
#include "KeyPoint.h"
#include "FeaturePoints.h"
#include "imaq.h"
#include "ImageData.h"
#include "neighbours.h"
#include <memory>
//#include <thread>
#include <pthread.h>
#include <mutex>
#include <set>
#include "opencv2/core.hpp"

//#include <unordered_map>

#include "QObject"
#include "HomographyData.h"

class MosaicData : public QObject{
    Q_OBJECT

private:
    //std::unordered_map<int,ImageData> imageList;
    std::vector<ImageData> imageList;
    std::vector<std::string> directoryList;
    std::vector<std::string> pgmFileNames;
    std::string pgmFolder;
    std::vector<std::string> fileNames;
    std::vector<FeaturePoints> featurePointList;
    std::mutex readMutex;
    std::mutex writeMutex;
    std::string input_path;
    std::string pgm_path;
    ImageData* referenceImage;

    std::vector<std::vector<HomographyData>> imagePairs;
    std::set<int> checkHomList;
    std::map<int,int> recursionList;
    std::vector<HomographyData> homographyList;

    imaq *im = NULL;

public:
    /**
     * Constructor - an empty constructor called to initiate class.
     */
    MosaicData(std::string input_path, std::string pgm_path);

public slots:
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

    void extractFeaturePointsThreaded();

    void createThreads();

    void ubcMatch();

    static void classWrapper(MosaicData* mosaicData);

    void createNeighbours();


private:
    void readFiles(std::string string);
    std::vector<std::string> readDirectoryFiles(const std::string &dir);
    void readPGMFromFolder();

    void convertToPGM(std::string string);

    void createImages();
    void stitchImages();
    void findPath();
    void calculatePairs(std::vector<HomographyData> &startNode, HomographyData prevNode, int recursionDepth);
    void calculateHomographies(std::vector<HomographyData> finalHomList);


signals:
    void finished();

    void recurseHomographies(HomographyData data);
};


#endif //TREEMARKUPTOOLBOX_MOSAICDATA_H
